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
package forestry.arboriculture.tiles;

import forestry.api.arboriculture.TreeManager;
import forestry.arboriculture.features.ArboricultureTiles;
import forestry.core.tiles.TileNaturalistChest;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileArboristChest extends TileNaturalistChest {
	public TileArboristChest(BlockPos pos, BlockState state) {
		super(ArboricultureTiles.ARBORIST_CHEST.tileType(), pos, state, TreeManager.treeRoot);
	}
}
