package forestry.sorting.gui;

import forestry.core.config.Constants;
import forestry.core.gui.slots.ISlotTextured;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SlotFilterFacing extends Slot implements ISlotTextured {

	public SlotFilterFacing(Container inventoryIn, int index, int xPosition, int yPosition) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public boolean mayPlace(ItemStack stack) {
		return false;
	}

	@Override
	public ResourceLocation getBackgroundTexture() {
		return new ResourceLocation(Constants.MOD_ID, "slots/bee");
	}
}
