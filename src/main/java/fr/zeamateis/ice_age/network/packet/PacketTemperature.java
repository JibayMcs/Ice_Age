package fr.zeamateis.ice_age.network.packet;

import fr.zeamateis.amy.network.IPacket;
import fr.zeamateis.ice_age.common.capability.player.temperature.CapabilityTemperature;
import fr.zeamateis.ice_age.common.capability.player.temperature.ITemperature;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketTemperature implements IPacket<PacketTemperature> {

    private float temperature;

    public PacketTemperature() {
    }

    public PacketTemperature(float temperatureIn) {
        temperature = temperatureIn;
    }

    @Override
    public void encode(PacketTemperature packet, PacketBuffer buffer) {
        buffer.writeFloat(packet.temperature);
    }

    @Override
    public PacketTemperature decode(PacketBuffer buffer) {
        return new PacketTemperature(buffer.readFloat());
    }

    @Override
    public void handle(PacketTemperature packet, Supplier<NetworkEvent.Context> ctxProvider) {
        if (ctxProvider.get().getSender() != null) {
            ITemperature temperature = (ITemperature) ctxProvider.get().getSender().getCapability(CapabilityTemperature.TEMPERATURE_CAPABILITY);
            temperature.set(packet.temperature);
        }

        ctxProvider.get().setPacketHandled(true);
    }


}