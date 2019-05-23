package fr.zeamateis.amy.particle;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.registry.IRegistry;

public class ParticleBuilder {


    public <T extends IParticleData> T create(ParticleType<T> particleTypeIn, IParticleFactory particleFactoryIn) {
        IRegistry.PARTICLE_TYPE.put(particleTypeIn.getId(), particleTypeIn);
        Minecraft.getInstance().particles.registerFactory(particleTypeIn, particleFactoryIn);
        return (T) IRegistry.PARTICLE_TYPE.get(particleTypeIn.getId());
    }
}
