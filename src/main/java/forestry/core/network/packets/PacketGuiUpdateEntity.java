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
package forestry.core.network.packets;

import forestry.core.network.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

public class PacketGuiUpdateEntity extends ForestryPacket implements IForestryPacketClient {
	private final Entity entity;
	private final IStreamableGui streamableGui;

	public PacketGuiUpdateEntity(IStreamableGui streamableGui, Entity entity) {
		this.entity = entity;
		this.streamableGui = streamableGui;
	}

	@Override
	public PacketIdClient getPacketId() {
		return PacketIdClient.GUI_UPDATE_ENTITY;
	}

	@Override
	protected void writeData(PacketBufferForestry data) {
		data.writeEntityById(entity);
		streamableGui.writeGuiData(data);
	}

	@OnlyIn(Dist.CLIENT)
	public static class Handler implements IForestryPacketHandlerClient {
		@Override
		public void onPacketData(PacketBufferForestry data, Player player) throws IOException {
			Entity entity = data.readEntityById(player.level);
			if (entity instanceof IStreamableGui) {
				((IStreamableGui) entity).readGuiData(data);
			}
		}
	}
}
