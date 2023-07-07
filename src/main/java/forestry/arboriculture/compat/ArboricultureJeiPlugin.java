package forestry.arboriculture.compat;

import forestry.arboriculture.features.ArboricultureItems;
import forestry.core.config.Constants;
import forestry.core.utils.JeiUtil;
import genetics.api.GeneticHelper;
import genetics.api.individual.IIndividual;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Optional;

@JeiPlugin
@OnlyIn(Dist.CLIENT)
public class ArboricultureJeiPlugin implements IModPlugin {
	@Override
	public ResourceLocation getPluginUid() {
		return new ResourceLocation(Constants.MOD_ID);
	}

	@Override
	public void registerItemSubtypes(ISubtypeRegistration subtypeRegistry) {
		IIngredientSubtypeInterpreter<ItemStack> arboSubtypeInterpreter = (itemStack, context) -> {
			Optional<IIndividual> individual = GeneticHelper.getIndividual(itemStack);
			return individual.map(iIndividual -> iIndividual.getGenome().getPrimary().getBinomial()).orElse(IIngredientSubtypeInterpreter.NONE);
		};

		subtypeRegistry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ArboricultureItems.SAPLING.item(), arboSubtypeInterpreter);
		subtypeRegistry.registerSubtypeInterpreter(VanillaTypes.ITEM_STACK, ArboricultureItems.POLLEN_FERTILE.item(), arboSubtypeInterpreter);
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		JeiUtil.addDescription(registration, ArboricultureItems.GRAFTER.item(), ArboricultureItems.GRAFTER_PROVEN.item());
	}
}
