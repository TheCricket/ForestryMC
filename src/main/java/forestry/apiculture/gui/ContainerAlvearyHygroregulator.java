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
package forestry.apiculture.gui;

import forestry.apiculture.features.ApicultureContainers;
import forestry.apiculture.inventory.InventoryHygroregulator;
import forestry.apiculture.multiblock.TileAlvearyHygroregulator;
import forestry.core.gui.ContainerLiquidTanks;
import forestry.core.gui.slots.SlotLiquidIn;
import forestry.core.tiles.TileUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;

public class ContainerAlvearyHygroregulator extends ContainerLiquidTanks<TileAlvearyHygroregulator> {

	public static ContainerAlvearyHygroregulator fromNetwork(int windowId, Inventory inv, FriendlyByteBuf data) {
		TileAlvearyHygroregulator tile = TileUtil.getTile(inv.player.level, data.readBlockPos(), TileAlvearyHygroregulator.class);
		return new ContainerAlvearyHygroregulator(windowId, inv, tile);    //TODO nullability.
	}

	public ContainerAlvearyHygroregulator(int windowId, Inventory playerInventory, TileAlvearyHygroregulator tile) {
		super(windowId, ApicultureContainers.ALVEARY_HYGROREGULATOR.containerType(), playerInventory, tile, 8, 84);

		this.addSlot(new SlotLiquidIn(tile, InventoryHygroregulator.SLOT_INPUT, 56, 38));
	}

}
