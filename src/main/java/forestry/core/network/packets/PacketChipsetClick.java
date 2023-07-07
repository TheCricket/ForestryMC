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
package forestry.core.network.packets;

import forestry.core.circuits.ItemCircuitBoard;
import forestry.core.gui.IContainerSocketed;
import forestry.core.network.*;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class PacketChipsetClick extends ForestryPacket implements IForestryPacketServer {
	private final int slot;

	public PacketChipsetClick(int slot) {
		this.slot = slot;
	}

	@Override
	protected void writeData(PacketBufferForestry data) {
		data.writeVarInt(slot);
	}

	@Override
	public PacketIdServer getPacketId() {
		return PacketIdServer.CHIPSET_CLICK;
	}

	public static class Handler implements IForestryPacketHandlerServer {
		@Override
		public void onPacketData(PacketBufferForestry data, ServerPlayer player) {
			int slot = data.readVarInt();

			if (player.containerMenu instanceof IContainerSocketed) {
				ItemStack itemstack = player.inventoryMenu.getCarried();
				if (itemstack.getItem() instanceof ItemCircuitBoard) {
					((IContainerSocketed) player.containerMenu).handleChipsetClickServer(slot, player, itemstack);
				}
			}
		}
	}
}
