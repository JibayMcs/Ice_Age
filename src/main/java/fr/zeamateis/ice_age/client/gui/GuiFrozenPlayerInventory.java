package fr.zeamateis.ice_age.client.gui;

import fr.zeamateis.amy.resources.ResourceBuilder;
import fr.zeamateis.ice_age.common.container.ContainerFrozenPlayer;
import fr.zeamateis.ice_age.common.entity.EntityFrozenDeadPlayer;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author MFF Team
 */
@OnlyIn(Dist.CLIENT)
public class GuiFrozenPlayerInventory extends GuiContainer {
    private final ResourceLocation tex = ResourceBuilder.build("textures/gui/frozen_player.png");
    private final Entity deadPlayer;

    public GuiFrozenPlayerInventory(InventoryPlayer playerInv, World world, int dpID) {
        super(new ContainerFrozenPlayer(playerInv, world, dpID));
        xSize = 256;
        ySize = 200;
        deadPlayer = world.getEntityByID(dpID);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        mc.getTextureManager().bindTexture(tex);
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
        String name = deadPlayer instanceof EntityFrozenDeadPlayer && ((EntityFrozenDeadPlayer) deadPlayer).getUsername() != null ? ((EntityFrozenDeadPlayer) deadPlayer).getUsername() : "Frozen corpse";
        drawCenteredString(fontRenderer, name, width / 2 - 85, height / 2 - 80, 0xaf0e0e);
        drawCenteredString(fontRenderer, mc.player.getDisplayName().getFormattedText(), width / 2 - 85, height / 2 + 5, 0x4286f4);
    }
}