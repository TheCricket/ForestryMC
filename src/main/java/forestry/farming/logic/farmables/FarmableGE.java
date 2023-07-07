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
package forestry.farming.logic.farmables;

import forestry.api.arboriculture.TreeManager;
import forestry.api.arboriculture.genetics.ITree;
import forestry.api.arboriculture.genetics.ITreeRoot;
import forestry.api.farming.ICrop;
import forestry.api.farming.IFarmable;
import forestry.api.genetics.alleles.AlleleManager;
import forestry.api.genetics.products.Product;
import forestry.arboriculture.features.ArboricultureBlocks;
import forestry.farming.logic.crops.CropDestroy;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FarmableGE implements IFarmable {

	private final Set<Item> windfall = new HashSet<>();

	//TODO would be nice to make this class more granular so windfall and germling checks could be more specific
	public FarmableGE() {
		windfall.addAll(AlleleManager.geneticRegistry.getRegisteredFruitFamilies().values().stream()
			.map(TreeManager.treeRoot::getFruitProvidersForFruitFamily)
			.flatMap(Collection::stream)
			.flatMap(p -> Stream.concat(p.getProducts().getPossibleProducts().stream(), p.getSpecialty().getPossibleProducts().stream()))
			.map(Product::getItem)
			.collect(Collectors.toSet()));
	}

	@Override
	public boolean isSaplingAt(Level world, BlockPos pos, BlockState blockState) {
		return ArboricultureBlocks.SAPLING_GE.blockEqual(blockState);
	}

	@Override
	@Nullable
	public ICrop getCropAt(Level world, BlockPos pos, BlockState blockState) {
		if (!blockState.is(BlockTags.LOGS)) {
			return null;
		}

		return new CropDestroy(world, blockState, pos, null);
	}

	@Override
	public boolean plantSaplingAt(Player player, ItemStack germling, Level world, BlockPos pos) {
		ITreeRoot treeRoot = TreeManager.treeRoot;

		ITree tree = treeRoot.create(germling).orElse(null);
		return tree != null && treeRoot.plantSapling(world, tree, player.getGameProfile(), pos);
	}

	@Override
	public boolean isGermling(ItemStack itemstack) {
		return TreeManager.treeRoot.isMember(itemstack);
	}

	@Override
	public boolean isWindfall(ItemStack itemstack) {
		return windfall.contains(itemstack.getItem());
	}

}
