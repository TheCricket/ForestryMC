package forestry.core.items;

import forestry.core.ItemGroupForestry;
import forestry.core.blocks.IBlockTypeTesr;
import forestry.core.render.RenderForestryItemProperties;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class ItemBlockBase<B extends Block> extends ItemBlockForestry<B> {
	public final IBlockTypeTesr blockTypeTesr;
	
	public ItemBlockBase(B block, Properties builder, final IBlockTypeTesr blockTypeTesr) {
		super(block, builder);
		this.blockTypeTesr = blockTypeTesr;
	}

	public ItemBlockBase(B block, final IBlockTypeTesr blockTypeTesr) {
		super(block, new Properties().tab(ItemGroupForestry.tabForestry));
		this.blockTypeTesr = blockTypeTesr;
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		final var renderItem = new RenderForestryItemProperties(this);
		consumer.accept(renderItem);
	}
}
