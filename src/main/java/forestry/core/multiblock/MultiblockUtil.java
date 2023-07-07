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
package forestry.core.multiblock;

import forestry.api.multiblock.IMultiblockComponent;
import forestry.api.multiblock.IMultiblockController;
import forestry.api.multiblock.IMultiblockLogic;
import forestry.core.tiles.TileUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkSource;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class MultiblockUtil {
	/**
	 * Returns an array containing references to neighboring IMultiblockComponent tile entities.
	 * Primarily a utility method. Only works after tileentity construction.
	 * <p>
	 * This method is chunk-safe on the server; it will not query for parts in chunks that are unloaded.
	 * Note that no method is chunk-safe on the client, because ChunkProviderClient is stupid.
	 *
	 * @return An array of references to neighboring IMultiblockComponent tile entities.
	 */
	public static List<IMultiblockComponent> getNeighboringParts(Level world, IMultiblockComponent part) {
		BlockPos partCoord = part.getCoordinates();

		List<BlockPos> neighbors = new ArrayList<>(Direction.values().length);
		for (Direction facing : Direction.values()) {
			BlockPos neighborCoord = new BlockPos(partCoord);
			neighborCoord = neighborCoord.relative(facing);
			neighbors.add(neighborCoord);
		}

		List<IMultiblockComponent> neighborParts = new ArrayList<>();
		ChunkSource chunkProvider = world.getChunkSource();
		for (BlockPos neighbor : neighbors) {
			if (!chunkProvider.hasChunk(neighbor.getX() >> 4, neighbor.getZ() >> 4)) {
				// Chunk not loaded, skip it.
				continue;
			}

			TileUtil.actOnTile(world, neighbor, IMultiblockComponent.class, neighborParts::add);
		}
		return neighborParts;
	}

	@Nullable
	public static <C extends IMultiblockComponent> C getComponent(BlockGetter world, BlockPos pos, Class<C> componentClass) {
		return TileUtil.getTile(world, pos, componentClass);
	}

	@Nullable
	public static <C extends IMultiblockComponent, L extends IMultiblockLogic> L getLogic(BlockGetter world, BlockPos pos, Class<C> componentClass) {
		C component = getComponent(world, pos, componentClass);
		if (component == null) {
			return null;
		}
		return (L) component.getMultiblockLogic();
	}

	@Nullable
	public static <C extends IMultiblockComponent, L extends IMultiblockLogic, M extends IMultiblockController> M getController(BlockGetter world, BlockPos pos, Class<C> componentClass) {
		L logic = getLogic(world, pos, componentClass);
		if (logic == null || !logic.isConnected()) {
			return null;
		}
		return (M) logic.getController();
	}

}
