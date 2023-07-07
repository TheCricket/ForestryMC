package forestry.api.genetics;

import com.mojang.authlib.GameProfile;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.Nullable;

public interface IBreedingTrackerManager {

	void registerTracker(String rootUID, IBreedingTrackerHandler handler);

	<T extends IBreedingTracker> T getTracker(String rootUID, LevelAccessor world, @Nullable GameProfile profile);
}
