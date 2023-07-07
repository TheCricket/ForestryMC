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
import forestry.core.gui.widgets.GameTokenWidget;
import forestry.core.gui.widgets.ProbeButton;
import forestry.core.gui.widgets.Widget;
import forestry.core.render.ColourProperties;
import forestry.core.tiles.EscritoireGame;
import forestry.core.tiles.EscritoireTextSource;
import forestry.core.tiles.TileEscritoire;
import forestry.core.utils.Translator;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class GuiEscritoire extends GuiForestry<ContainerEscritoire> {
	private final ItemStack LEVEL_ITEM = new ItemStack(Items.PAPER);
	private final EscritoireTextSource textSource = new EscritoireTextSource();
	private final TileEscritoire tile;

	public GuiEscritoire(ContainerEscritoire container, Inventory inv, Component title) {
		super(Constants.TEXTURE_PATH_GUI + "/escritoire.png", container, inv, title);

		this.tile = container.getTile();
		this.imageWidth = 228;
		this.imageHeight = 235;

		this.widgetManager.add(new ProbeButton(this, widgetManager, 14, 16));

		EscritoireGame game = tile.getGame();

		// Inner ring
		addTokenWidget(game, 115, 51, 0);
		addTokenWidget(game, 115, 77, 1);
		addTokenWidget(game, 94, 90, 2);
		addTokenWidget(game, 73, 77, 3);
		addTokenWidget(game, 73, 51, 4);
		addTokenWidget(game, 94, 38, 5);

		// Outer ring
		addTokenWidget(game, 115, 25, 6);
		addTokenWidget(game, 136, 38, 7);
		addTokenWidget(game, 136, 64, 8);

		addTokenWidget(game, 136, 90, 9);
		addTokenWidget(game, 115, 103, 10);
		addTokenWidget(game, 94, 116, 11);

		addTokenWidget(game, 73, 103, 12);
		addTokenWidget(game, 52, 90, 13);
		addTokenWidget(game, 52, 64, 14);

		addTokenWidget(game, 52, 38, 15);
		addTokenWidget(game, 73, 25, 16);
		addTokenWidget(game, 94, 12, 17);

		// Corners
		addTokenWidget(game, 52, 12, 18);
		addTokenWidget(game, 136, 12, 19);
		addTokenWidget(game, 52, 116, 20);
		addTokenWidget(game, 136, 116, 21);
	}

	private void addTokenWidget(EscritoireGame game, int x, int y, int index) {
		Widget gameTokenWidget = new GameTokenWidget(game, widgetManager, x, y, index);
		widgetManager.add(gameTokenWidget);
	}

	@Override
	protected void renderBg(PoseStack transform, float partialTicks, int mouseY, int mouseX) {
		super.renderBg(transform, partialTicks, mouseY, mouseX);

		for (int i = 0; i <= tile.getGame().getBountyLevel() / 4; i++) {
			GuiUtil.drawItemStack(this, LEVEL_ITEM, leftPos + 170 + i * 8, topPos + 7);
		}

		textLayout.startPage(transform);
		{
			transform.scale(0.5F, 0.5F, 0.5F);
			transform.translate(leftPos + 170, topPos + 10, 0.0);

			textLayout.newLine();
			textLayout.newLine();
			String format = ChatFormatting.UNDERLINE + ChatFormatting.ITALIC.toString();
			int attemptNo = EscritoireGame.BOUNTY_MAX - tile.getGame().getBountyLevel();
			String attemptNoString = Translator.translateToLocalFormatted("for.gui.escritoire.attempt.number", attemptNo);
			textLayout.drawLine(transform, format + attemptNoString, 170, ColourProperties.INSTANCE.get("gui.mail.lettertext"));
			textLayout.newLine();
			String escritoireText = textSource.getText(tile.getGame());
			textLayout.drawSplitLine(escritoireText, 170, 90, ColourProperties.INSTANCE.get("gui.mail.lettertext"));
		}
		textLayout.endPage(transform);
	}

	@Override
	protected void addLedgers() {
		addErrorLedger(tile);
		addHintLedger("escritoire");
	}
}
