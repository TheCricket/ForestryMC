package forestry.core.genetics.root;

import com.mojang.authlib.GameProfile;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IBreedingTrackerHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public class ClientBreedingHandler extends ServerBreedingHandler {
	private final Map<String, IBreedingTracker> trackerByUID = new LinkedHashMap<>();

	@Override
	@SuppressWarnings("unchecked")
	public <T extends IBreedingTracker> T getTracker(String rootUID, LevelAccessor world, @Nullable GameProfile profile) {
		if (world instanceof ServerLevel) {
			return super.getTracker(rootUID, world, profile);
		}
		IBreedingTrackerHandler handler = BreedingTrackerManager.factories.get(rootUID);
		T tracker = (T) trackerByUID.computeIfAbsent(rootUID, (key) -> handler.createTracker());
		handler.populateTracker(tracker, Minecraft.getInstance().level, profile);
		return tracker;
	}
}
