package fr.zeamateis.ice_age.client.particle;

import fr.zeamateis.ice_age.client.particle.type.NumericParticleType;
import fr.zeamateis.ice_age.init.IceAgeParticles;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ParticleDamage extends ParticleNumeric implements IParticleData {

    public ParticleDamage(double numberIn, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46354_8_, double p_i46354_10_, double p_i46354_12_, int colorIn) {
        super(numberIn, worldIn, xCoordIn, yCoordIn, zCoordIn, p_i46354_8_, p_i46354_10_, p_i46354_12_, colorIn);
    }

    public ParticleDamage(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i1211_8_, double p_i1211_10_, double p_i1211_12_) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, p_i1211_8_, p_i1211_10_, p_i1211_12_);
    }

    @Override
    public ParticleType<NumericParticleType> getType() {
        return IceAgeParticles.DAMAGE.getType();
    }

    @Override
    public void write(PacketBuffer buffer) {

    }

    @Override
    public String getParameters() {
        return IceAgeParticles.DAMAGE.getParameters();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<NumericParticleType> {
        @Override
        public Particle makeParticle(NumericParticleType typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleDamage(typeIn.getNumber(), worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getColor());
        }
    }
}
