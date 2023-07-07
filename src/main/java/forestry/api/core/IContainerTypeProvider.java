package forestry.api.core;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import javax.annotation.Nullable;

public interface IContainerTypeProvider<C extends AbstractContainerMenu> {
	boolean hasContainerType();

	@Nullable
	MenuType<C> getContainerType();

	MenuType<C> containerType();
}
