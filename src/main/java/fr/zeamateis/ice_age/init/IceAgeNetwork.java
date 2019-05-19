package fr.zeamateis.ice_age.init;

import fr.zeamateis.amy.network.AmyNetwork;
import fr.zeamateis.ice_age.network.packet.PacketIceParticle;
import fr.zeamateis.ice_age.network.packet.PacketSnowStorm;
import fr.zeamateis.ice_age.network.packet.PacketTemperature;
import fr.zeamateis.ice_age.network.packet.PacketWorldDay;

public class IceAgeNetwork {

    public static void register() {
        PacketWorldDay dayCounter = new PacketWorldDay();
        AmyNetwork.messageBuilder(PacketWorldDay.class).encoder(dayCounter::encode).decoder(dayCounter::decode).consumer(dayCounter::handle).add();

        PacketSnowStorm snowStorm = new PacketSnowStorm();
        AmyNetwork.messageBuilder(PacketSnowStorm.class).encoder(snowStorm::encode).decoder(snowStorm::decode).consumer(snowStorm::handle).add();

        PacketTemperature temperature = new PacketTemperature();
        AmyNetwork.messageBuilder(PacketTemperature.class).encoder(temperature::encode).decoder(temperature::decode).consumer(temperature::handle).add();

        PacketIceParticle iceParticle = new PacketIceParticle();
        AmyNetwork.messageBuilder(PacketIceParticle.class).encoder(iceParticle::encode).decoder(iceParticle::decode).consumer(iceParticle::handle).add();

    }
}
