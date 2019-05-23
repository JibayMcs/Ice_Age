package fr.zeamateis.ice_age.network.packet;

import fr.zeamateis.amy.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketNumericParticles implements IPacket<PacketNumericParticles> {

    private float damageAmount;
    private double x, y, z;
    private double xSpeed, ySpeed, zSpeed;
    private int color;

    public PacketNumericParticles() {
    }

    private PacketNumericParticles(float damageAmount, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, int colorIn) {
        this.damageAmount = damageAmount;
        this.x = x;
        this.y = y;
        this.z = z;
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.zSpeed = zSpeed;
        color = colorIn;
    }

    @Override
    public void encode(PacketNumericParticles packet, PacketBuffer buffer) {
        buffer.writeFloat(packet.damageAmount);
        buffer.writeDouble(packet.x);
        buffer.writeDouble(packet.y);
        buffer.writeDouble(packet.z);
        buffer.writeDouble(packet.xSpeed);
        buffer.writeDouble(packet.ySpeed);
        buffer.writeDouble(packet.zSpeed);
        buffer.writeInt(packet.color);
    }

    @Override
    public PacketNumericParticles decode(PacketBuffer buffer) {
        return new PacketNumericParticles(buffer.readFloat(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readDouble(), buffer.readInt());
    }

    @Override
    public void handle(PacketNumericParticles packet, Supplier<NetworkEvent.Context> ctxProvider) {
        if (ctxProvider.get().getSender() != null) {
           /* WorldClient world = Minecraft.getInstance().world;

            NumericParticleType damageParticle = IceAgeParticles.DAMAGE;
            damageParticle.setNumber(packet.damageAmount);
            damageParticle.setColor(packet.color);

            world.addParticle(damageParticle, packet.x, packet.y, packet.z, packet.xSpeed, packet.ySpeed, packet.zSpeed);*/
        }

        ctxProvider.get().setPacketHandled(true);
    }

}