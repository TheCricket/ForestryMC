package forestry.api.apiculture.genetics;

import net.minecraft.world.item.ItemStack;

import java.util.function.BiConsumer;

public interface IBeeProductProvider {
	default void addProducts(BiConsumer<ItemStack, Float> registry) {
	}

	default void addSpecialties(BiConsumer<ItemStack, Float> registry) {
	}
}
