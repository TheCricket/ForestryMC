package forestry.core.genetics.analyzer;

import forestry.api.genetics.gatgets.IDatabaseTab;
import genetics.api.individual.IIndividual;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

//TODO: Rework the database and the analyser
public abstract class DatabaseTab<I extends IIndividual> implements IDatabaseTab<I> {
	private final Supplier<ItemStack> stackSupplier;
	private final String name;

	public DatabaseTab(String name, Supplier<ItemStack> stackSupplier) {
		this.name = name;
		this.stackSupplier = stackSupplier;
	}

	@Override
	public ItemStack getIconStack() {
		return stackSupplier.get();
	}

	@Override
	public Component getTooltip(IIndividual individual) {
		return new TranslatableComponent("for.gui.database.tab." + name + ".name");
	}
}
