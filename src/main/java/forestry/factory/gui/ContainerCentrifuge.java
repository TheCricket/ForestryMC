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
package forestry.factory.gui;

import forestry.core.gui.ContainerSocketed;
import forestry.core.gui.slots.SlotFiltered;
import forestry.core.gui.slots.SlotLocked;
import forestry.core.gui.slots.SlotOutput;
import forestry.core.network.packets.PacketItemStackDisplay;
import forestry.core.tiles.TileUtil;
import forestry.factory.features.FactoryContainers;
import forestry.factory.tiles.TileCentrifuge;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

public class ContainerCentrifuge extends ContainerSocketed<TileCentrifuge> {

	public static ContainerCentrifuge fromNetwork(int windowId, Inventory inv, FriendlyByteBuf data) {
		TileCentrifuge tile = TileUtil.getTile(inv.player.level, data.readBlockPos(), TileCentrifuge.class);
		return new ContainerCentrifuge(windowId, inv, tile);    //TODO nullability.
	}

	public ContainerCentrifuge(int windowId, Inventory player, TileCentrifuge tile) {
		super(windowId, FactoryContainers.CENTRIFUGE.containerType(), player, tile, 8, 84);

		// Resource
		this.addSlot(new SlotFiltered(this.tile, 0, 16, 37));

		// Craft Preview display
		this.addSlot(new SlotLocked(this.tile.getCraftPreviewInventory(), 0, 49, 37));

		// Product Inventory
		for (int l = 0; l < 3; l++) {
			for (int k = 0; k < 3; k++) {
				this.addSlot(new SlotOutput(this.tile, 1 + k + l * 3, 112 + k * 18, 19 + l * 18));
			}
		}
	}

	private ItemStack oldCraftPreview = ItemStack.EMPTY;

	@Override
	public void broadcastChanges() {
		super.broadcastChanges();

		Container craftPreviewInventory = tile.getCraftPreviewInventory();

		ItemStack newCraftPreview = craftPreviewInventory.getItem(0);
		if (!ItemStack.matches(oldCraftPreview, newCraftPreview)) {
			oldCraftPreview = newCraftPreview;

			PacketItemStackDisplay packet = new PacketItemStackDisplay(tile, newCraftPreview);
			sendPacketToListeners(packet);
		}
	}

}
