/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.arboriculture.genetics;

import forestry.api.genetics.IEffectData;
import forestry.api.genetics.alleles.IAlleleEffect;
import genetics.api.individual.IGenome;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Simple allele encapsulating a leaf effect. (Not implemented)
 */
public interface IAlleleLeafEffect extends IAlleleEffect {

	IEffectData doEffect(IGenome genome, IEffectData storedData, Level world, BlockPos pos);

}
