package forestry.sorting.tiles;

import forestry.api.core.ILocatable;
import forestry.api.genetics.filter.IFilterLogic;
import forestry.core.tiles.ITitled;
import net.minecraft.world.Container;

import javax.annotation.Nullable;

public interface IFilterContainer extends ILocatable, ITitled {

	@Nullable
	Container getBuffer();

	TileGeneticFilter getTileEntity();

	IFilterLogic getLogic();
}
