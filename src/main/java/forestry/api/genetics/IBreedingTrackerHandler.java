package forestry.api.genetics;

import com.mojang.authlib.GameProfile;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public interface IBreedingTrackerHandler {

	String getFileName(@Nullable GameProfile profile);

	IBreedingTracker createTracker();

	IBreedingTracker createTracker(CompoundTag tag);

	void populateTracker(IBreedingTracker tracker, @Nullable Level world, @Nullable GameProfile profile);

}
