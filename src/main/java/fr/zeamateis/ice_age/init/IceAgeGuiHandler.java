package fr.zeamateis.ice_age.init;

import fr.zeamateis.ice_age.client.gui.GuiFrozenPlayerInventory;
import fr.zeamateis.ice_age.common.IceAgeMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;

public class IceAgeGuiHandler {

    public static void register() {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> {
            return (openContainer) -> {
                ResourceLocation location = openContainer.getId();
                if (location.toString().equals(IceAgeMod.MODID + ":gui_dead_player")) {
                    EntityPlayerSP player = Minecraft.getInstance().player;
                    return new GuiFrozenPlayerInventory(player.inventory, player.world, openContainer.getAdditionalData().readInt());
                }
                return null;
            };
        });
    }
}
