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
package forestry.arboriculture.worldgen;

import forestry.api.arboriculture.ITreeGenData;
import forestry.core.worldgen.FeatureHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class FeaturePine extends FeatureTree {

	public FeaturePine(ITreeGenData tree) {
		super(tree, 6, 4);
	}

	@Override
	public Set<BlockPos> generateTrunk(LevelAccessor world, Random rand, TreeBlockTypeLog wood, BlockPos startPos) {
		FeatureHelper.generateTreeTrunk(world, rand, wood, startPos, height, girth, 0, 0, null, 0);

		Set<BlockPos> branchEnds = new HashSet<>();
		for (int yBranch = 2; yBranch < height - 2; yBranch++) {
			branchEnds.addAll(FeatureHelper.generateBranches(world, rand, wood, startPos.offset(0, yBranch, 0), girth, 0.05f, 0.1f, Math.round((height - yBranch) * 0.25f), 1, 0.25f));
		}
		return branchEnds;
	}

	@Override
	protected void generateLeaves(LevelAccessor world, Random rand, TreeBlockTypeLeaf leaf, TreeContour contour, BlockPos startPos) {
		for (BlockPos branchEnd : contour.getBranchEnds()) {
			FeatureHelper.generateCylinderFromPos(world, leaf, branchEnd, 2 + girth, 1, FeatureHelper.EnumReplaceMode.AIR, contour);
		}

		int leafSpawn = height + 1;
		float diameterchange = 1.25f / height;
		int leafSpawned = 2;

		FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafSpawn--, 0), girth, girth, 1, FeatureHelper.EnumReplaceMode.SOFT, contour);
		FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafSpawn--, 0), girth, (float) 1 + girth, 1, FeatureHelper.EnumReplaceMode.SOFT, contour);

		while (leafSpawn > 1) {
			FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafSpawn--, 0), girth, 3 * diameterchange * leafSpawned + girth, 1, FeatureHelper.EnumReplaceMode.SOFT, contour);
			FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafSpawn--, 0), girth, 2 * diameterchange * leafSpawned + girth, 1, FeatureHelper.EnumReplaceMode.SOFT, contour);
			leafSpawned += 2;
		}
	}
}
