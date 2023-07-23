/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/

package forestry;

import com.google.common.base.Preconditions;
import forestry.api.climate.ClimateManager;
import forestry.api.core.ForestryAPI;
import forestry.api.core.ISetupListener;
import forestry.api.core.ISpriteRegistry;
import forestry.api.recipes.*;
import forestry.arboriculture.loot.CountBlockFunction;
import forestry.arboriculture.loot.GrafterLootModifier;
import forestry.core.EventHandlerCore;
import forestry.core.circuits.CircuitRecipe;
import forestry.core.climate.ClimateFactory;
import forestry.core.climate.ClimateRoot;
import forestry.core.climate.ClimateStateHelper;
import forestry.core.config.Constants;
import forestry.core.data.*;
import forestry.core.errors.EnumErrorCode;
import forestry.core.errors.ErrorStateRegistry;
import forestry.core.gui.elements.GuiElementFactory;
import forestry.core.loot.ConditionLootModifier;
import forestry.core.loot.OrganismFunction;
import forestry.core.models.ModelBlockCached;
import forestry.core.network.NetworkHandler;
import forestry.core.network.PacketHandlerServer;
import forestry.core.proxy.*;
import forestry.core.recipes.HygroregulatorRecipe;
import forestry.core.render.ColourProperties;
import forestry.core.render.ForestrySpriteUploader;
import forestry.core.render.TextureManagerForestry;
import forestry.core.utils.ForgeUtils;
import forestry.core.worldgen.VillagerJigsaw;
import forestry.factory.recipes.*;
import forestry.modules.ForestryModuleUids;
import forestry.modules.ForestryModules;
import forestry.modules.ModuleManager;
import forestry.modules.features.ModFeatureRegistry;
import genetics.api.alleles.IAllele;
import genetics.utils.AlleleUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

import javax.annotation.Nullable;

/**
 * Forestry Minecraft Mod
 *
 * @author SirSengir
 */
@Mod("forestry")
public class Forestry {

	@SuppressWarnings("NullableProblems")
	public static Forestry instance;

	public Forestry() {
		instance = this;
		ForestryAPI.instance = this;
		ForestryAPI.forestryConstants = new Constants();
		ForestryAPI.errorStateRegistry = new ErrorStateRegistry();
		ClimateManager.climateRoot = ClimateRoot.getInstance();
		ClimateManager.climateFactory = ClimateFactory.INSTANCE;
		ClimateManager.stateHelper = ClimateStateHelper.INSTANCE;
		EnumErrorCode.init();

		ModuleManager moduleManager = ModuleManager.getInstance();
		ForestryAPI.moduleManager = moduleManager;
		moduleManager.registerContainers(new ForestryModules());
		ModuleManager.runSetup();
		NetworkHandler networkHandler = new NetworkHandler();
		//				DistExecutor.runForDist(()->()-> networkHandler.clientPacketHandler(), ()->()-> networkHandler.serverPacketHandler());
		IEventBus modEventBus = ForgeUtils.modBus();
		modEventBus.addListener(this::setup);
		modEventBus.addListener(this::registerCapabilities);
		//		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
		modEventBus.addListener(this::processIMCMessages);
		modEventBus.addListener(this::clientSetupRenderers);
		modEventBus.addListener(this::gatherData);
		EventHandlerCore eventHandlerCore = new EventHandlerCore();
		MinecraftForge.EVENT_BUS.register(eventHandlerCore);
		MinecraftForge.EVENT_BUS.register(this);
		Proxies.render = DistExecutor.runForDist(() -> ProxyRenderClient::new, () -> ProxyRender::new);
		Proxies.common = DistExecutor.runForDist(() -> ProxyClient::new, () -> ProxyCommon::new);

		ModuleManager.getModuleHandler().runSetup();
		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> clientInit(modEventBus, networkHandler));
		modEventBus.addListener(EventPriority.NORMAL, false, FMLCommonSetupEvent.class, evt -> networkHandler.serverPacketHandler());
	}

	public void clientSetupRenderers(EntityRenderersEvent.RegisterRenderers event) {
		ModuleManager.getModuleHandler().registerGuiFactories();

		for (ModFeatureRegistry value : ModFeatureRegistry.getRegistries().values()) {
			value.clientSetupRenderers(event);
		}
	}

	@Nullable
	private static PacketHandlerServer packetHandler;

	public static PacketHandlerServer getPacketHandler() {
		Preconditions.checkNotNull(packetHandler);
		return packetHandler;
	}

	private void setup(FMLCommonSetupEvent event) {
		// Forestry's villager houses
		event.enqueueWork(VillagerJigsaw::init);

		packetHandler = new PacketHandlerServer();

		// Register event handler
		//TODO - DistExecutor
		callSetupListeners(true);
		ModuleManager.getModuleHandler().runPreInit();
		Proxies.render.registerItemAndBlockColors();
		//TODO put these here for now
		ModuleManager.getModuleHandler().runInit();
		callSetupListeners(false);
		ModuleManager.getModuleHandler().runPostInit();
	}

	private void registerCapabilities(RegisterCapabilitiesEvent event) {
		ModuleManager.getModuleHandler().registerCapabilities(event::register);
	}

	//TODO: Move to somewhere else
	private void callSetupListeners(boolean start) {
		for (IAllele allele : AlleleUtils.getAlleles()) {
			if (allele instanceof ISetupListener listener) {
				if (start) {
					listener.onStartSetup();
				} else {
					listener.onFinishSetup();
				}
			}
		}
	}

	private void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		ForestryBlockTagsProvider blockTagsProvider = new ForestryBlockTagsProvider(generator.getPackOutput(), generator, existingFileHelper);

		generator.addProvider(event.includeServer(), blockTagsProvider);
		generator.addProvider(event.includeServer(), new ForestryAdvancementProvider(generator));
		generator.addProvider(event.includeServer(), new ForestryItemTagsProvider(generator, blockTagsProvider, existingFileHelper));
		generator.addProvider(event.includeServer(), new ForestryBackpackTagProvider(generator, blockTagsProvider, existingFileHelper));
		generator.addProvider(event.includeServer(), new ForestryFluidTagsProvider(generator, existingFileHelper));
		generator.addProvider(event.includeServer(), new ForestryLootTableProvider(generator));
		generator.addProvider(event.includeServer(), new WoodBlockStateProvider(generator));
		generator.addProvider(event.includeServer(), new WoodBlockModelProvider(generator));
		generator.addProvider(event.includeServer(), new WoodItemModelProvider(generator));
		generator.addProvider(event.includeServer(), new ForestryBlockStateProvider(generator));
		generator.addProvider(event.includeServer(), new ForestryBlockModelProvider(generator));
		generator.addProvider(event.includeServer(), new ForestryItemModelProvider(generator));
		generator.addProvider(event.includeServer(), new ForestryRecipeProvider(generator));
		generator.addProvider(event.includeServer(), new ForestryMachineRecipeProvider(generator));
		generator.addProvider(event.includeServer(), new ForestryLootModifierProvider(generator));
	}

	private void clientInit(IEventBus modEventBus, NetworkHandler networkHandler) {
		modEventBus.addListener(EventPriority.NORMAL, false, RegisterColorHandlersEvent.Block.class, x -> {
			Minecraft minecraft = Minecraft.getInstance();
			ForestrySpriteUploader spriteUploader = new ForestrySpriteUploader(minecraft.textureManager, TextureManagerForestry.LOCATION_FORESTRY_TEXTURE, "gui");
			TextureManagerForestry.getInstance().init(spriteUploader);
			ResourceManager resourceManager = minecraft.getResourceManager();
			if (resourceManager instanceof ReloadableResourceManager reloadableManager) {
				reloadableManager.registerReloadListener(ColourProperties.INSTANCE);
				reloadableManager.registerReloadListener(GuiElementFactory.INSTANCE);
				reloadableManager.registerReloadListener(spriteUploader);
			}
			//EntriesCategory.registerSearchTree();
			ModuleManager.getModuleHandler().runClientInit();

		});
		modEventBus.addListener(EventPriority.NORMAL, false, FMLLoadCompleteEvent.class, fmlLoadCompleteEvent -> networkHandler.clientPacketHandler());
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = Constants.MOD_ID)
	public static class RegistryEvents {

		private RegistryEvents() {
		}

		@SubscribeEvent(priority = EventPriority.HIGH)
		public static void createFeatures(RegisterEvent event) {
			ModuleManager.getModuleHandler().createFeatures();
		}

		@SubscribeEvent(priority = EventPriority.LOW)
		public static void createObjects(RegisterEvent event) {
			ModuleManager.getModuleHandler().createObjects((type, moduleID) -> !moduleID.equals(ForestryModuleUids.CRATE));
			ModuleManager.getModuleHandler().runRegisterBackpacksAndCrates();
			ModuleManager.getModuleHandler().createObjects((type, moduleID) -> moduleID.equals(ForestryModuleUids.CRATE));
		}

		@SubscribeEvent(priority = EventPriority.LOWEST)
		public static void registerObjects(RegisterEvent event) {
			ModuleManager.getModuleHandler().registerObjects(event);
		}

		@SubscribeEvent
		public static void registerRecipeSerialziers(RegisterEvent event) {
			IForgeRegistry<RecipeSerializer<?>> registry = event.getForgeRegistry();
			if(registry != null) {
				register(registry, ICarpenterRecipe.TYPE, new CarpenterRecipe.Serializer());
				register(registry, ICentrifugeRecipe.TYPE, new CentrifugeRecipe.Serializer());
				register(registry, IFabricatorRecipe.TYPE, new FabricatorRecipe.Serializer());
				register(registry, IFabricatorSmeltingRecipe.TYPE, new FabricatorSmeltingRecipe.Serializer());
				register(registry, IFermenterRecipe.TYPE, new FermenterRecipe.Serializer());
				register(registry, IHygroregulatorRecipe.TYPE, new HygroregulatorRecipe.Serializer());
				register(registry, IMoistenerRecipe.TYPE, new MoistenerRecipe.Serializer());
				register(registry, ISqueezerRecipe.TYPE, new SqueezerRecipe.Serializer());
				register(registry, ISqueezerContainerRecipe.TYPE, new SqueezerContainerRecipe.Serializer());
				register(registry, IStillRecipe.TYPE, new StillRecipe.Serializer());
				register(registry, ISolderRecipe.TYPE, new CircuitRecipe.Serializer());
			}
		}

		private static void register(IForgeRegistry<RecipeSerializer<?>> registry, RecipeType<?> type, RecipeSerializer<?> serializer) {
			Registry.register(Registry.RECIPE_TYPE, type.toString(), type);
			registry.register(serializer.setRegistryName(new ResourceLocation(type.toString())));
		}

		@SubscribeEvent
		public static void registerLootModifiers(RegisterEvent event) {
			IForgeRegistry<IGlobalLootModifier> registry = event.getForgeRegistry();
			registry.register(ConditionLootModifier.SERIALIZER);
			registry.register(GrafterLootModifier.SERIALIZER);

			OrganismFunction.type = Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(Constants.MOD_ID, "set_species_nbt"), new LootItemFunctionType(new OrganismFunction.Serializer()));
			CountBlockFunction.type = Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(Constants.MOD_ID, "count_from_block"), new LootItemFunctionType(new CountBlockFunction.Serializer()));
		}


		@SubscribeEvent
		@OnlyIn(Dist.CLIENT)
		public void handleTextureRemap(TextureStitchEvent event) {
			if (event.getAtlas().location() == InventoryMenu.BLOCK_ATLAS) {
				TextureManagerForestry.getInstance().registerSprites(ISpriteRegistry.fromEvent(event));
				ModelBlockCached.clear();
			}
		}
	}

	@SubscribeEvent
	public void registerCommands(RegisterCommandsEvent event) {
		ModuleManager.registerCommands(event.getDispatcher());
	}

	public void processIMCMessages(InterModProcessEvent event) {
		ModuleManager.getModuleHandler().processIMCMessages(event.getIMCStream());
	}
}
