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

public class FeatureMahoe extends FeatureTree {

	public FeatureMahoe(ITreeGenData tree) {
		super(tree, 6, 3);
	}

	@Override
	public Set<BlockPos> generateTrunk(LevelAccessor world, Random rand, TreeBlockTypeLog wood, BlockPos startPos) {

		Set<BlockPos> branchCoords = new HashSet<>(FeatureHelper.generateTreeTrunk(world, rand, wood, startPos, height, girth, 0, 0, null, 0));

		for (int yBranch = 2; yBranch < height - 1; yBranch++) {
			branchCoords.addAll(FeatureHelper.generateBranches(world, rand, wood, startPos.offset(0, yBranch, 0), girth, 0.15f, 0.25f, Math.round((height - yBranch) * 0.75f), 1, 0.25f));
		}
		return branchCoords;
	}

	@Override
	protected void generateLeaves(LevelAccessor world, Random rand, TreeBlockTypeLeaf leaf, TreeContour contour, BlockPos startPos) {
		for (BlockPos branchEnd : contour.getBranchEnds()) {
			FeatureHelper.generateCylinderFromPos(world, leaf, branchEnd, 2 + girth, 2, FeatureHelper.EnumReplaceMode.AIR, contour);
		}

		int yCenter = height - girth;
		yCenter = yCenter > 3 ? yCenter : 4;
		FeatureHelper.generateSphereFromTreeStartPos(world, startPos.offset(0, yCenter, 0), girth, 3 + rand.nextInt(girth), leaf, FeatureHelper.EnumReplaceMode.AIR, contour);
	}
}
