/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.apiculture.inventory;

import forestry.api.apiculture.BeeManager;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeekeepingMode;
import forestry.api.apiculture.genetics.IBee;
import forestry.api.apiculture.hives.IHiveFrame;
import forestry.apiculture.InventoryBeeHousing;
import forestry.core.utils.SlotUtil;
import net.minecraft.util.Tuple;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class InventoryApiary extends InventoryBeeHousing implements IApiaryInventory {
	public static final int SLOT_FRAMES_1 = 9;
	public static final int SLOT_FRAMES_COUNT = 3;

	public InventoryApiary() {
		super(12);
	}

	@Override
	public boolean canSlotAccept(int slotIndex, ItemStack itemStack) {
		if (SlotUtil.isSlotInRange(slotIndex, SLOT_FRAMES_1, SLOT_FRAMES_COUNT)) {
			return itemStack.getItem() instanceof IHiveFrame && getItem(slotIndex).isEmpty();
		}

		return super.canSlotAccept(slotIndex, itemStack);
	}

	// override for pipe automation
	@Override
	public boolean canPlaceItem(int slotIndex, ItemStack itemStack) {
		return !SlotUtil.isSlotInRange(slotIndex, SLOT_FRAMES_1, SLOT_FRAMES_COUNT) &&
				super.canPlaceItem(slotIndex, itemStack);
	}

	public Collection<Tuple<IHiveFrame, ItemStack>> getFrames() {
		Collection<Tuple<IHiveFrame, ItemStack>> hiveFrames = new ArrayList<>(SLOT_FRAMES_COUNT);

		for (int i = SLOT_FRAMES_1; i < SLOT_FRAMES_1 + SLOT_FRAMES_COUNT; i++) {
			ItemStack stackInSlot = getItem(i);
			Item itemInSlot = stackInSlot.getItem();
			if (itemInSlot instanceof IHiveFrame frame) {
				hiveFrames.add(new Tuple<>(frame, stackInSlot.copy()));
			}
		}

		return hiveFrames;
	}

	@Override
	public void wearOutFrames(IBeeHousing beeHousing, int amount) {
		IBeekeepingMode beekeepingMode = BeeManager.beeRoot.getBeekeepingMode(beeHousing.getWorldObj());
		int wear = Math.round(amount * beekeepingMode.getWearModifier());

		for (int i = SLOT_FRAMES_1; i < SLOT_FRAMES_1 + SLOT_FRAMES_COUNT; i++) {
			ItemStack hiveFrameStack = getItem(i);
			Item hiveFrameItem = hiveFrameStack.getItem();
			if ((hiveFrameItem instanceof IHiveFrame hiveFrame)) {

				ItemStack queenStack = getQueen();
				Optional<IBee> optionalBee = BeeManager.beeRoot.create(queenStack);
				if (optionalBee.isPresent()) {
					IBee queen = optionalBee.get();
					ItemStack usedFrame = hiveFrame.frameUsed(beeHousing, hiveFrameStack, queen, wear);

					setItem(i, usedFrame);
				}
			}
		}
	}
}
