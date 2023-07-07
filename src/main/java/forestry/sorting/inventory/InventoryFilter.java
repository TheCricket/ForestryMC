package forestry.sorting.inventory;

import forestry.core.inventory.InventoryAdapterTile;
import forestry.sorting.tiles.TileGeneticFilter;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;

public class InventoryFilter extends InventoryAdapterTile<TileGeneticFilter> {
	public InventoryFilter(TileGeneticFilter tile) {
		super(tile, 6, "Items");
	}

	@Override
	public boolean canTakeItemThroughFace(int slotIndex, ItemStack stack, Direction side) {
		return false;
	}
}
