package forestry.lepidopterology.compat;

import forestry.core.config.Constants;
import forestry.lepidopterology.features.LepidopterologyItems;
import genetics.api.GeneticHelper;
import genetics.api.individual.IIndividual;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@JeiPlugin
@OnlyIn(Dist.CLIENT)
public class LepidopterologyJeiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(Constants.MOD_ID);
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration subtypeRegistry) {
		IIngredientSubtypeInterpreter<ItemStack> butterflySubtypeInterpreter = (itemStack, context) -> {
			Optional<IIndividual> individual = GeneticHelper.getIndividual(itemStack);
			return individual.map(iIndividual -> iIndividual.getGenome().getPrimary().getBinomial()).orElse(IIngredientSubtypeInterpreter.NONE);
		};

		subtypeRegistry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, LepidopterologyItems.BUTTERFLY_GE.item(), butterflySubtypeInterpreter);
		subtypeRegistry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, LepidopterologyItems.COCOON_GE.item(), butterflySubtypeInterpreter);
		subtypeRegistry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, LepidopterologyItems.CATERPILLAR_GE.item(), butterflySubtypeInterpreter);
		subtypeRegistry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, LepidopterologyItems.SERUM_GE.item(), butterflySubtypeInterpreter);
	}
}
