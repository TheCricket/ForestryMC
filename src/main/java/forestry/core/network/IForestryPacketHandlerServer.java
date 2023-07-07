package forestry.core.network;

import net.minecraft.server.level.ServerPlayer;

import java.io.IOException;

public interface IForestryPacketHandlerServer extends IForestryPacketHandler {
	void onPacketData(PacketBufferForestry data, ServerPlayer player) throws IOException;
}
