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
package forestry.apiculture.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import forestry.api.core.tooltips.ToolTip;
import forestry.core.gui.widgets.Widget;
import forestry.core.gui.widgets.WidgetManager;
import forestry.core.render.TextureManagerForestry;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Collection;
import java.util.Collections;
import java.util.Locale;

public class HabitatSlot extends Widget {
	private final Collection<Biome.BiomeCategory> biomes;
	private final String name;
	private final String iconIndex;
	public boolean isActive = false;

	public HabitatSlot(WidgetManager widgetManager, int xPos, int yPos, String name, Collection<Biome.BiomeCategory> biomes) {
		super(widgetManager, xPos, yPos);
		this.biomes = biomes;
		this.name = name;
		this.iconIndex = "habitats/" + name.toLowerCase(Locale.ENGLISH);
	}

	@Override
	public ToolTip getToolTip(int mouseX, int mouseY) {
		ToolTip tooltip = new ToolTip();
		tooltip.add(new TextComponent(name));
		return tooltip;
	}

	@OnlyIn(Dist.CLIENT)
	public TextureAtlasSprite getIcon() {
		return TextureManagerForestry.getInstance().getDefault(iconIndex);
	}

	public void setActive(Collection<Biome.BiomeCategory> biomes) {
		isActive = !Collections.disjoint(this.biomes, biomes);
	}

	@Override
	public void draw(PoseStack transform, int startY, int startX) {
		if (!isActive) {
			RenderSystem.setShaderColor(0.2f, 0.2f, 0.2f, 0.2f);
		} else {
			RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		}

		TextureManagerForestry.getInstance().bindGuiTextureMap();
		GuiComponent.blit(transform, startX + xPos, startY + yPos, manager.gui.getBlitOffset(), 16, 16, getIcon());
	}
}
