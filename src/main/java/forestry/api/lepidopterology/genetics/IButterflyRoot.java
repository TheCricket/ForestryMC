/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.lepidopterology.genetics;

import com.mojang.authlib.GameProfile;
import forestry.api.genetics.IForestrySpeciesRoot;
import forestry.api.lepidopterology.ILepidopteristTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;

public interface IButterflyRoot extends IForestrySpeciesRoot<IButterfly> {

	/* BUTTERFLY SPECIFIC */
	@Override
	ILepidopteristTracker getBreedingTracker(LevelAccessor world, @Nullable GameProfile player);

	/**
	 * Spawns the given butterfly in the world.
	 *
	 * @return butterfly entity on success, null otherwise.
	 */
	Mob spawnButterflyInWorld(Level world, IButterfly butterfly, double x, double y, double z);

	BlockPos plantCocoon(LevelAccessor world, BlockPos pos, IButterfly caterpillar, GameProfile owner, int age, boolean createNursery);

	/**
	 * @return true if passed item is mated.
	 */
	boolean isMated(ItemStack stack);

}
