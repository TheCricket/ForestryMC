/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.arboriculture;

import com.mojang.authlib.GameProfile;
import genetics.api.individual.IGenome;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;
import java.util.Random;

public interface ITreeGenData {

	int getGirth();

	float getHeightModifier();

	/**
	 * @return Position that this tree can grow. May be different from pos if there are multiple saplings.
	 * Returns null if a sapling at the given position can not grow into a tree.
	 */
	@Nullable
	BlockPos canGrow(LevelAccessor world, BlockPos pos, int expectedGirth, int expectedHeight);

	boolean setLeaves(LevelAccessor world, @Nullable GameProfile owner, BlockPos pos, Random random);

	boolean setLogBlock(LevelAccessor world, BlockPos pos, Direction facing);

	boolean allowsFruitBlocks();

	boolean trySpawnFruitBlock(LevelAccessor world, Random rand, BlockPos pos);

	IGenome getGenome();
}
