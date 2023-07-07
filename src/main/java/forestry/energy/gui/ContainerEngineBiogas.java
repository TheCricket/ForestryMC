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
package forestry.energy.gui;

import forestry.core.gui.ContainerLiquidTanks;
import forestry.core.gui.slots.SlotLiquidIn;
import forestry.core.network.packets.PacketGuiUpdate;
import forestry.core.tiles.TileUtil;
import forestry.energy.features.EnergyContainers;
import forestry.energy.inventory.InventoryEngineBiogas;
import forestry.energy.tiles.TileEngineBiogas;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class ContainerEngineBiogas extends ContainerLiquidTanks<TileEngineBiogas> {

	//TODO dedupe
	public static ContainerEngineBiogas fromNetwork(int windowId, Inventory inv, FriendlyByteBuf extraData) {
		TileEngineBiogas tile = TileUtil.getTile(inv.player.level, extraData.readBlockPos(), TileEngineBiogas.class);
		return new ContainerEngineBiogas(windowId, inv, tile);
	}

	public ContainerEngineBiogas(int windowId, Inventory player, TileEngineBiogas engine) {
		super(windowId, EnergyContainers.ENGINE_BIOGAS.containerType(), player, engine, 8, 84);

		this.addSlot(new SlotLiquidIn(engine, InventoryEngineBiogas.SLOT_CAN, 143, 40));
	}

	@Override
	public void broadcastChanges() {
		super.broadcastChanges();
		PacketGuiUpdate packet = new PacketGuiUpdate(tile);
		sendPacketToListeners(packet);
	}
}
