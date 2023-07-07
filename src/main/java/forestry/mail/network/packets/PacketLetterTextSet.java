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
package forestry.mail.network.packets;

import forestry.core.network.*;
import forestry.mail.gui.ContainerLetter;
import net.minecraft.server.level.ServerPlayer;

public class PacketLetterTextSet extends ForestryPacket implements IForestryPacketServer {
	private final String string;

	public PacketLetterTextSet(String string) {
		this.string = string;
	}

	@Override
	public PacketIdServer getPacketId() {
		return PacketIdServer.LETTER_TEXT_SET;
	}

	@Override
	protected void writeData(PacketBufferForestry data) {
		data.writeUtf(string);
	}

	public static class Handler implements IForestryPacketHandlerServer {

		@Override
		public void onPacketData(PacketBufferForestry data, ServerPlayer player) {
			if (player.containerMenu instanceof ContainerLetter) {
				String string = data.readUtf();
				((ContainerLetter) player.containerMenu).handleSetText(string);
			}
		}
	}
}
