package fr.zeamateis.amy.network;

import fr.zeamateis.ice_age.common.IceAgeMod;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class AmyNetwork {

    private static final String PROTOCOL_VERSION = String.valueOf(1);
    private static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(IceAgeMod.MODID, "ice_age_channel")).networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals).serverAcceptedVersions(PROTOCOL_VERSION::equals).simpleChannel();

    private static int id = 0;

    public static SimpleChannel getNetworkChannel() {
        return CHANNEL;
    }

    public static <M> SimpleChannel.MessageBuilder<M> messageBuilder(Class<M> packetIn) {
        System.out.println(id);
        return getNetworkChannel().messageBuilder(packetIn, id++);
    }

    public static void sendPacketToServer(IPacket<?> packetIn) {
        CHANNEL.sendToServer(packetIn);
    }

    public static void sendPacketTo(EntityPlayerMP player, IPacket<?> packetIn) {
        CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), packetIn);
    }

    public static void sendPacketToEveryone(IPacket<?> packetIn) {
        CHANNEL.send(PacketDistributor.ALL.noArg(), packetIn);
    }

    public static void sendPacketToDimension(DimensionType dimension, IPacket<?> packetIn) {
        CHANNEL.send(PacketDistributor.DIMENSION.with(() -> dimension), packetIn);
    }
}