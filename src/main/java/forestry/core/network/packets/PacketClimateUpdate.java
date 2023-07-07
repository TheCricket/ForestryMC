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

import forestry.api.climate.IClimateHousing;
import forestry.api.climate.IClimateTransformer;
import forestry.core.climate.ClimateTransformer;
import forestry.core.network.*;
import forestry.core.tiles.TileUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

import java.io.IOException;

public class PacketClimateUpdate extends ForestryPacket implements IForestryPacketClient {

	private BlockPos pos;
	private ClimateTransformer container;

	public PacketClimateUpdate(BlockPos pos, ClimateTransformer container) {
		this.pos = pos;
		this.container = container;
	}

	@Override
	public PacketIdClient getPacketId() {
		return PacketIdClient.UPDATE_CLIMATE;
	}

	@Override
	protected void writeData(PacketBufferForestry data) {
		data.writeBlockPos(pos);
		container.writeData(data);
	}

	public static class Handler implements IForestryPacketHandlerClient {
		@Override
		public void onPacketData(PacketBufferForestry data, Player player) throws IOException {
			BlockPos position = data.readBlockPos();
			IClimateHousing housing = TileUtil.getTile(player.level, position, IClimateHousing.class);
			if (housing == null) {
				return;
			}
			IClimateTransformer transformer = housing.getTransformer();
			if (transformer instanceof IStreamable streamable) {
				streamable.readData(data);
			}
			//housing.onUpdateClimate();
		}
	}
}
