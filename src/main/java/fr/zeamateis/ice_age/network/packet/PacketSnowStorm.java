package fr.zeamateis.ice_age.network.packet;

import fr.zeamateis.amy.network.IPacket;
import fr.zeamateis.ice_age.common.world.DayCounterWorldSavedData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSnowStorm implements IPacket<PacketSnowStorm> {

    private boolean snowStorm;

    public PacketSnowStorm() {
    }

    public PacketSnowStorm(boolean snowStormIn) {
        snowStorm = snowStormIn;
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(PacketSnowStorm packet) {
        EntityPlayerSP player = Minecraft.getInstance().player;
        World world = player.world;
        DayCounterWorldSavedData.get(world).setSnowStorm(packet.snowStorm);
    }

    @Override
    public void encode(PacketSnowStorm packet, PacketBuffer buffer) {
        buffer.writeBoolean(packet.snowStorm);
    }

    @Override
    public PacketSnowStorm decode(PacketBuffer buffer) {
        return new PacketSnowStorm(buffer.readBoolean());
    }

    @Override
    public void handle(PacketSnowStorm packet, Supplier<NetworkEvent.Context> ctxProvider) {
        ctxProvider.get().enqueueWork(() -> handleClient(packet));
        ctxProvider.get().setPacketHandled(true);
    }

}