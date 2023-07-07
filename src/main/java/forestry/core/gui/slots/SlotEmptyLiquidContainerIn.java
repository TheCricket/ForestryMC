package forestry.core.gui.slots;

import forestry.core.tiles.IFilterSlotDelegate;
import net.minecraft.world.Container;

public class SlotEmptyLiquidContainerIn extends SlotFiltered {
	public <T extends Container & IFilterSlotDelegate> SlotEmptyLiquidContainerIn(T inventory, int slotIndex, int xPos, int yPos) {
		super(inventory, slotIndex, xPos, yPos);
		setBackgroundTexture("slots/container");
	}
}
