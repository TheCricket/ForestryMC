package forestry.core.gui;

import net.minecraft.world.inventory.Slot;

import javax.annotation.Nullable;

public interface IContainerAnalyzerProvider extends IGuiSelectable {
	@Nullable
	Slot getAnalyzerSlot();
}
