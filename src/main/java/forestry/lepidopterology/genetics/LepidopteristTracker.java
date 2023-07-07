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
package forestry.lepidopterology.genetics;

import forestry.api.genetics.IBreedingTracker;
import forestry.api.lepidopterology.ButterflyManager;
import forestry.api.lepidopterology.ILepidopteristTracker;
import forestry.api.lepidopterology.genetics.IButterfly;
import forestry.core.genetics.BreedingTracker;
import genetics.api.individual.IIndividual;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;

public class LepidopteristTracker extends BreedingTracker implements ILepidopteristTracker {

	/**
	 * Required for creation from map storage
	 */
	public LepidopteristTracker() {
		super("NORMAL");
	}

	public LepidopteristTracker(CompoundTag tag) {
		super("NORMAL", tag);
	}

	@Override
	protected IBreedingTracker getBreedingTracker(Player player) {
		//TODO world cast
		return ButterflyManager.butterflyRoot.getBreedingTracker(player.level, player.getGameProfile());
	}

	@Override
	protected String speciesRootUID() {
		return ButterflyRoot.UID;
	}

	@Override
	public void registerPickup(IIndividual individual) {
	}

	@Override
	public void registerCatch(IButterfly butterfly) {
		registerSpecies(butterfly.getGenome().getPrimary());
		registerSpecies(butterfly.getGenome().getSecondary());
	}

}
