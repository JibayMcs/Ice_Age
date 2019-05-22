package fr.zeamateis.ice_age.client.render.entity.model;

import fr.zeamateis.ice_age.common.entity.EntityFrozenDeadPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author MFF Team
 */
@OnlyIn(Dist.CLIENT)
public class ModelPlayerCorpse extends ModelBiped {


    public ModelPlayerCorpse(float p_i1168_1_, int textureWidth, int textureHeight) {
        super(p_i1168_1_, 0.0F, textureWidth, textureHeight);
    }

    @Override
    public void render(Entity entityIn, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float sizeIn) {

        if (entityIn instanceof EntityFrozenDeadPlayer) {

            EntityFrozenDeadPlayer entityPlayerCorpse = (EntityFrozenDeadPlayer) entityIn;

            GlStateManager.pushMatrix();

            float[] deathRotationAngles = entityPlayerCorpse.getRenderRotation();

            if (deathRotationAngles != null) {
                bipedHead.rotateAngleY = deathRotationAngles[0];
                bipedHeadwear.rotateAngleY = bipedHead.rotateAngleY;
                bipedLeftArm.rotateAngleY = deathRotationAngles[1];
                bipedLeftArm.rotateAngleZ = deathRotationAngles[2];
                bipedRightArm.rotateAngleY = deathRotationAngles[3];
                bipedRightArm.rotateAngleZ = deathRotationAngles[4];
                bipedLeftLeg.rotateAngleY = deathRotationAngles[5];
                bipedLeftLeg.rotateAngleZ = deathRotationAngles[6];
                bipedRightLeg.rotateAngleY = deathRotationAngles[7];
                bipedRightLeg.rotateAngleZ = deathRotationAngles[8];
            }

            bipedHead.render(sizeIn);
            bipedBody.render(sizeIn);
            bipedRightArm.render(sizeIn);
            bipedLeftArm.render(sizeIn);
            bipedRightLeg.render(sizeIn);
            bipedLeftLeg.render(sizeIn);
            bipedHeadwear.render(sizeIn);


            GlStateManager.popMatrix();
        }
    }
}