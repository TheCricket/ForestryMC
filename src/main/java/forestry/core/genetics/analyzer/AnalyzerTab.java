package forestry.core.genetics.analyzer;

import forestry.api.genetics.gatgets.IDatabaseTab;
import forestry.core.features.CoreItems;
import forestry.core.gui.elements.DatabaseElement;
import genetics.api.individual.IIndividual;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Locale;

public enum AnalyzerTab implements IDatabaseTab<IIndividual> {
	ANALYZE {
		@Override
		public void createElements(DatabaseElement container, IIndividual individual, ItemStack itemStack) {

		}

		@Override
		public ItemStack getIconStack() {
			return CoreItems.PORTABLE_ALYZER.stack();
		}
	};

	@Override
	public Component getTooltip(IIndividual individual) {
		return Component.translatable("for.gui.database.tab." + name().toLowerCase(Locale.ENGLISH) + ".name");
	}
}
