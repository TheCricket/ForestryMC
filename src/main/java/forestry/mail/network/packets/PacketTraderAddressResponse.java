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
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PacketTraderAddressResponse extends ForestryPacket implements IForestryPacketClient {
	private final BlockPos pos;
	private final String addressName;

	public PacketTraderAddressResponse(TileTrader tile, String addressName) {
		this.pos = tile.getBlockPos();
		this.addressName = addressName;
	}

	@Override
	public PacketIdClient getPacketId() {
		return PacketIdClient.TRADING_ADDRESS_RESPONSE;
	}

	@Override
	protected void writeData(PacketBufferForestry data) {
		data.writeBlockPos(pos);
		data.writeUtf(addressName);
	}

	@OnlyIn(Dist.CLIENT)
	public static class Handler implements IForestryPacketHandlerClient {

		@Override
		public void onPacketData(PacketBufferForestry data, Player player) {
			BlockPos pos = data.readBlockPos();
			String addressName = data.readUtf();

			TileUtil.actOnTile(player.level, pos, TileTrader.class, tile -> tile.handleSetAddressResponse(addressName));
		}
	}
}
