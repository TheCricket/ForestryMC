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

import forestry.core.gui.ContainerLiquidTanks;
import forestry.core.gui.slots.SlotEmptyLiquidContainerIn;
import forestry.core.gui.slots.SlotOutput;
import forestry.core.tiles.TileUtil;
import forestry.factory.features.FactoryContainers;
import forestry.factory.inventory.InventoryRaintank;
import forestry.factory.tiles.TileRaintank;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerRaintank extends ContainerLiquidTanks<TileRaintank> {

	public static ContainerRaintank fromNetwork(int windowId, Inventory inv, FriendlyByteBuf data) {
		TileRaintank tile = TileUtil.getTile(inv.player.level, data.readBlockPos(), TileRaintank.class);
		return new ContainerRaintank(windowId, inv, tile);    //TODO nullability.
	}

	public ContainerRaintank(int windowId, Inventory player, TileRaintank tile) {
		super(windowId, FactoryContainers.RAINTANK.containerType(), player, tile, 8, 84);
		addDataSlots(new SimpleContainerData(1));

		this.addSlot(new SlotEmptyLiquidContainerIn(this.tile, InventoryRaintank.SLOT_RESOURCE, 116, 19));
		this.addSlot(new SlotOutput(this.tile, InventoryRaintank.SLOT_PRODUCT, 116, 55));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void setData(int messageId, int data) {
		super.setData(messageId, data);

		tile.getGUINetworkData(messageId, data);
	}

	@Override
	public void broadcastChanges() {
		super.broadcastChanges();

		for (ContainerListener crafter : containerListeners) {
			tile.sendGUINetworkData(this, crafter);
		}
	}
}
