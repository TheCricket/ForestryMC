package forestry.database.blocks;

import forestry.core.blocks.BlockBase;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Material;

public class BlockDatabase extends BlockBase<BlockTypeDatabase> {

	public BlockDatabase(BlockTypeDatabase blockType) {
		super(blockType, Block.Properties.of(Material.METAL));
	}

}
