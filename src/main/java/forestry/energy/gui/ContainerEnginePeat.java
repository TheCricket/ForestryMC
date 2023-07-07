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

import forestry.core.gui.ContainerTile;
import forestry.core.gui.slots.SlotFiltered;
import forestry.core.gui.slots.SlotOutput;
import forestry.core.network.packets.PacketGuiUpdate;
import forestry.core.tiles.TileUtil;
import forestry.energy.features.EnergyContainers;
import forestry.energy.tiles.TileEnginePeat;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class ContainerEnginePeat extends ContainerTile<TileEnginePeat> {

	//TODO dedupe
	public static ContainerEnginePeat fromNetwork(int windowId, Inventory inv, FriendlyByteBuf extraData) {
		TileEnginePeat tile = TileUtil.getTile(inv.player.level, extraData.readBlockPos(), TileEnginePeat.class);
		return new ContainerEnginePeat(windowId, inv, tile);
	}

	public ContainerEnginePeat(int id, Inventory player, TileEnginePeat tile) {
		super(id, EnergyContainers.ENGINE_PEAT.containerType(), player, tile, 8, 84);

		this.addSlot(new SlotFiltered(tile, 0, 44, 46));

		this.addSlot(new SlotOutput(tile, 1, 98, 35));
		this.addSlot(new SlotOutput(tile, 2, 98, 53));
		this.addSlot(new SlotOutput(tile, 3, 116, 35));
		this.addSlot(new SlotOutput(tile, 4, 116, 53));
	}

	@Override
	public void broadcastChanges() {
		super.broadcastChanges();
		PacketGuiUpdate packet = new PacketGuiUpdate(tile);
		sendPacketToListeners(packet);
	}
}
