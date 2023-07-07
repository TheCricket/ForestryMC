package forestry.core.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public interface IColoredBlock {
	@OnlyIn(Dist.CLIENT)
	int colorMultiplier(BlockState state, @Nullable BlockGetter worldIn, @Nullable BlockPos pos, int tintIndex);
}
