package forestry.sorting.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import forestry.core.gui.GuiForestry;
import net.minecraft.network.chat.Component;

import java.util.Collection;

public interface ISelectableProvider<S> {
	Collection<S> getEntries();

	void onSelect(S selectable);

	void draw(GuiForestry gui, S selectable, PoseStack transform, int y, int x);

	Component getName(S selectable);
}
