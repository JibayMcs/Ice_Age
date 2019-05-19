package fr.zeamateis.ice_age.client.render.world;

import fr.zeamateis.ice_age.common.world.DayCounterWorldSavedData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

@OnlyIn(Dist.CLIENT)
public class IceAgeSkyRenderer implements IRenderHandler {

    private static final ResourceLocation MOON_PHASES_TEXTURES = new ResourceLocation("textures/environment/moon_phases.png");
    private static final ResourceLocation SUN_TEXTURES = new ResourceLocation("textures/environment/sun.png");

    private final int starGLCallList;

    private final int glSkyList;
    private final int glSkyList2;

    private final VertexBuffer starVBO;
    private final VertexBuffer skyVBO;
    private final VertexBuffer sky2VBO;

    private final VertexFormat vertexBufferFormat;

    private final boolean vboEnabled;

    public IceAgeSkyRenderer() {
        starGLCallList = ObfuscationReflectionHelper.getPrivateValue(WorldRenderer.class, Minecraft.getInstance().worldRenderer, "starGLCallList");
        glSkyList = ObfuscationReflectionHelper.getPrivateValue(WorldRenderer.class, Minecraft.getInstance().worldRenderer, "glSkyList");
        glSkyList2 = ObfuscationReflectionHelper.getPrivateValue(WorldRenderer.class, Minecraft.getInstance().worldRenderer, "glSkyList2");

        starVBO = ObfuscationReflectionHelper.getPrivateValue(WorldRenderer.class, Minecraft.getInstance().worldRenderer, "starVBO");
        skyVBO = ObfuscationReflectionHelper.getPrivateValue(WorldRenderer.class, Minecraft.getInstance().worldRenderer, "skyVBO");
        sky2VBO = ObfuscationReflectionHelper.getPrivateValue(WorldRenderer.class, Minecraft.getInstance().worldRenderer, "sky2VBO");

        vboEnabled = ObfuscationReflectionHelper.getPrivateValue(WorldRenderer.class, Minecraft.getInstance().worldRenderer, "vboEnabled");

        vertexBufferFormat = new VertexFormat();
        vertexBufferFormat.addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));

    }


    @Override
    public void render(float partialTicks, WorldClient world, Minecraft mc) {
        GlStateManager.disableTexture2D();
        Vec3d vec3d = world.getSkyColor(mc.getRenderViewEntity(), partialTicks);
        float f = (float) vec3d.x;
        float f1 = (float) vec3d.y;
        float f2 = (float) vec3d.z;
        GlStateManager.color3f(f, f1, f2);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.depthMask(false);
        GlStateManager.enableFog();
        if (vboEnabled) {
            skyVBO.bindBuffer();
            GlStateManager.enableClientState(32884);
            GlStateManager.vertexPointer(3, 5126, 12, 0);
            skyVBO.drawArrays(7);
            skyVBO.unbindBuffer();
            GlStateManager.disableClientState(32884);
        } else {
            GlStateManager.callList(glSkyList);
        }

        GlStateManager.disableFog();
        GlStateManager.disableAlphaTest();
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderHelper.disableStandardItemLighting();
        float[] afloat = world.dimension.calcSunriseSunsetColors(world.getCelestialAngle(partialTicks), partialTicks);
        if (afloat != null) {
            GlStateManager.disableTexture2D();
            GlStateManager.shadeModel(7425);
            GlStateManager.pushMatrix();
            GlStateManager.rotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotatef(MathHelper.sin(world.getCelestialAngleRadians(partialTicks)) < 0.0F ? 180.0F : 0.0F, 0.0F, 0.0F, 1.0F);
            GlStateManager.rotatef(90.0F, 0.0F, 0.0F, 1.0F);
            float f3 = afloat[0];
            float f4 = afloat[1];
            float f5 = afloat[2];
            bufferbuilder.begin(6, DefaultVertexFormats.POSITION_COLOR);
            bufferbuilder.pos(0.0D, 100.0D, 0.0D).color(f3, f4, f5, afloat[3]).endVertex();
            int i = 16;

            for (int j = 0; j <= 16; ++j) {
                float f6 = (float) j * ((float) Math.PI * 2F) / 16.0F;
                float f7 = MathHelper.sin(f6);
                float f8 = MathHelper.cos(f6);
                bufferbuilder.pos((double) (f7 * 120.0F), (double) (f8 * 120.0F), (double) (-f8 * 40.0F * afloat[3])).color(afloat[0], afloat[1], afloat[2], 0.0F).endVertex();
            }

            tessellator.draw();
            GlStateManager.popMatrix();
            GlStateManager.shadeModel(7424);
        }

        GlStateManager.enableTexture2D();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.pushMatrix();
        float f11 = 1.0F - world.getRainStrength(partialTicks);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, f11);
        GlStateManager.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotatef(world.getCelestialAngle(partialTicks) * 360.0F, 1.0F, 0.0F, 0.0F);
        float f12 = 0F;


        switch (DayCounterWorldSavedData.get(world).getAgeInDays()) {
            case 0:
                f12 = 30F;
                GlStateManager.color4f(255F / 255F, 255F / 255F, 255F / 255F, 1.0F);
                break;
            case 5:
                f12 = 20F;
                GlStateManager.color4f(127F / 255F, 127F / 255F, 127F / 255F, 1F);
                break;
            case 10:
                f12 = 15F;
                GlStateManager.color4f(63F / 255F, 63F / 255F, 63F / 255F, 1F);
                break;
            case 15:
                f12 = 10F;
                GlStateManager.color4f(1F, 1F, 1F, 0.1F);
                break;
        }


        mc.getTextureManager().bindTexture(SUN_TEXTURES);
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double) (-f12), 100.0D, (double) (-f12)).tex(0.0D, 0.0D).endVertex();
        bufferbuilder.pos((double) f12, 100.0D, (double) (-f12)).tex(1.0D, 0.0D).endVertex();
        bufferbuilder.pos((double) f12, 100.0D, (double) f12).tex(1.0D, 1.0D).endVertex();
        bufferbuilder.pos((double) (-f12), 100.0D, (double) f12).tex(0.0D, 1.0D).endVertex();
        tessellator.draw();
        f12 = 20.0F;
        mc.getTextureManager().bindTexture(MOON_PHASES_TEXTURES);
        int k = world.getMoonPhase();
        int l = k % 4;
        int i1 = k / 4 % 2;
        float f13 = (float) (l + 0) / 4.0F;
        float f14 = (float) (i1 + 0) / 2.0F;
        float f15 = (float) (l + 1) / 4.0F;
        float f9 = (float) (i1 + 1) / 2.0F;
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos((double) (-f12), -100.0D, (double) f12).tex((double) f15, (double) f9).endVertex();
        bufferbuilder.pos((double) f12, -100.0D, (double) f12).tex((double) f13, (double) f9).endVertex();
        bufferbuilder.pos((double) f12, -100.0D, (double) (-f12)).tex((double) f13, (double) f14).endVertex();
        bufferbuilder.pos((double) (-f12), -100.0D, (double) (-f12)).tex((double) f15, (double) f14).endVertex();
        tessellator.draw();
        GlStateManager.disableTexture2D();
        float f10 = world.getStarBrightness(partialTicks) * f11;
        if (f10 > 0.0F) {
            GlStateManager.color4f(f10, f10, f10, f10);
            if (vboEnabled) {
                starVBO.bindBuffer();
                GlStateManager.enableClientState(32884);
                GlStateManager.vertexPointer(3, 5126, 12, 0);
                starVBO.drawArrays(7);
                starVBO.unbindBuffer();
                GlStateManager.disableClientState(32884);
            } else {
                GlStateManager.callList(starGLCallList);
            }
        }

        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.enableFog();
        GlStateManager.popMatrix();
        GlStateManager.disableTexture2D();
        GlStateManager.color3f(0.0F, 0.0F, 0.0F);
        double d0 = mc.player.getEyePosition(partialTicks).y - world.getHorizon();
        if (d0 < 0.0D) {
            GlStateManager.pushMatrix();
            GlStateManager.translatef(0.0F, 12.0F, 0.0F);
            if (vboEnabled) {
                sky2VBO.bindBuffer();
                GlStateManager.enableClientState(32884);
                GlStateManager.vertexPointer(3, 5126, 12, 0);
                sky2VBO.drawArrays(7);
                sky2VBO.unbindBuffer();
                GlStateManager.disableClientState(32884);
            } else {
                GlStateManager.callList(glSkyList2);
            }

            GlStateManager.popMatrix();
        }

        if (world.dimension.isSkyColored()) {
            GlStateManager.color3f(f * 0.2F + 0.04F, f1 * 0.2F + 0.04F, f2 * 0.6F + 0.1F);
        } else {
            GlStateManager.color3f(f, f1, f2);
        }

        GlStateManager.pushMatrix();
        GlStateManager.translatef(0.0F, -((float) (d0 - 16.0D)), 0.0F);
        GlStateManager.callList(glSkyList2);
        GlStateManager.popMatrix();
        GlStateManager.enableTexture2D();
        GlStateManager.depthMask(true);
    }
}
