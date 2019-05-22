package fr.zeamateis.amy.resources;

import fr.zeamateis.ice_age.common.IceAgeMod;
import net.minecraft.util.ResourceLocation;

public class ResourceBuilder {

    public static <T extends ResourceLocation> ResourceLocation build(String resourceNameIn) {
        return new ResourceLocation(IceAgeMod.MODID, resourceNameIn);
    }


    public static <T> T Null() {
        return null;
    }
}
