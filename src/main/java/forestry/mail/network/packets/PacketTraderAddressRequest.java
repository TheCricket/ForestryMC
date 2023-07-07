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
package forestry.mail.network.packets;

import forestry.core.network.*;
import forestry.core.tiles.TileUtil;
import forestry.mail.tiles.TileTrader;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public class PacketTraderAddressRequest extends ForestryPacket implements IForestryPacketServer {
	private final BlockPos pos;
	private final String addressName;

	public PacketTraderAddressRequest(TileTrader tile, String addressName) {
		this.pos = tile.getBlockPos();
		this.addressName = addressName;
	}

	@Override
	public PacketIdServer getPacketId() {
		return PacketIdServer.TRADING_ADDRESS_REQUEST;
	}

	@Override
	protected void writeData(PacketBufferForestry data) {
		data.writeBlockPos(pos);
		data.writeUtf(addressName);
	}

	public static class Handler implements IForestryPacketHandlerServer {

		@Override
		public void onPacketData(PacketBufferForestry data, ServerPlayer player) {
			BlockPos pos = data.readBlockPos();
			String addressName = data.readUtf();

			TileUtil.actOnTile(player.level, pos, TileTrader.class, tile -> tile.handleSetAddressRequest(addressName));
		}
	}
}
