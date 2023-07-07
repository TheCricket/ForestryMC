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
package forestry.core.gui;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import forestry.api.core.tooltips.IToolTipProvider;
import forestry.api.core.tooltips.ToolTip;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Collection;

@OnlyIn(Dist.CLIENT)
public class GuiUtil {
	public static void drawItemStack(GuiForestry gui, ItemStack stack, int xPos, int yPos) {
		drawItemStack(gui.getFontRenderer(), stack, xPos, yPos);
	}

	public static void drawItemStack(Font fontRenderer, ItemStack stack, int xPos, int yPos) {
		Font font = fontRenderer;

		ItemRenderer itemRender = Minecraft.getInstance().getItemRenderer();
		itemRender.renderAndDecorateItem(stack, xPos, yPos);
		itemRender.renderGuiItemDecorations(font, stack, xPos, yPos, null);
	}

	//TODO hopefully this is client side...
	public static void drawToolTips(PoseStack transform, IGuiSizable gui, @Nullable IToolTipProvider provider, ToolTip toolTips, int mouseX, int mouseY) {
		if (!toolTips.isEmpty()) {
			transform.pushPose();
			if (provider == null || provider.isRelativeToGui()) {
				transform.translate(-gui.getGuiLeft(), -gui.getGuiTop(), 0);
			}
			Window window = Minecraft.getInstance().getWindow();    //TODO - more resolution stuff to check
			//GuiUtils.drawHoveringText(transform, toolTips.getLines(), mouseX, mouseY, window.getGuiScaledWidth(), window.getGuiScaledHeight(), -1, gui.getGameInstance().font);
			transform.popPose();
		}
	}

	public static void drawToolTips(PoseStack transform, IGuiSizable gui, Collection<?> objects, int mouseX, int mouseY) {
		for (Object obj : objects) {
			if (!(obj instanceof IToolTipProvider provider)) {
				continue;
			}
			if (!provider.isToolTipVisible()) {
				continue;
			}
			int mX = mouseX;
			int mY = mouseY;
			if (provider.isRelativeToGui()) {
				mX -= gui.getGuiLeft();
				mY -= gui.getGuiTop();
			}
			ToolTip tips = provider.getToolTip(mX, mY);
			if (tips == null) {
				continue;
			}
			boolean mouseOver = provider.isHovering(mX, mY);
			tips.onTick(mouseOver);
			if (mouseOver && tips.isReady()) {
				tips.refresh();
				drawToolTips(transform, gui, provider, tips, mouseX, mouseY);
			}
		}
	}
}
