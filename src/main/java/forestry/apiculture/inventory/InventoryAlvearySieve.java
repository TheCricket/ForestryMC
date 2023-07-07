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

import forestry.apiculture.multiblock.TileAlvearySieve;
import forestry.core.features.CoreItems;
import forestry.core.inventory.InventoryAdapterTile;
import forestry.core.inventory.watchers.ISlotPickupWatcher;
import forestry.core.items.definitions.EnumCraftingMaterial;
import forestry.core.utils.ItemStackUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class InventoryAlvearySieve extends InventoryAdapterTile<TileAlvearySieve> implements ISlotPickupWatcher {
	public static final int SLOT_POLLEN_1 = 0;
	public static final int SLOTS_POLLEN_COUNT = 4;
	public static final int SLOT_SIEVE = 4;

	public InventoryAlvearySieve(TileAlvearySieve alvearySieve) {
		super(alvearySieve, 5, "Items", 1);
	}

	@Override
	public boolean canSlotAccept(int slotIndex, ItemStack itemStack) {
		return ItemStackUtil.isIdenticalItem(CoreItems.CRAFTING_MATERIALS.stack(EnumCraftingMaterial.WOVEN_SILK, 1), itemStack);
	}

	public boolean canStorePollen() {
		if (getItem(SLOT_SIEVE).isEmpty()) {
			return false;
		}

		for (int i = SLOT_POLLEN_1; i < SLOT_POLLEN_1 + SLOTS_POLLEN_COUNT; i++) {
			if (getItem(i).isEmpty()) {
				return true;
			}
		}

		return false;
	}

	public void storePollenStack(ItemStack itemstack) {
		for (int i = SLOT_POLLEN_1; i < SLOT_POLLEN_1 + SLOTS_POLLEN_COUNT; i++) {
			if (getItem(i).isEmpty()) {
				setItem(i, itemstack);
				return;
			}
		}
	}

	/* ISlotPickupWatcher */
	@Override
	public void onTake(int slotIndex, Player player) {
		if (slotIndex == SLOT_SIEVE) {
			for (int i = SLOT_POLLEN_1; i < SLOT_POLLEN_1 + SLOTS_POLLEN_COUNT; i++) {
				setItem(i, ItemStack.EMPTY);
			}
		} else {
			setItem(SLOT_SIEVE, ItemStack.EMPTY);
		}
	}
}
