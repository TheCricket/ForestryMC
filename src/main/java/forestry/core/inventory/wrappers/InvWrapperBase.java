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
package forestry.core.inventory.wrappers;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Created by CovertJaguar on 3/6/2016 for Railcraft.
 */
public abstract class InvWrapperBase implements Container {

	private final Container inv;
	private boolean checkItems = true;

	public InvWrapperBase(Container inv) {
		this(inv, true);
	}

	public InvWrapperBase(Container inv, boolean checkItems) {
		this.inv = inv;
		this.checkItems = checkItems;
	}

	public Container getBaseInventory() {
		return inv;
	}

	@Override
	public int getContainerSize() {
		return inv.getContainerSize();
	}

	@Override
	public ItemStack getItem(int slot) {
		return inv.getItem(slot);
	}

	@Override
	public ItemStack removeItem(int slot, int amount) {
		return inv.removeItem(slot, amount);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return inv.removeItemNoUpdate(slot);
	}

	@Override
	public void setItem(int slot, ItemStack itemstack) {
		inv.setItem(slot, itemstack);
	}

	@Override
	public int getMaxStackSize() {
		return inv.getMaxStackSize();
	}

	@Override
	public void setChanged() {
		inv.setChanged();
	}

	@Override
	public boolean stillValid(Player PlayerEntity) {
		return inv.stillValid(PlayerEntity);
	}

	@Override
	public void startOpen(Player player) {
		inv.startOpen(player);
	}

	@Override
	public void stopOpen(Player player) {
		inv.stopOpen(player);
	}

	@Override
	public boolean canPlaceItem(int slot, ItemStack stack) {
		return !checkItems || inv.canPlaceItem(slot, stack);
	}

	@Override
	public void clearContent() {
		inv.clearContent();
	}

	public boolean checkItems() {
		return checkItems;
	}
}

