package forestry.core.network.packets;

import forestry.api.climate.ClimateCapabilities;
import forestry.api.climate.IClimateListener;
import forestry.core.network.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.util.LazyOptional;

public class PacketClimateListenerUpdateRequest extends ForestryPacket implements IForestryPacketServer {
	private final BlockPos pos;

	public PacketClimateListenerUpdateRequest(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	protected void writeData(PacketBufferForestry data) {
		data.writeBlockPos(pos);
	}

	@Override
	public PacketIdServer getPacketId() {
		return PacketIdServer.CLIMATE_LISTENER_UPDATE_REQUEST;
	}

	public static class Handler implements IForestryPacketHandlerServer {

		@Override
		public void onPacketData(PacketBufferForestry data, ServerPlayer player) {
			BlockPos pos = data.readBlockPos();
			BlockEntity tileEntity = player.level.getBlockEntity(pos);
			if (tileEntity != null) {
				LazyOptional<IClimateListener> listener = tileEntity.getCapability(ClimateCapabilities.CLIMATE_LISTENER);
				listener.ifPresent(l -> l.syncToClient(player));
			}
		}
	}
}
