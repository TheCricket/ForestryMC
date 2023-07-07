package forestry.core.particles;

import forestry.core.config.Constants;
import forestry.core.registration.ParticleTypeDeferredRegister;
import forestry.core.registration.ParticleTypeRegistryObject;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Bus.MOD)
public class CoreParticles {
	public static final ParticleTypeDeferredRegister PARTICLE_TYPES = new ParticleTypeDeferredRegister(Constants.MOD_ID);

	public static final ParticleTypeRegistryObject<SnowParticleData> SNOW_PARTICLE = PARTICLE_TYPES.register("snow_particle", SnowParticleType::new);

	@SubscribeEvent
	public static void registerParticleFactory(ParticleFactoryRegisterEvent event) {
		Minecraft.getInstance().particleEngine.register(CoreParticles.SNOW_PARTICLE.getParticleType(), SnowParticle.Factory::new);
	}
}
