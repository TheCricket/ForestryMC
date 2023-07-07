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
import forestry.api.arboriculture.ITreeModifier;
import forestry.api.arboriculture.TreeManager;
import forestry.core.worldgen.FeatureHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Random;
import java.util.Set;

public abstract class FeatureTree extends FeatureArboriculture {
	private static final int minHeight = 4;
	private static final int maxHeight = 80;

	private final int baseHeight;
	private final int heightVariation;

	protected int girth;
	protected int height;

	protected FeatureTree(ITreeGenData tree, int baseHeight, int heightVariation) {
		super(tree);
		this.baseHeight = baseHeight;
		this.heightVariation = heightVariation;
	}

	@Override
	public Set<BlockPos> generateTrunk(LevelAccessor world, Random rand, TreeBlockTypeLog wood, BlockPos startPos) {
		FeatureHelper.generateTreeTrunk(world, rand, wood, startPos, height, girth, 0, 0, null, 0);
		return Collections.emptySet();
	}

	@Override
	protected void generateLeaves(LevelAccessor world, Random rand, TreeBlockTypeLeaf leaf, TreeContour contour, BlockPos startPos) {
		int leafHeight = height + 1;
		FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafHeight--, 0), girth, girth, 1, FeatureHelper.EnumReplaceMode.AIR, contour);
		FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafHeight--, 0), girth, 0.5f + girth, 1, FeatureHelper.EnumReplaceMode.AIR, contour);
		FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafHeight--, 0), girth, 1.9f + girth, 1, FeatureHelper.EnumReplaceMode.AIR, contour);
		FeatureHelper.generateCylinderFromTreeStartPos(world, leaf, startPos.offset(0, leafHeight, 0), girth, 1.9f + girth, 1, FeatureHelper.EnumReplaceMode.AIR, contour);
	}

	@Override
	protected void generateExtras(LevelAccessor world, Random rand, BlockPos startPos) {
		if (hasPods()) {
			FeatureHelper.generatePods(tree, world, rand, startPos, height, minPodHeight, girth, FeatureHelper.EnumReplaceMode.AIR);
		}
	}

	@Override
	@Nullable
	public BlockPos getValidGrowthPos(LevelAccessor world, BlockPos pos) {
		return tree.canGrow(world, pos, girth, height);
	}

	@Override
	public final void preGenerate(LevelAccessor world, Random rand, BlockPos startPos) {
		super.preGenerate(world, rand, startPos);
		height = determineHeight(world, rand, baseHeight, heightVariation);
		girth = tree.getGirth();
	}

	protected int modifyByHeight(LevelAccessor world, int val, int min, int max) {
		ITreeModifier treeModifier = TreeManager.treeRoot.getTreekeepingMode(world);
		int determined = Math.round(val * tree.getHeightModifier() * treeModifier.getHeightModifier(tree.getGenome(), 1f));
		return determined < min ? min : Math.min(determined, max);
	}

	private int determineHeight(LevelAccessor world, Random rand, int required, int variation) {
		ITreeModifier treeModifier = TreeManager.treeRoot.getTreekeepingMode(world);
		int baseHeight = required + rand.nextInt(variation);
		int height = Math.round(baseHeight * tree.getHeightModifier() * treeModifier.getHeightModifier(tree.getGenome(), 1f));
		return height < minHeight ? minHeight : Math.min(height, maxHeight);
	}
}
