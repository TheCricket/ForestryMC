package forestry.factory.recipes.jei.bottler;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import javax.annotation.Nullable;

public record BottlerRecipe(
		ItemStack inputStack,
		FluidStack fluid,
		@Nullable ItemStack outputStack,
		boolean fillRecipe) {}
