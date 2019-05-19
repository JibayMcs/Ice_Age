package fr.zeamateis.ice_age.common.capability.player.temperature;

import fr.zeamateis.amy.network.AmyNetwork;
import fr.zeamateis.ice_age.network.packet.PacketTemperature;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Default implementation of ITemperature
 */
public class Temperature implements ITemperature {
    private final EntityPlayer player;
    private final float max_temperature = 20.0F;
    private float temperature;

    Temperature(EntityPlayer playerIn) {
        player = playerIn;
    }

    @Override
    public void consume(float points) {
        temperature -= points;

        if (temperature < 0.0F) {
            temperature = 0.0F;
        }
        synchronize();
    }

    @Override
    public void warm(float points) {
        if (temperature <= max_temperature) {
            temperature += points;
        }

        synchronize();
    }

    @Override
    public void set(float points) {
        if (points <= max_temperature) {
            temperature = points;
            synchronize();
        }
    }

    @Override
    public float getTemperature() {
        return temperature;
    }

    @Override
    public float getMaxTemperature() {
        return max_temperature;
    }

    @Override
    public void synchronize() {
        //System.out.println("SEND PACKET TEMP");
        // if (player != null && !player.getEntityWorld().isRemote) {
        AmyNetwork.sendPacketToEveryone(new PacketTemperature(getTemperature()));
        // }
    }
}