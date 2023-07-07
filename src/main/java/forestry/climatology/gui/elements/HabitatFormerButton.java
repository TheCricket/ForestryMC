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
package forestry.climatology.gui.elements;

import com.mojang.blaze3d.vertex.PoseStack;
import forestry.climatology.gui.GuiHabitatFormer;
import forestry.core.features.CoreItems;
import forestry.core.gui.Drawable;
import forestry.core.gui.GuiUtil;
import forestry.core.gui.elements.ButtonElement;
import forestry.core.items.definitions.EnumElectronTube;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.ItemStack;

import java.util.function.Consumer;

public class HabitatFormerButton extends ButtonElement {
	private static final Drawable ENABLED_BUTTON = new Drawable(GuiHabitatFormer.TEXTURE, 234, 0, 22, 22);
	private static final Drawable DISABLED_BUTTON = new Drawable(GuiHabitatFormer.TEXTURE, 212, 0, 22, 22);

	private final ItemStack iconStack;

	public HabitatFormerButton(int xPos, int yPos, boolean selectionButton, Consumer<Boolean> onClicked) {
		super(new ButtonElement.Builder()
				.action(button -> onClicked.accept(selectionButton))
				.size(22)
				.textures(DISABLED_BUTTON, ENABLED_BUTTON)
				.pos(xPos, yPos)
		);
		this.iconStack = selectionButton ? CoreItems.ELECTRON_TUBES.stack(EnumElectronTube.GOLD, 1) : CoreItems.GEAR_BRONZE.stack();
	}

	@Override
	public void drawElement(PoseStack transform, int mouseX, int mouseY) {
		super.drawElement(transform, mouseX, mouseY);
		Font fontRenderer = Minecraft.getInstance().font;
		GuiUtil.drawItemStack(fontRenderer, iconStack, 3, 3);
	}
}
