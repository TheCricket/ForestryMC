package forestry.cultivation.items;

import forestry.core.items.ItemBlockForestry;
import forestry.cultivation.blocks.BlockPlanter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;

public class ItemBlockPlanter extends ItemBlockForestry<BlockPlanter> {

	public ItemBlockPlanter(BlockPlanter block) {
		super(block);
	}

	@Override
	public Component getName(ItemStack stack) {
		String name = getBlock().blockType.getSerializedName();
		return new TranslatableComponent("block.forestry.planter." + (getBlock().getMode().getSerializedName()), new TranslatableComponent("block.forestry." + name));
	}
}
