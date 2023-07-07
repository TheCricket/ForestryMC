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

import forestry.core.network.*;
import forestry.core.tiles.IItemStackDisplay;
import forestry.core.tiles.TileForestry;
import forestry.core.tiles.TileUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

public class PacketItemStackDisplay extends ForestryPacket implements IForestryPacketClient {
	private final BlockPos pos;
	private final ItemStack itemStack;

	public <T extends TileForestry & IItemStackDisplay> PacketItemStackDisplay(T tile, ItemStack itemStack) {
		this.pos = tile.getBlockPos();
		this.itemStack = itemStack;
	}

	@Override
	protected void writeData(PacketBufferForestry data) {
		data.writeBlockPos(pos);
		data.writeItem(itemStack);
	}

	@Override
	public PacketIdClient getPacketId() {
		return PacketIdClient.ITEMSTACK_DISPLAY;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Handler implements IForestryPacketHandlerClient {
		@Override
		public void onPacketData(PacketBufferForestry data, Player player) throws IOException {
			BlockPos pos = data.readBlockPos();
			ItemStack itemStack = data.readItem();

			TileUtil.actOnTile(player.level, pos, IItemStackDisplay.class, tile -> tile.handleItemStackForDisplay(itemStack));
		}
	}
}
