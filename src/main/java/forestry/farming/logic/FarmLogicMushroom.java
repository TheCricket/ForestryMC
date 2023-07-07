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
package forestry.farming.logic;

import forestry.api.farming.IFarmHousing;
import forestry.api.farming.IFarmProperties;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FarmLogicMushroom extends FarmLogicArboreal {

	public FarmLogicMushroom(IFarmProperties properties, boolean isManual) {
		super(properties, isManual);
	}

	@Override
	public NonNullList<ItemStack> collect(Level world, IFarmHousing farmHousing) {
		return NonNullList.create();//Needed to override Arboreal #collect
	}

}
