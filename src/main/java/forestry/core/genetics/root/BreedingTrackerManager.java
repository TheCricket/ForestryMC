package forestry.core.genetics.root;

import com.google.common.base.Preconditions;
import com.mojang.authlib.GameProfile;
import forestry.api.genetics.IBreedingTracker;
import forestry.api.genetics.IBreedingTrackerHandler;
import forestry.api.genetics.IBreedingTrackerManager;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nullable;
import java.util.LinkedHashMap;
import java.util.Map;

public enum BreedingTrackerManager implements IBreedingTrackerManager {
	INSTANCE;

	BreedingTrackerManager() {
		sidedHandler = DistExecutor.safeRunForDist(() -> ClientBreedingHandler::new, () -> ServerBreedingHandler::new);
	}

	static final Map<String, IBreedingTrackerHandler> factories = new LinkedHashMap<>();

	@Nullable
	private SidedHandler sidedHandler = null;

	@Override
	public void registerTracker(String rootUID, IBreedingTrackerHandler handler) {
		factories.put(rootUID, handler);
	}

	@Override
	public <T extends IBreedingTracker> T getTracker(String rootUID, LevelAccessor world, @Nullable GameProfile profile) {
		return getSidedHandler().getTracker(rootUID, world, profile);
	}

	private SidedHandler getSidedHandler() {
		Preconditions.checkNotNull(sidedHandler, "Called breeding tracker method to early.");
		return sidedHandler;
	}

	interface SidedHandler {
		<T extends IBreedingTracker> T getTracker(String rootUID, LevelAccessor world, @Nullable GameProfile player);
	}
}
