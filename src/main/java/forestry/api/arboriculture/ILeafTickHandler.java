/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.arboriculture;

import forestry.api.arboriculture.genetics.ITree;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Random;

public interface ILeafTickHandler {
	boolean onRandomLeafTick(ITree tree, Level world, Random rand, BlockPos pos, boolean isDestroyed);
}
