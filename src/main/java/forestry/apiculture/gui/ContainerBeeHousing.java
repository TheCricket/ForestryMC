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

import forestry.api.climate.IClimateListener;
import forestry.apiculture.features.ApicultureContainers;
import forestry.apiculture.tiles.TileBeeHousingBase;
import forestry.core.climate.ClimateRoot;
import forestry.core.gui.ContainerAnalyzerProvider;
import forestry.core.network.IForestryPacketClient;
import forestry.core.network.PacketBufferForestry;
import forestry.core.network.packets.PacketGuiUpdate;
import forestry.core.tiles.TileUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.common.util.LazyOptional;

public class ContainerBeeHousing extends ContainerAnalyzerProvider<TileBeeHousingBase> implements IContainerBeeHousing {

	private final IGuiBeeHousingDelegate delegate;
	private final GuiBeeHousing.Icon icon;

	public static ContainerBeeHousing fromNetwork(int windowId, Inventory inv, FriendlyByteBuf data) {
		PacketBufferForestry buf = new PacketBufferForestry(data);
		TileBeeHousingBase tile = TileUtil.getTile(inv.player.level, buf.readBlockPos(), TileBeeHousingBase.class);
		boolean hasFrames = buf.readBoolean();
		GuiBeeHousing.Icon icon = buf.readEnum(GuiBeeHousing.Icon.values());
		return new ContainerBeeHousing(windowId, inv, tile, hasFrames, icon);    //TODO nullability.
	}

	//TODO hack icon in GUI by checking title. Then it isn't needed here.
	public ContainerBeeHousing(int windowId, Inventory player, TileBeeHousingBase tile, boolean hasFrames, GuiBeeHousing.Icon icon) {
		super(windowId, ApicultureContainers.BEE_HOUSING.containerType(), player, tile, 8, 108);
		ContainerBeeHelper.addSlots(this, tile, hasFrames);

		tile.getBeekeepingLogic().clearCachedValues();
		LazyOptional<IClimateListener> listener = ClimateRoot.getInstance().getListener(tile.getLevel(), tile.getBlockPos());
		if (player.player instanceof ServerPlayer) {
			listener.ifPresent(l -> l.syncToClient((ServerPlayer) player.player));
		}

		delegate = tile;
		this.icon = icon;
	}

	private int beeProgress = -1;

	@Override
	public void broadcastChanges() {
		super.broadcastChanges();

		int beeProgress = tile.getBeekeepingLogic().getBeeProgressPercent();
		if (this.beeProgress != beeProgress) {
			this.beeProgress = beeProgress;
			IForestryPacketClient packet = new PacketGuiUpdate(tile);
			sendPacketToListeners(packet);
		}
	}

	@Override
	public IGuiBeeHousingDelegate getDelegate() {
		return delegate;
	}

	@Override
	public GuiBeeHousing.Icon getIcon() {
		return icon;
	}
}
