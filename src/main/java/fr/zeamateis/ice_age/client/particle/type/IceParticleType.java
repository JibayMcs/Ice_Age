package fr.zeamateis.ice_age.client.particle.type;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class IceParticleType extends ParticleType<IceParticleType> implements IParticleData {
    public static final IDeserializer<IceParticleType> DESERIALIZER = new IDeserializer<IceParticleType>() {
        @Override
        public IceParticleType deserialize(ParticleType<IceParticleType> particleTypeIn, StringReader reader) throws CommandSyntaxException {
            reader.expect(' ');
            return new IceParticleType(particleTypeIn.getId());
        }

        @Override
        public IceParticleType read(ParticleType<IceParticleType> particleTypeIn, PacketBuffer buffer) {
            return new IceParticleType(particleTypeIn.getId());
        }
    };

    private double randomRotX, randomRotY, randomRotZ;


    public IceParticleType(ResourceLocation resourceLocationIn) {
        super(resourceLocationIn, true, DESERIALIZER);
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeDouble(randomRotX);
        buffer.writeDouble(randomRotY);
        buffer.writeDouble(randomRotZ);
    }

    @Override
    public String getParameters() {
        return getType().getId() + " " + randomRotX + " " + randomRotY + " " + randomRotZ;
    }

    @Override
    public ParticleType<IceParticleType> getType() {
        return this;
    }

    @OnlyIn(Dist.CLIENT)
    public double getRandomRotX() {
        return randomRotX;
    }

    public void setRandomRotX(double randomRotX) {
        this.randomRotX = randomRotX;
    }

    @OnlyIn(Dist.CLIENT)
    public double getRandomRotY() {
        return randomRotY;
    }

    public void setRandomRotY(double randomRotY) {
        this.randomRotY = randomRotY;
    }

    @OnlyIn(Dist.CLIENT)
    public double getRandomRotZ() {
        return randomRotZ;
    }

    public void setRandomRotZ(double randomRotZ) {
        this.randomRotZ = randomRotZ;
    }

}