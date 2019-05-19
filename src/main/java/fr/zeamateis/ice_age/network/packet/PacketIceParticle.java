package fr.zeamateis.ice_age.network.packet;

import fr.zeamateis.amy.network.IPacket;
import fr.zeamateis.ice_age.client.particle.type.IceParticleType;
import fr.zeamateis.ice_age.init.IceAgeParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketIceParticle implements IPacket<PacketIceParticle> {

    private double x, y, z;
    private double xSpeed, ySpeed, zSpeed;

    private double randomRotX, randomRotY, randomRotZ;

    public PacketIceParticle() {
    }

    public PacketIceParticle(double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, double randomRotX, double randomRotY, double randomRotZ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
        this.randomRotX = randomRotX;
        this.randomRotY = randomRotY;
        this.randomRotZ = randomRotZ;
    }

    @Override
    public void encode(PacketIceParticle packet, PacketBuffer buffer) {
        buffer.writeDouble(packet.x);
        buffer.writeDouble(packet.y);
        buffer.writeDouble(packet.z);
        buffer.writeDouble(packet.xSpeed);
        buffer.writeDouble(packet.ySpeed);
        buffer.writeDouble(packet.zSpeed);
        buffer.writeDouble(packet.randomRotX);
        buffer.writeDouble(packet.randomRotY);
        buffer.writeDouble(packet.randomRotZ);
    }

    @Override
    public PacketIceParticle decode(PacketBuffer buffer) {
        return new PacketIceParticle(buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble());
    }

    @Override
    public void handle(PacketIceParticle packet, Supplier<NetworkEvent.Context> ctxProvider) {
        if (ctxProvider.get().getSender() != null) {
            WorldClient world = Minecraft.getInstance().world;

            IceParticleType iceParticleType = IceAgeParticles.ICE;

            iceParticleType.setRandomRotX(packet.randomRotX);
            iceParticleType.setRandomRotY(packet.randomRotY);
            iceParticleType.setRandomRotZ(packet.randomRotZ);

            world.addParticle(IceAgeParticles.ICE, packet.x, packet.y, packet.z, packet.xSpeed, packet.ySpeed, packet.zSpeed);
        }

        ctxProvider.get().setPacketHandled(true);
    }

}