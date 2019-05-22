package fr.zeamateis.ice_age.client.gui;

import fr.zeamateis.amy.resources.ResourceBuilder;
import fr.zeamateis.ice_age.common.capability.player.temperature.CapabilityTemperature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

@OnlyIn(Dist.CLIENT)
public class GuiTemperature extends GuiIngame {

    private static final ResourceLocation ICONS = ResourceBuilder.build("textures/gui/icons_temperature.png");


    public GuiTemperature() {
        super(Minecraft.getInstance());
    }

    public void renderGameOverlay(RenderGameOverlayEvent eventIn) {
        EntityPlayer entityplayer = getPlayer();
        if (entityplayer != null) {
            entityplayer.getCapability(CapabilityTemperature.TEMPERATURE_CAPABILITY).ifPresent(temperature -> {

                if (eventIn.getType() == RenderGameOverlayEvent.ElementType.HEALTH) {
                    mc.getTextureManager().bindTexture(ICONS);

                    if (!(entityplayer.getRidingEntity() instanceof EntityHorse)) {
                        int thirstLevel = (int) temperature.getTemperature();
                        int xStart = mc.mainWindow.getScaledWidth() / 2 + 10;
                        int yStart = mc.mainWindow.getScaledHeight() - 50;

                        for (int i = 0; i < 10; i++) {
                            drawTexturedModalRect(xStart + i * 8, yStart, 1, 1, 7, 10);
                            if (thirstLevel % 2 != 0 && 10 - i - 1 == thirstLevel / 2) {
                                drawTexturedModalRect(xStart + i * 8, yStart, 17, 1, 7, 10);
                            } else if (thirstLevel / 2 >= 10 - i) {
                                drawTexturedModalRect(xStart + i * 8, yStart, 9, 1, 7, 10);
                            }
                        }
                    }
                    mc.getTextureManager().bindTexture(Gui.ICONS);
                } else if (eventIn.getType() == RenderGameOverlayEvent.ElementType.AIR) {
                    if (eventIn.isCancelable())
                        eventIn.setCanceled(true);

                    int xStart = (mc.mainWindow.getScaledWidth() / 2) + 91;
                    int yStart = mc.mainWindow.getScaledHeight() - 49;
                    int xModifier = 0;
                    int yModifier = 0;

                    int armorLevel = entityplayer.getTotalArmorValue();

                    if (!(entityplayer.getRidingEntity() instanceof EntityHorse)) {
                        if (armorLevel > 0) {
                            yModifier = -11;
                        } else {
                            xModifier = -102;
                        }
                    }

                    if (entityplayer.areEyesInFluid(FluidTags.WATER)) {
                        int air = entityplayer.getAir();
                        int full = MathHelper.ceil(((air - 2) * 10.0D) / 300.0D);
                        int partial = MathHelper.ceil((air * 10.0D) / 300.0D) - full;

                        for (int i = 0; i < (full + partial); ++i) {
                            drawTexturedModalRect((xStart - (i * 8) - 9) + xModifier, yStart + yModifier, (i < full ? 16 : 25), 18, 9, 9);
                        }
                    }
                }
            });
        }
    }


    private EntityPlayer getPlayer() {
        return !(mc.getRenderViewEntity() instanceof EntityPlayer) ? null : (EntityPlayer) mc.getRenderViewEntity();
    }

}