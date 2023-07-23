package forestry.core.features;

import forestry.core.ModuleCore;
import forestry.core.blocks.*;
import forestry.core.items.ItemBlockBase;
import forestry.core.items.ItemBlockForestry;
import forestry.modules.features.*;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

@FeatureProvider
public class CoreBlocks {
	private static final IFeatureRegistry REGISTRY = ModFeatureRegistry.get(ModuleCore.class);

	public static final FeatureBlockGroup<BlockCore, BlockTypeCoreTesr> BASE = REGISTRY.blockGroup(BlockCore::new, BlockTypeCoreTesr.VALUES).itemWithType(ItemBlockBase::new).create();
	public static final FeatureBlock<BlockBogEarth, ItemBlockForestry> BOG_EARTH = REGISTRY.block(BlockBogEarth::new, ItemBlockForestry::new, "bog_earth");
	public static final FeatureBlock<Block, ItemBlockForestry> PEAT = REGISTRY.block(() -> new Block(Block.Properties.of(Material.DIRT)
			.strength(0.5f)
			.sound(SoundType.GRAVEL)), "peat");
	public static final FeatureBlock<BlockHumus, ItemBlockForestry> HUMUS = REGISTRY.block(BlockHumus::new, ItemBlockForestry::new, "humus");
	public static final FeatureBlockGroup<BlockResourceStorage, EnumResourceType> RESOURCE_STORAGE = REGISTRY.blockGroup(BlockResourceStorage::new, EnumResourceType.VALUES).item(ItemBlockForestry::new).identifier("resource_storage").create();
	public static final FeatureBlock<OreBlock, BlockItem> APATITE_ORE = REGISTRY.block(() -> new OreBlock(BlockBehaviour.Properties.copy(Blocks.COAL_ORE), UniformInt.of(0, 4)), ItemBlockForestry::new, "apatite_ore");
	public static final FeatureBlock<OreBlock, BlockItem> DEEPSLATE_APATITE_ORE = REGISTRY.block(() -> new OreBlock(BlockBehaviour.Properties.copy(APATITE_ORE.block()), UniformInt.of(0, 4)), ItemBlockForestry::new, "deepslate_apatite_ore");
	public static final FeatureBlock<OreBlock, BlockItem> TIN_ORE = REGISTRY.block(() -> new OreBlock(BlockBehaviour.Properties.copy(Blocks.COPPER_ORE)), ItemBlockForestry::new, "tin_ore");
	public static final FeatureBlock<OreBlock, BlockItem> DEEPSLATE_TIN_ORE = REGISTRY.block(() -> new OreBlock(BlockBehaviour.Properties.copy(TIN_ORE.block())), ItemBlockForestry::new, "deepslate_tin_ore");
	public static final FeatureBlock<OreBlock, BlockItem> RAW_TIN_BLOCK = REGISTRY.block(() -> new OreBlock(BlockBehaviour.Properties.copy(Blocks.RAW_COPPER_BLOCK)), ItemBlockForestry::new, "raw_tin_block");

	private CoreBlocks() {
	}
}
