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
package forestry.lepidopterology.entities;

import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * @author CovertJaguar <http://www.railcraft.info/>
 */
public abstract class AIButterflyMovement extends AIButterflyBase {
	@Nullable
	protected Vec3 flightTarget;

	protected AIButterflyMovement(EntityButterfly entity) {
		super(entity);
	}

	@Override
	public boolean canContinueToUse() {
		if (entity.getState() != EnumButterflyState.FLYING) {
			return false;
		}
		if (flightTarget == null) {
			return false;
		}
		// Abort if the flight target changed on us.
		if (entity.getDestination() == null || !entity.getDestination().equals(flightTarget)) {
			return false;
		}

		// Continue if we have not yet reached the destination.
		if (entity.getDestination().distanceToSqr(entity.position()) > 2.0f) {
			return true;
		}

		entity.setDestination(null);
		return false;
	}

	@Override
	public void tick() {
		// Reset destination if we did collide.
		if (entity.isInWater()) {
			flightTarget = getRandomDestinationUpwards();
		} else if (false) {//TODO: entity.collided
			flightTarget = entity.getRandom().nextBoolean() ? getRandomDestination() : null;
		} else if (entity.level.random.nextInt(300) == 0) {
			flightTarget = getRandomDestination();
		}
		entity.setDestination(flightTarget);
		entity.changeExhaustion(1);
	}

	@Override
	public void start() {
	}

	@Override
	public void stop() {
		flightTarget = null;
	}
}
