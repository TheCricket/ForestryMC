package forestry.core.registration;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

public class ParticleTypeRegistryObject<PARTICLE extends ParticleOptions> extends WrappedRegistryObject<ParticleType<PARTICLE>> {

	public ParticleTypeRegistryObject(RegistryObject<ParticleType<PARTICLE>> registryObject) {
		super(registryObject);
	}

	@Nonnull
	public ParticleType<PARTICLE> getParticleType() {
		return get();
	}
}
