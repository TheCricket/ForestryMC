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
package forestry.core.items;

import forestry.core.ItemGroupForestry;
import forestry.core.utils.ItemTooltipUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;

public class ItemBlockForestry<B extends Block> extends BlockItem {

	private final int burnTime;

	public ItemBlockForestry(B block, Item.Properties builder) {
		super(block, builder);

		if (builder instanceof ItemProperties properties) {
			this.burnTime = properties.burnTime;
		} else {
			burnTime = -1;
		}
	}

	public ItemBlockForestry(B block) {
		this(block, new Item.Properties().tab(ItemGroupForestry.tabForestry));
	}

	@Override
	public B getBlock() {
		//noinspection unchecked
		return (B) super.getBlock();
	}

	@Override
	public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
		return burnTime;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag advanced) {
		super.appendHoverText(stack, world, tooltip, advanced);
		ItemTooltipUtil.addInformation(stack, world, tooltip, advanced);
	}
}
