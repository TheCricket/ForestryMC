package forestry.api.core;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import javax.annotation.Nullable;

public interface ITileTypeProvider<T extends BlockEntity> {
	boolean hasTileType();

	@Nullable
	BlockEntityType<T> getTileType();

	BlockEntityType<T> tileType();


}
