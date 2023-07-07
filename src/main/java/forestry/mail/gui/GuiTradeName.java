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
package forestry.mail.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import forestry.core.config.Constants;
import forestry.core.gui.GuiForestry;
import forestry.core.render.ColourProperties;
import forestry.core.utils.NetworkUtil;
import forestry.core.utils.Translator;
import forestry.mail.network.packets.PacketTraderAddressRequest;
import forestry.mail.tiles.TileTrader;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.apache.commons.lang3.StringUtils;
import org.lwjgl.glfw.GLFW;

public class GuiTradeName extends GuiForestry<ContainerTradeName> {
	private final TileTrader tile;
	private EditBox addressNameField;

	public GuiTradeName(ContainerTradeName container, Inventory inv, Component title) {
		super(Constants.TEXTURE_PATH_GUI + "/tradername.png", container, inv, title);
		this.tile = container.getTile();
		this.imageWidth = 176;
		this.imageHeight = 90;

		addressNameField = new EditBox(this.minecraft.font, leftPos + 44, topPos + 39, 90, 14, null);
	}

	@Override
	public void init() {
		super.init();

		addressNameField = new EditBox(this.minecraft.font, leftPos + 44, topPos + 39, 90, 14, null);
		addressNameField.setValue(container.getAddress().getName());
		addressNameField.setFocus(true);
	}

	@Override
	public boolean keyPressed(int key, int scanCode, int modifiers) {

		// Set focus or enter text into address
		if (addressNameField.isFocused()) {
			if (scanCode == GLFW.GLFW_KEY_ENTER) {
				setAddress();
			} else {
				addressNameField.keyPressed(key, scanCode, modifiers);
			}
			return true;
		}

		return super.keyPressed(key, scanCode, modifiers);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
		if (super.mouseClicked(mouseX, mouseY, mouseButton)) {
			return false;    //TODO this return value
		}
		addressNameField.mouseClicked(mouseX, mouseY, mouseButton);
		return true;
	}

	@Override
	protected void renderBg(PoseStack transform, float partialTicks, int var3, int var2) {
		super.renderBg(transform, partialTicks, var3, var2);

		String prompt = Translator.translateToLocal("for.gui.mail.nametrader");
		textLayout.startPage(transform);
		textLayout.newLine();
		textLayout.drawCenteredLine(transform, prompt, 0, ColourProperties.INSTANCE.get("gui.mail.text"));
		textLayout.endPage(transform);
		addressNameField.render(transform, var2, var3, partialTicks);    //TODO correct?
	}

	@Override
	public void removed() {
		super.removed();
		setAddress();
	}

	private void setAddress() {
		String address = addressNameField.getValue();
		if (StringUtils.isNotBlank(address)) {
			PacketTraderAddressRequest packet = new PacketTraderAddressRequest(tile, address);
			NetworkUtil.sendToServer(packet);
		}
	}

	@Override
	protected void addLedgers() {
		addErrorLedger(tile);
	}
}
