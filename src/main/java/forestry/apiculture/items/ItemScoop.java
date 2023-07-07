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
package forestry.apiculture.items;

import forestry.api.core.IToolScoop;
import forestry.api.core.ItemGroups;
import forestry.core.data.ForestryTags;
import forestry.core.items.ItemForestry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class ItemScoop extends ItemForestry implements IToolScoop {

	public ItemScoop() {
		super(new Item.Properties()
				.durability(10)
				.tab(ItemGroups.tabApiculture));
	}

	@Override
	public float getDestroySpeed(ItemStack itemstack, BlockState state) {
		if (state.is(ForestryTags.Blocks.MINEABLE_SCOOP)) {
			return 2.0F;
		} else {
			return 1.0F;
		}
	}

	@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity player) {
		stack.hurtAndBreak(2, player, (living) -> living.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		return true;
	}

	@Override
	public boolean mineBlock(ItemStack stack, Level world, BlockState blockState, BlockPos pos, LivingEntity player) {
		if (!world.isClientSide && blockState.getDestroySpeed(world, pos) != 0.0F) {
			stack.hurtAndBreak(1, player, (living) -> living.broadcastBreakEvent(EquipmentSlot.MAINHAND));
		}

		return true;
	}
}
