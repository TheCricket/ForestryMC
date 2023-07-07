package forestry.api.genetics.gatgets;

import forestry.core.gui.elements.GuiElement;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface IGeneticAnalyzer {
	IGeneticAnalyzerProvider getProvider();

	/**
	 * @return True if the analyzer is currently visible.
	 */
	boolean isVisible();

	void setVisible(boolean visible);

	/**
	 * Called at the end of the constructor of the analyzer provider.
	 */
	void init();

	/**
	 * Updates the displayed content of the analyzer.
	 */
	void update();

	/**
	 * @return
	 */
	GuiElement getItemElement();

	void updateSelected();

	void setSelectedSlot(int selectedSlot);

	int getSelected();
}
