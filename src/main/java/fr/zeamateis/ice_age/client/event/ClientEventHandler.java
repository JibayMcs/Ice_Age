package fr.zeamateis.ice_age.client.event;

import fr.zeamateis.ice_age.client.gui.GuiTemperature;
import fr.zeamateis.ice_age.common.IceAgeMod;
import fr.zeamateis.ice_age.common.world.DayCounterWorldSavedData;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@EventBusSubscriber(modid = IceAgeMod.MODID, bus = EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {

    private static final GuiTemperature guiTemperature;
    public static int rendererUpdateCount;

    static {
        guiTemperature = new GuiTemperature();
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGameOverlayEvent event) {
        guiTemperature.renderGameOverlay(event);
    }

    //TODO change fog if player is only outside
    @SubscribeEvent
    public static void onFogDensity(EntityViewRenderEvent.FogDensity event) {
        if (DayCounterWorldSavedData.get(event.getEntity().world).isSnowStorm()) {
            GlStateManager.fogMode(GlStateManager.FogMode.EXP);
            event.setDensity(0.125F);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onClientTickEvent(TickEvent.ClientTickEvent event) {
        ++rendererUpdateCount;
    }

    @SubscribeEvent
    public static void onRenderWorld(RenderWorldLastEvent event) {
        // new IceAgeSkyRenderer().render(event.getPartialTicks(), Minecraft.getInstance().world, Minecraft.getInstance());
    }

    @SubscribeEvent
    public static void onWorldLoad(WorldEvent.Load e) {

    }
}