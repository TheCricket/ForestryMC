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
package forestry.core.genetics.mutations;

import forestry.api.climate.IClimateProvider;
import forestry.api.core.EnumHumidity;
import forestry.api.genetics.IMutationCondition;
import forestry.api.genetics.alleles.AlleleManager;
import forestry.core.utils.Translator;
import genetics.api.alleles.IAllele;
import genetics.api.individual.IGenome;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

public class MutationConditionHumidity implements IMutationCondition {
	private final EnumHumidity minHumidity;
	private final EnumHumidity maxHumidity;

	public MutationConditionHumidity(EnumHumidity minHumidity, EnumHumidity maxHumidity) {
		this.minHumidity = minHumidity;
		this.maxHumidity = maxHumidity;
	}

	@Override
	public float getChance(Level world, BlockPos pos, IAllele allele0, IAllele allele1, IGenome genome0, IGenome genome1, IClimateProvider climate) {
		EnumHumidity biomeHumidity = climate.getHumidity();

		if (biomeHumidity.ordinal() < minHumidity.ordinal() || biomeHumidity.ordinal() > maxHumidity.ordinal()) {
			return 0;
		}
		return 1;
	}

	//TODO textcomponent (this will probably crash atm)
	@Override
	public Component getDescription() {
		String minHumidityString = AlleleManager.climateHelper.toDisplay(minHumidity).getString();

		if (minHumidity != maxHumidity) {
			String maxHumidityString = AlleleManager.climateHelper.toDisplay(maxHumidity).getString();
			//TODO: REPLACE
			return Component.literal(Translator.translateToLocal("for.mutation.condition.humidity.range").replace("%LOW", minHumidityString).replace("%HIGH", maxHumidityString));
		} else {
			return Component.translatable("for.mutation.condition.humidity.single", minHumidityString);
		}
	}
}
