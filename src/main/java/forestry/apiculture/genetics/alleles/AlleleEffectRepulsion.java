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
package forestry.apiculture.genetics.alleles;

import forestry.api.apiculture.IBeeHousing;
import forestry.api.genetics.IEffectData;
import forestry.apiculture.entities.AIAvoidPlayers;
import genetics.api.individual.IGenome;
import net.minecraft.world.entity.monster.Monster;

import java.util.List;

public class AlleleEffectRepulsion extends AlleleEffectThrottled {

	public AlleleEffectRepulsion() {
		super("repulsion", false, 100, true, true);
	}

	@Override
	public IEffectData doEffectThrottled(IGenome genome, IEffectData storedData, IBeeHousing housing) {
		List<Monster> mobs = getEntitiesInRange(genome, housing, Monster.class);
		for (Monster mob : mobs) {
			if (!isMobAvoidingPlayers(mob)) {
				mob.goalSelector.addGoal(3, new AIAvoidPlayers(mob, 6.0f, 0.25f, 0.3f));
				mob.goalSelector.tick();    //TODO - I think
			}
		}

		return storedData;
	}

	private boolean isMobAvoidingPlayers(Monster mob) {
		return mob.goalSelector.getRunningGoals().anyMatch(task -> task.getGoal() instanceof AIAvoidPlayers);
	}
}
