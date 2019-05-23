package fr.zeamateis.ice_age.init;

import fr.zeamateis.amy.particle.ParticleBuilder;
import fr.zeamateis.ice_age.client.particle.ParticleIce;
import fr.zeamateis.ice_age.client.particle.type.IceParticleType;
import fr.zeamateis.ice_age.common.IceAgeMod;
import net.minecraft.util.ResourceLocation;

public class IceAgeParticles {

    private static final ParticleBuilder BUILDER = new ParticleBuilder();

    public static IceParticleType ICE;

    public void registerParticles() {
        ICE = BUILDER.create(new IceParticleType(new ResourceLocation(IceAgeMod.MODID, "particle_ice")), new ParticleIce.Factory());
    }

}
