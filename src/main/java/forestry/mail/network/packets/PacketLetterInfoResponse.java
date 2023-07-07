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

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import forestry.api.mail.*;
import forestry.core.network.*;
import forestry.mail.TradeStationInfo;
import forestry.mail.gui.ILetterInfoReceiver;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.UUID;

// TODO: split this into two different packets
public class PacketLetterInfoResponse extends ForestryPacket implements IForestryPacketClient {
	public final EnumAddressee type;
	@Nullable
	public final ITradeStationInfo tradeInfo;
	@Nullable
	public final IMailAddress address;

	public PacketLetterInfoResponse(EnumAddressee type, @Nullable ITradeStationInfo info, @Nullable IMailAddress address) {
		this.type = type;
		if (type == EnumAddressee.TRADER) {
			this.tradeInfo = info;
			this.address = null;
		} else if (type == EnumAddressee.PLAYER) {
			this.tradeInfo = info;
			this.address = address;
		} else {
			throw new IllegalArgumentException("Unknown addressee type: " + type);
		}
	}

	@Override
	public PacketIdClient getPacketId() {
		return PacketIdClient.LETTER_INFO_RESPONSE;
	}

	@Override
	public void writeData(PacketBufferForestry data) {
		data.writeEnum(type, EnumAddressee.values());

		if (type == EnumAddressee.PLAYER) {
			Preconditions.checkNotNull(address);
			GameProfile profile = address.getPlayerProfile();

			data.writeLong(profile.getId().getMostSignificantBits());
			data.writeLong(profile.getId().getLeastSignificantBits());
			data.writeUtf(profile.getName());

		} else if (type == EnumAddressee.TRADER) {
			if (tradeInfo == null) {
				data.writeBoolean(false);
			} else {
				data.writeBoolean(true);
				data.writeUtf(tradeInfo.getAddress().getName());

				data.writeLong(tradeInfo.getOwner().getId().getMostSignificantBits());
				data.writeLong(tradeInfo.getOwner().getId().getLeastSignificantBits());
				data.writeUtf(tradeInfo.getOwner().getName());

				data.writeItem(tradeInfo.getTradegood());
				data.writeItemStacks(tradeInfo.getRequired());

				data.writeEnum(tradeInfo.getState(), EnumTradeStationState.values());
			}
		}
	}

	@OnlyIn(Dist.CLIENT)
	public static class Handler implements IForestryPacketHandlerClient {
		@Override
		public void onPacketData(PacketBufferForestry data, Player player) throws IOException {
			AbstractContainerMenu container = player.containerMenu;
			if (container instanceof ILetterInfoReceiver) {
				EnumAddressee type = data.readEnum(EnumAddressee.values());
				ITradeStationInfo tradeInfo = null;
				IMailAddress address = null;

				if (type == EnumAddressee.PLAYER) {
					GameProfile profile = new GameProfile(new UUID(data.readLong(), data.readLong()), data.readUtf());
					address = PostManager.postRegistry.getMailAddress(profile);
				} else if (type == EnumAddressee.TRADER) {
					if (data.readBoolean()) {
						address = PostManager.postRegistry.getMailAddress(data.readUtf());
						GameProfile owner = new GameProfile(new UUID(data.readLong(), data.readLong()), data.readUtf());

						ItemStack tradegood = data.readItem();
						NonNullList<ItemStack> required = data.readItemStacks();

						EnumTradeStationState state = data.readEnum(EnumTradeStationState.values());
						tradeInfo = new TradeStationInfo(address, owner, tradegood, required, state);
					} else {
						tradeInfo = null;
					}
				}
				((ILetterInfoReceiver) container).handleLetterInfoUpdate(type, address, tradeInfo);
			}
		}
	}
}
