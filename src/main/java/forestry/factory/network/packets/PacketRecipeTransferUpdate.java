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
package forestry.factory.network.packets;

import forestry.core.network.*;
import forestry.core.tiles.TileBase;
import forestry.core.tiles.TileUtil;
import forestry.factory.tiles.TileCarpenter;
import forestry.factory.tiles.TileFabricator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

public class PacketRecipeTransferUpdate extends ForestryPacket implements IForestryPacketClient {
	private final BlockPos pos;
	private final NonNullList<ItemStack> craftingInventory;

	public PacketRecipeTransferUpdate(TileBase base, NonNullList<ItemStack> craftingInventory) {
		this.pos = base.getBlockPos();
		this.craftingInventory = craftingInventory;
	}

	@Override
	protected void writeData(PacketBufferForestry data) {
		data.writeBlockPos(pos);
		data.writeItemStacks(craftingInventory);
	}

	@Override
	public PacketIdClient getPacketId() {
		return PacketIdClient.RECIPE_TRANSFER_UPDATE;
	}

	@OnlyIn(Dist.CLIENT)
	public static class Handler implements IForestryPacketHandlerClient {
		@Override
		public void onPacketData(PacketBufferForestry data, Player player) throws IOException {
			BlockPos pos = data.readBlockPos();
			NonNullList<ItemStack> craftingInventory = data.readItemStacks();

			BlockEntity tile = TileUtil.getTile(player.level, pos);
			if (tile instanceof TileCarpenter carpenter) {
				int index = 0;
				for (ItemStack stack : craftingInventory) {
					carpenter.getCraftingInventory().setItem(index, stack);
					index++;
				}
			} else if (tile instanceof TileFabricator fabricator) {
				int index = 0;
				for (ItemStack stack : craftingInventory) {
					fabricator.getCraftingInventory().setItem(index, stack);
					index++;
				}
			}
		}
	}
}
