package fr.zeamateis.ice_age.network.packet;

import fr.zeamateis.amy.network.IPacket;
import fr.zeamateis.ice_age.client.render.world.IceAgeSkyRenderer;
import fr.zeamateis.ice_age.client.render.world.IceAgeWeatherRenderer;
import fr.zeamateis.ice_age.common.IceAgeMod;
import fr.zeamateis.ice_age.common.world.DayCounterWorldSavedData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketWorldDay implements IPacket<PacketWorldDay> {

    private int worldTime;

    public PacketWorldDay() {
    }

    public PacketWorldDay(int worldTimeIn) {
        worldTime = worldTimeIn;
    }

    @OnlyIn(Dist.CLIENT)
    private static void handleClient(PacketWorldDay packet) {
        EntityPlayerSP player = Minecraft.getInstance().player;
        World world = player.world;

        System.out.println("WORLD TIME AGE " + packet.worldTime);
        DayCounterWorldSavedData.get(world).setAgeInDays(packet.worldTime);

        IRenderHandler skyRenderer = world.getDimension().getSkyRenderer();
        IRenderHandler weatherRenderer = world.getDimension().getWeatherRenderer();

        if (world != null) {

            if (world.getDimension().isSurfaceWorld()) {
                if (skyRenderer == null) {
                    IceAgeMod.getLogger().info("Setting sky renderer for dimension {}", world.getDimension().getType());
                    world.getDimension().setSkyRenderer(new IceAgeSkyRenderer());
                }
                if (weatherRenderer == null) {
                    IceAgeMod.getLogger().info("Setting weather renderer for dimension {}", world.getDimension().getType());
                    world.getDimension().setWeatherRenderer(new IceAgeWeatherRenderer());
                } else {
                    IceAgeMod.getLogger().info("Not hooking sky and weather renderer for dimension {} {}", world.getDimension().getType(), skyRenderer.getClass());
                }
            }
        }

    }

    @Override
    public void encode(PacketWorldDay packet, PacketBuffer buffer) {
        buffer.writeInt(packet.worldTime);
    }

    @Override
    public PacketWorldDay decode(PacketBuffer buffer) {
        return new PacketWorldDay(buffer.readInt());
    }

    @Override
    public void handle(PacketWorldDay packet, Supplier<NetworkEvent.Context> ctxProvider) {
        ctxProvider.get().enqueueWork(() -> handleClient(packet));
        ctxProvider.get().setPacketHandled(true);
    }

}