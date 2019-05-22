package fr.zeamateis.ice_age.client.render.entity;

import com.mojang.util.UUIDTypeAdapter;
import fr.zeamateis.ice_age.client.render.entity.model.ModelPlayerCorpse;
import fr.zeamateis.ice_age.common.entity.EntityFrozenDeadPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class RenderFrozenPlayer extends Render<EntityFrozenDeadPlayer> {

    private final ModelPlayerCorpse model = new ModelPlayerCorpse(0.0F, 64, 64);
    private Random random = new Random();

    public RenderFrozenPlayer(RenderManager renderManagerIn) {
        super(renderManagerIn);
    }

    private void preRenderTransparentBlocks() {
        Minecraft.getInstance().gameRenderer.enableLightmap();
        GlStateManager.pushMatrix();
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }
        GlStateManager.disableLighting();
        GlStateManager.enableCull();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableAlphaTest();
        GlStateManager.enableDepthTest();
        GlStateManager.depthMask(false);
    }

    private void postRenderTransparentBlocks() {
        GlStateManager.popMatrix();
        GlStateManager.enableLighting();
        GlStateManager.disableCull();
        GlStateManager.disableBlend();
        GlStateManager.enableAlphaTest();
        GlStateManager.depthMask(true);
        Minecraft.getInstance().gameRenderer.disableLightmap();
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityFrozenDeadPlayer entity) {
        ResourceLocation r = AbstractClientPlayer.getLocationSkin(entity.getUsername());
        if (r == null) {
            r = DefaultPlayerSkin.getDefaultSkin(UUIDTypeAdapter.fromString(entity.getUUID()));
        }
        AbstractClientPlayer.getDownloadImageSkin(r, entity.getUsername());
        return r;
    }

    @Override
    public void doRender(EntityFrozenDeadPlayer entity, double x, double y, double z, float yaw, float tick) {
        renderDeadPlayer(entity, x, y, z, yaw, tick);
    }

    private void renderDeadPlayer(EntityFrozenDeadPlayer deadPlayer, double x, double y, double z, float yaw, float partialTicks) {

        /*for (ItemStack armorStack : deadPlayer.getArmorInventoryList()) {
            if (armorStack.getItem() instanceof ItemArmor) {
            }
        }*/


        GlStateManager.pushMatrix();
        GlStateManager.color4f(0.2F, 0.2F, 1.0F, 1.0F);
        bindEntityTexture(deadPlayer);
        GlStateManager.translated(x, y + 1.6F, z);
        GlStateManager.rotatef(-180.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotatef(90.0F, 0.0F, 1.0F, 0.0F);
        model.render(deadPlayer, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F);
        GlStateManager.popMatrix();

        preRenderTransparentBlocks();
        renderIceBlock(deadPlayer, x, y + 0.5D, z);
        renderIceBlock(deadPlayer, x, y + 1.5D, z);
        postRenderTransparentBlocks();
    }

    //TODO Fix render block bug
    private void renderIceBlock(EntityFrozenDeadPlayer deadPlayer, double x, double y, double z) {
        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableCull();
        GlStateManager.enableAlphaTest();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.depthMask(false);
        GlStateManager.translated(x, y, z);
        GlStateManager.rotatef(deadPlayer.getRotationYawHead(), 0, 1, 0);
        GlStateManager.rotatef(-90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.translatef(-0.5F, -0.5F, 0.5F);
        bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        Minecraft.getInstance().gameRenderer.enableLightmap();
        Minecraft.getInstance().getBlockRendererDispatcher().renderBlockBrightness(Blocks.ICE.getDefaultState(), 1.0F);
        Minecraft.getInstance().gameRenderer.disableLightmap();
        GlStateManager.translatef(0.0F, 0.0F, 1.0F);
        GlStateManager.enableCull();
        GlStateManager.popMatrix();
    }

}
