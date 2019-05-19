package fr.zeamateis.ice_age.proxy;

import fr.zeamateis.ice_age.client.render.entity.RenderFrozenPlayer;
import fr.zeamateis.ice_age.common.entity.EntityFrozenDeadPlayer;
import fr.zeamateis.ice_age.init.IceAgeParticles;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void onRegistryEntityRenderer() {
        RenderingRegistry.registerEntityRenderingHandler(EntityFrozenDeadPlayer.class, RenderFrozenPlayer::new);
    }

    @Override
    public void onRegistryParticle() {
        IceAgeParticles particles = new IceAgeParticles();
        particles.registerParticles();
    }
}