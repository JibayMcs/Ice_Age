package fr.zeamateis.ice_age.client.particle;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ParticleNumeric extends Particle {

	private double number;
	private int customColor;

	protected ParticleNumeric(World worldIn, double p_i1211_2_, double p_i1211_4_, double p_i1211_6_, double p_i1211_8_, double p_i1211_10_, double p_i1211_12_) {
		this(0, worldIn, p_i1211_2_, p_i1211_4_, p_i1211_6_, p_i1211_8_, p_i1211_10_, p_i1211_12_, 0);
	}

	public ParticleNumeric(double numberIn, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double p_i46354_8_, double p_i46354_10_, double p_i46354_12_, int colorIn) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, 0.0D, 0.0D, 0.0D);
		this.particleTextureJitterX = 0.0F;
		this.particleTextureJitterY = 0.0F;
		this.particleGravity = 0.1F;
		this.particleScale = 3.0F;
		this.maxAge = 12;
		this.number = numberIn;
		this.customColor = colorIn;
	}

	/**
	 * Renders the particle
	 */
	@Override
	public void renderParticle(BufferBuilder buffer, Entity entityIn, float partialTicks, float rotationX, float rotationZ, float rotationYZ, float rotationXY, float rotationXZ) {
		float rotationYaw = (-Minecraft.getInstance().player.rotationYaw);
		float rotationPitch = Minecraft.getInstance().player.rotationPitch;

		double posX = (this.prevPosX + (this.posX - this.prevPosX) * partialTicks - interpPosX);
		double posY = (this.prevPosY + (this.posY - this.prevPosY) * partialTicks - interpPosY);
		double posZ = (this.prevPosZ + (this.posZ - this.prevPosZ) * partialTicks - interpPosZ);

		GL11.glPushMatrix();
		GL11.glDepthFunc(519);

		GL11.glTranslated(posX, posY - 0.5F, posZ);
		GL11.glRotatef(rotationYaw, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(rotationPitch, 1.0F, 0.0F, 0.0F);

		GL11.glScaled(-1.0, -1.0, 1.0);
		GL11.glScaled(this.particleScale * 0.01D, this.particleScale * 0.01D, this.particleScale * 0.01D);
		GL11.glScaled(1.0, 1.0, 1.0);

		OpenGlHelper.glMultiTexCoord2f(OpenGlHelper.GL_TEXTURE1, 240.0F, 0.0F);
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glDepthMask(true);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(3553);
		GL11.glEnable(2929);
		GL11.glDisable(2896);
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(3042);
		GL11.glEnable(3008);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		final FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
		fontRenderer.drawStringWithShadow(String.valueOf((int) this.number), 0, 0, this.customColor);

		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glDepthFunc(515);

		GL11.glPopMatrix();
		this.particleScale *= 1F;
	}

	public void tick() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		if (this.age++ >= this.maxAge) {
			this.setExpired();
		}

		this.prevParticleAngle = this.particleAngle;
		this.particleAngle += (float) Math.PI * 0.2 * 2.0F;
		if (this.onGround) {
			this.prevParticleAngle = this.particleAngle = 0.0F;
		}

		this.move(this.motionX, this.motionY, this.motionZ);
		this.motionY -= (double) 0.003F;
		this.motionY = Math.max(this.motionY, (double) -0.14F);
	}

	public int getFXLayer() {
		return 3;
	}

	public void setNumber(double numberIn) {
		this.number = numberIn;
	}

}