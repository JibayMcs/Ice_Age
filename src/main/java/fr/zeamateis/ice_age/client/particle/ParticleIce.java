package fr.zeamateis.ice_age.client.particle;

import fr.zeamateis.ice_age.client.particle.type.IceParticleType;
import fr.zeamateis.ice_age.init.IceAgeParticles;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class ParticleIce extends Particle implements IParticleData {

    private double randomRotX, randomRotY, randomRotZ;

    private BlockPos particlePos;

    public ParticleIce(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double speedX, double speedY,
                       double speedZ, double randomRotX, double randomRotY, double randomRotZ) {
        super(worldIn, xCoordIn, yCoordIn, zCoordIn, speedX, speedY, speedZ);
        particleGravity = 1.0F;
        particleScale = 3.0F;
        maxAge = (int) (64.0D / (Math.random() * 0.8D + 0.2D));
        this.randomRotX = randomRotX;
        this.randomRotY = randomRotY;
        this.randomRotZ = randomRotZ;
        particleAlpha = 0.5F;
        particlePos = new BlockPos(xCoordIn, yCoordIn, zCoordIn);
    }

    /**
     * Renders the particle
     */
    @Override
    public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX,
                               float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {

        if (isExpired)
            return;

        double posX = (prevPosX + (this.posX - prevPosX) * partialTicks - interpPosX);
        double posY = (prevPosY + (this.posY - prevPosY) * partialTicks - interpPosY);
        double posZ = (prevPosZ + (this.posZ - prevPosZ) * partialTicks - interpPosZ);

        Minecraft.getInstance().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

        GlStateManager.pushMatrix();
        buffer.setTranslation(-this.posX, -this.posY, -this.posZ);

        GlStateManager.translated(posX, posY + 0.25D, posZ);
        GlStateManager.rotated(randomRotX, 1.0D, 0.0D, 0.0D);
        GlStateManager.rotated(randomRotY, 0.0D, 1.0D, 0.0D);
        GlStateManager.rotated(randomRotZ, 0.0D, 0.0D, 1.0D);

        GlStateManager.scaled(-1.0, -1.0, 1.0);
        GlStateManager.scaled(particleScale * 0.08D, particleScale * 0.08D, particleScale * 0.08D);
        GlStateManager.scaled(1.0, 1.0, 1.0);

        GlStateManager.pushMatrix();

        buffer.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        GlStateManager.enableCull();
        GlStateManager.enableColorMaterial();
        GL11.glColorMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT_AND_DIFFUSE);

        RenderHelper.enableStandardItemLighting();
        Minecraft.getInstance().gameRenderer.enableLightmap();
        Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().renderModelFlat(world, Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelShapes().getModel(Blocks.ICE.getDefaultState()), Blocks.ICE.getDefaultState(), new BlockPos(this.posX, this.posY, this.posZ), buffer, false, rand, -1L);
        Minecraft.getInstance().gameRenderer.disableLightmap();
        RenderHelper.disableStandardItemLighting();

        //buffer.setTranslation(0, 0, 0);
        GlStateManager.disableCull();
        Tessellator.getInstance().draw();
        GlStateManager.popMatrix();

        GlStateManager.popMatrix();

    }

    @Override
    public void tick() {
        prevPosX = posX;
        prevPosY = posY;
        prevPosZ = posZ;
        if (age++ >= maxAge) {
            setExpired();
        }

        motionY -= 0.03D;
        move(motionX, motionY, motionZ);
        motionX *= 0.9990000128746033D;
        motionY *= 0.9990000128746033D;
        motionZ *= 0.9990000128746033D;
        if (onGround) {
            motionX *= 0.699999988079071D;
            motionZ *= 0.699999988079071D;
            particleScale -= 0.01D;

            if (particleScale <= 0)
                setExpired();
        }
    }

    @Override
    public int getFXLayer() {
        return 3;
    }


    @Override
    public ParticleType<?> getType() {
        return IceAgeParticles.ICE.getType();
    }

    @Override
    public void write(PacketBuffer packetBuffer) {

    }

    @Override
    public String getParameters() {
        return IceAgeParticles.ICE.getParameters();
    }

    @OnlyIn(Dist.CLIENT)
    public static class Factory implements IParticleFactory<IceParticleType> {
        @Override
        public Particle makeParticle(IceParticleType typeIn, World worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ParticleIce(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, typeIn.getRandomRotX(), typeIn.getRandomRotY(), typeIn.getRandomRotZ());
        }
    }
}