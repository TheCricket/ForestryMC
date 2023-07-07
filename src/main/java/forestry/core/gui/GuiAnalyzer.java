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

import com.mojang.blaze3d.vertex.PoseStack;
import forestry.core.config.Constants;
import forestry.core.gui.widgets.TankWidget;
import forestry.core.render.EnumTankLevel;
import forestry.core.tiles.TileAnalyzer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuiAnalyzer extends GuiForestryTitled<ContainerAnalyzer> {
	private final TileAnalyzer tile;

	public GuiAnalyzer(ContainerAnalyzer analyzer, Inventory inventory, Component title) {
		super(Constants.TEXTURE_PATH_GUI + "/alyzer.png", analyzer, inventory, title);
		this.tile = analyzer.tile;
		this.imageHeight = 176;
		this.widgetManager.add(new TankWidget(this.widgetManager, 95, 24, 0));
	}

	@Override
	protected void renderBg(PoseStack transform, float partialTicks, int mouseY, int mouseX) {
		super.renderBg(transform, partialTicks, mouseY, mouseX);
		drawAnalyzeMeter(transform, leftPos + 64, topPos + 30, tile.getProgressScaled(46), EnumTankLevel.rateTankLevel(tile.getProgressScaled(100)));
	}

	private void drawAnalyzeMeter(PoseStack transform, int x, int y, int height, EnumTankLevel rated) {
		int i = 176 + rated.getLevelScaled(16);
		int k = 60;

		blit(transform, x, y + 46 - height, i, k + 46 - height, 4, height);
	}

	@Override
	protected void addLedgers() {
		addErrorLedger(tile);
		addHintLedger("analyzer");
	}
}
