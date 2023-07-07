package forestry.core.registration;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ParticleTypeDeferredRegister extends WrappedDeferredRegister<ParticleType<?>> {

	public ParticleTypeDeferredRegister(String modid) {
		super(modid, ForgeRegistries.PARTICLE_TYPES);
	}

	public ParticleTypeRegistryObject<SimpleParticleType> registerBasicParticle(String name) {
		return register(name, () -> new SimpleParticleType(false));
	}

	public <PARTICLE extends ParticleOptions> ParticleTypeRegistryObject<PARTICLE> register(String name, Supplier<ParticleType<PARTICLE>> sup) {
		return register(name, sup, ParticleTypeRegistryObject::new);
	}
}