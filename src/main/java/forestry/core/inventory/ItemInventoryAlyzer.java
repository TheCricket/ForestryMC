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
package forestry.core.inventory;

import com.google.common.collect.ImmutableSet;
import forestry.api.core.IErrorSource;
import forestry.api.core.IErrorState;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IForestrySpeciesRoot;
import forestry.apiculture.features.ApicultureItems;
import forestry.core.errors.EnumErrorCode;
import forestry.core.utils.GeneticsUtil;
import genetics.api.GeneticHelper;
import genetics.api.individual.IIndividual;
import genetics.api.root.IRootDefinition;
import genetics.utils.RootUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class ItemInventoryAlyzer extends ItemInventory implements IErrorSource {
	public static final int SLOT_ENERGY = 0;
	public static final int SLOT_SPECIMEN = 1;
	public static final int SLOT_ANALYZE_1 = 2;
	public static final int SLOT_ANALYZE_2 = 3;
	public static final int SLOT_ANALYZE_3 = 4;
	public static final int SLOT_ANALYZE_4 = 5;
	public static final int SLOT_ANALYZE_5 = 6;

	public ItemInventoryAlyzer(Player player, ItemStack itemstack) {
		super(player, 7, itemstack);
	}

	public static boolean isAlyzingFuel(ItemStack itemstack) {
		if (itemstack.isEmpty()) {
			return false;
		}

		return ApicultureItems.HONEY_DROPS.itemEqual(itemstack) || ApicultureItems.HONEYDEW.itemEqual(itemstack);

	}

	@Override
	public boolean canSlotAccept(int slotIndex, ItemStack itemStack) {
		if (slotIndex == SLOT_ENERGY) {
			return isAlyzingFuel(itemStack);
		}

		// only allow one slot to be used at a time
		if (hasSpecimen() && getItem(slotIndex).isEmpty()) {
			return false;
		}

		itemStack = GeneticsUtil.convertToGeneticEquivalent(itemStack);
		IRootDefinition<IForestrySpeciesRoot<IIndividual>> definition = RootUtils.getRoot(itemStack);
		if (!definition.isPresent()) {
			return false;
		}
		IForestrySpeciesRoot<IIndividual> speciesRoot = definition.get();

		if (slotIndex == SLOT_SPECIMEN) {
			return true;
		}

		Optional<IIndividual> optionalIndividual = speciesRoot.create(itemStack);
		return optionalIndividual.filter(IIndividual::isAnalyzed).isPresent();
	}

	@Override
	public void setItem(int index, ItemStack itemStack) {
		super.setItem(index, itemStack);
		if (index == SLOT_SPECIMEN) {
			analyzeSpecimen(itemStack);
		}
	}

	private void analyzeSpecimen(ItemStack specimen) {
		if (specimen.isEmpty()) {
			return;
		}

		ItemStack convertedSpecimen = GeneticsUtil.convertToGeneticEquivalent(specimen);
		if (!ItemStack.matches(specimen, convertedSpecimen)) {
			setItem(SLOT_SPECIMEN, convertedSpecimen);
			specimen = convertedSpecimen;
		}

		IRootDefinition<IForestrySpeciesRoot<IIndividual>> definition = RootUtils.getRoot(specimen);
		// No individual, abort
		if (!definition.isPresent()) {
			return;
		}
		IForestrySpeciesRoot<IIndividual> speciesRoot = definition.get();

		Optional<IIndividual> optionalIndividual = speciesRoot.create(specimen);

		// Analyze if necessary
		if (optionalIndividual.isPresent()) {
			IIndividual individual = optionalIndividual.get();
			if (!individual.isAnalyzed()) {
				final boolean requiresEnergy = true;
				// Requires energy
				if (!isAlyzingFuel(getItem(SLOT_ENERGY))) {
					return;
				}

				if (individual.analyze()) {
					IBreedingTracker breedingTracker = speciesRoot.getBreedingTracker(player.level, player.getGameProfile());
					breedingTracker.registerSpecies(individual.getGenome().getPrimary());
					breedingTracker.registerSpecies(individual.getGenome().getSecondary());

					GeneticHelper.setIndividual(specimen, individual);

					// Decrease energy
					removeItem(SLOT_ENERGY, 1);
				}
			}
		}

		setItem(SLOT_ANALYZE_1, specimen);
		setItem(SLOT_SPECIMEN, ItemStack.EMPTY);
	}

	@Override
	public final ImmutableSet<IErrorState> getErrorStates() {
		ImmutableSet.Builder<IErrorState> errorStates = ImmutableSet.builder();

		if (!hasSpecimen()) {
			errorStates.add(EnumErrorCode.NO_SPECIMEN);
		} else {
			IRootDefinition<IForestrySpeciesRoot<IIndividual>> definition = RootUtils.getRoot(getSpecimen());
			if (definition.isPresent() && !isAlyzingFuel(getItem(SLOT_ENERGY))) {
				errorStates.add(EnumErrorCode.NO_HONEY);
			}
		}

		return errorStates.build();
	}

	public ItemStack getSpecimen() {
		for (int i = SLOT_SPECIMEN; i <= SLOT_ANALYZE_5; i++) {
			ItemStack itemStack = getItem(i);
			if (!itemStack.isEmpty()) {
				return itemStack;
			}
		}
		return ItemStack.EMPTY;
	}

	public boolean hasSpecimen() {
		return !getSpecimen().isEmpty();
	}

	@Override
	protected void onWriteNBT(CompoundTag nbt) {
		ItemStack energy = getItem(ItemInventoryAlyzer.SLOT_ENERGY);
		int amount = 0;
		if (!energy.isEmpty()) {
			amount = energy.getCount();
		}
		nbt.putInt("Charges", amount);
	}
}
