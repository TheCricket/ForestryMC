package genetics.api.root.translator;

import genetics.api.individual.IIndividual;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * Translates blockStates into genetic data.
 * Used by bees and butterflies to convert and pollinate foreign leaf blocks.
 */
public interface IBlockTranslator<I extends IIndividual> {
	@Nullable
	I getIndividualFromObject(BlockState blockState);

	default ItemStack getGeneticEquivalent(BlockState blockState) {
		return ItemStack.EMPTY;
	}
}
