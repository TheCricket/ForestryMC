/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/

package forestry.api.storage;

import forestry.api.genetics.IForestrySpeciesRoot;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

/**
 * The Backpack Interface allows you to add items to Forestry backpacks or create your own backpacks.
 * <p>
 * To create your own backpack, create an {@link IBackpackDefinition}.
 * Backpack definitions have a filter, which you can create here with
 * {@link #createNaturalistBackpackFilter(String)}
 * or create your own.
 * <p>
 * After you've created your backpack definition, create the item with
 * {@link #createBackpack(IBackpackDefinition, EnumBackpackType)}
 * or {@link #createNaturalistBackpack(IBackpackDefinition, String, CreativeModeTab)}
 * and then register the returned item like any other item.
 */
public interface IBackpackInterface {

	/**
	 * Creates a backpack with the given UID and type, returning the item.
	 *
	 * @param definition The backpack definition.
	 * @param type       Type of backpack.
	 * @return Created backpack item.
	 */
	Item createBackpack(IBackpackDefinition definition, EnumBackpackType type);

	/**
	 * Create a backpack that can hold items from a specific {@link IForestrySpeciesRoot}.
	 *
	 * @param definition The backpack definition.
	 * @param rootUid    The species root.
	 * @param tab
	 * @return Created backpack item.
	 */
	Item createNaturalistBackpack(IBackpackDefinition definition, String rootUid, CreativeModeTab tab);

	/**
	 * Makes a new naturalist backpack filter. Only accepts items from a specific {@link IForestrySpeciesRoot}.
	 * Useful for implementing {@link IBackpackDefinition} for naturalist's backpacks.
	 *
	 * @param speciesRootUid The species root's unique ID. See {@link IForestrySpeciesRoot#getUID()}.
	 * @return a new backpack filter for the specified species root
	 * @see #createNaturalistBackpack(IBackpackDefinition, String, CreativeModeTab)
	 */
	Predicate<ItemStack> createNaturalistBackpackFilter(String speciesRootUid);
}
