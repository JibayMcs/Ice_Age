package fr.zeamateis.ice_age.init;

import fr.zeamateis.ice_age.client.particle.ParticleIce;
import fr.zeamateis.ice_age.client.particle.type.IceParticleType;
import fr.zeamateis.ice_age.client.particle.type.NumericParticleType;
import fr.zeamateis.ice_age.common.IceAgeMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.IRegistry;

public class IceAgeParticles {

    public static NumericParticleType DAMAGE;
    public static IceParticleType ICE;

    private static <T extends ParticleType<?>> T getRegisteredParticleTypes(String particleNameIn) {

        T t = (T) IRegistry.PARTICLE_TYPE.get(new ResourceLocation(IceAgeMod.MODID, particleNameIn));
        if (t == null) {
            throw new IllegalStateException("Invalid or unknown particle type: " + particleNameIn);
        } else {
            return t;
        }
    }

    public static void spawnParticle(IceParticleType particleTypeIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        Minecraft.getInstance().particles.addParticle(particleTypeIn, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    public void registerParticles() {
        create("particle_ice").init(ICE = getRegisteredParticleTypes("particle_ice")).build(ICE, new ParticleIce.Factory());
    }

    private IceAgeParticles create(String particleNameIn) {
        IRegistry.PARTICLE_TYPE.put(new ResourceLocation(IceAgeMod.MODID, particleNameIn), new IceParticleType(new ResourceLocation(IceAgeMod.MODID, particleNameIn)));
        return this;
    }

    private IceAgeParticles init(IceParticleType specialParticleType) {
        getRegisteredParticleTypes(specialParticleType.getType().getId().getPath());
        return this;
    }

    private IceAgeParticles build(ParticleType<IceParticleType> particleTypeIn, IParticleFactory<IceParticleType> particleFactoryIn) {
        Minecraft.getInstance().particles.registerFactory(particleTypeIn, particleFactoryIn);
        return this;
    }
}
