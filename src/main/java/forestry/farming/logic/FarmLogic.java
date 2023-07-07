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

import com.google.common.base.Predicate;
import forestry.api.farming.*;
import forestry.core.utils.VectUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

public abstract class FarmLogic implements IFarmLogic {
	private final EntitySelectorFarm entitySelectorFarm;
	protected final IFarmProperties properties;
	protected final boolean isManual;

	public FarmLogic(IFarmProperties properties, boolean isManual) {
		this.properties = properties;
		this.isManual = isManual;
		this.entitySelectorFarm = new EntitySelectorFarm(properties);
	}

	protected Collection<IFarmable> getFarmables() {
		return properties.getFarmables();
	}

	protected Collection<Soil> getSoils() {
		return properties.getSoils();
	}

	@Override
	public IFarmProperties getProperties() {
		return properties;
	}

	@Override
	public boolean isManual() {
		return isManual;
	}

	@Override
	public Collection<ICrop> harvest(Level world, IFarmHousing housing, FarmDirection direction, int extent, BlockPos pos) {
		Stack<ICrop> crops = new Stack<>();
		for (int i = 0; i < extent; i++) {
			BlockPos position = translateWithOffset(pos.above(), direction, i);
			ICrop crop = getCrop(world, position);
			if (crop != null) {
				crops.push(crop);
			}
		}
		return crops;
	}

	@Nullable
	protected ICrop getCrop(Level world, BlockPos position) {
		if (!world.hasChunkAt(position) || world.isEmptyBlock(position)) {
			return null;
		}
		BlockState blockState = world.getBlockState(position);
		for (IFarmable seed : getFarmables()) {
			ICrop crop = seed.getCropAt(world, position, blockState);
			if (crop != null) {
				return crop;
			}
		}
		return null;
	}

	@Deprecated
	public boolean isAcceptedWindfall(ItemStack stack) {
		return false;
	}

	protected final boolean isWaterSourceBlock(Level world, BlockPos position) {
		if (!world.hasChunkAt(position)) {
			return false;
		}
		BlockState blockState = world.getBlockState(position);
		Block block = blockState.getBlock();
		return block == Blocks.WATER;
	}

	protected final boolean isIceBlock(Level world, BlockPos position) {
		if (!world.hasChunkAt(position)) {
			return false;
		}
		BlockState blockState = world.getBlockState(position);
		Block block = blockState.getBlock();
		return block == Blocks.ICE;
	}

	protected final BlockPos translateWithOffset(BlockPos pos, FarmDirection farmDirection, int step) {
		return VectUtil.scale(farmDirection.getFacing().getNormal(), step).offset(pos);
	}

	private static AABB getHarvestBox(Level world, IFarmHousing farmHousing, boolean toWorldHeight) {
		BlockPos coords = farmHousing.getCoords();
		Vec3i area = farmHousing.getArea();
		Vec3i offset = farmHousing.getOffset();

		BlockPos min = coords.offset(offset);
		BlockPos max = min.offset(area);

		int maxY = max.getY();
		if (toWorldHeight) {
			maxY = world.getMaxBuildHeight();
		}

		return new AABB(min.getX(), min.getY(), min.getZ(), max.getX(), maxY, max.getZ());
	}

	protected NonNullList<ItemStack> collectEntityItems(Level world, IFarmHousing farmHousing, boolean toWorldHeight) {
		AABB harvestBox = getHarvestBox(world, farmHousing, toWorldHeight);

		List<ItemEntity> entityItems = world.getEntitiesOfClass(ItemEntity.class, harvestBox, entitySelectorFarm);
		NonNullList<ItemStack> stacks = NonNullList.create();
		for (ItemEntity entity : entityItems) {
			ItemStack contained = entity.getItem();
			stacks.add(contained.copy());
			entity.remove(Entity.RemovalReason.DISCARDED);
		}
		return stacks;
	}

	// for debugging
	@Override
	public String toString() {
		return properties.getTranslationKey();
	}

	private static class EntitySelectorFarm implements Predicate<ItemEntity> {
		private final IFarmProperties properties;

		public EntitySelectorFarm(IFarmProperties properties) {
			this.properties = properties;
		}

		@Override
		public boolean apply(@Nullable ItemEntity entity) {
			if (entity == null || !entity.isAlive()) {
				return false;
			}

			//TODO not sure if this key still exists
			if (entity.getPersistentData().getBoolean("PreventRemoteMovement")) {
				return false;
			}

			ItemStack contained = entity.getItem();
			return properties.isAcceptedSeedling(contained) || properties.isAcceptedWindfall(contained);
		}
	}
}
