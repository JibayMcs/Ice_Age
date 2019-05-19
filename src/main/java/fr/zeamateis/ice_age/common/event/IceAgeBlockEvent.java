package fr.zeamateis.ice_age.common.event;

import fr.zeamateis.ice_age.common.IceAgeMod;
import fr.zeamateis.ice_age.common.world.DayCounterWorldSavedData;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = IceAgeMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class IceAgeBlockEvent {

    @SubscribeEvent
    public static void harvestDrops(BlockEvent.HarvestDropsEvent event) {
        /*World world = event.getWorld().getWorld();

        if (DayCounterWorldSavedData.get(world).getAgeInDays() >= 15) {
            if (event.getState().isIn(BlockTags.LOGS)) {
                event.getDrops().forEach(drops -> {
                    event.getDrops().remove(drops);
                    event.getDrops().add(new ItemStack(Blocks.STONE));
                });
            }
        }*/
    }

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent event) {

        World world = event.getWorld().getWorld();

        if (DayCounterWorldSavedData.get(world).getAgeInDays() >= 15) {
            if (event.getState().isIn(BlockTags.LOGS)) {
                if (isPlayerHarvestingLogWithoutCorrectTool(event.getState(), event.getPlayer())) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {

        World world = event.getEntityPlayer().world;

        if (DayCounterWorldSavedData.get(world).getAgeInDays() >= 15) {
            if (event.getState().isIn(BlockTags.LOGS)) {
                if (isPlayerHarvestingLogWithoutCorrectTool(event.getState(), event.getEntityPlayer())) {
                    event.setCanceled(true);
                } else {
                    event.setNewSpeed(event.getOriginalSpeed() / 6);
                }
            }
        }
    }

    /**
     * Can the tool harvest the block?
     * <p>
     * Adapted from {@link net.minecraftforge.common.ForgeHooks#canToolHarvestBlock}, but has an IBlockState parameter instead of getting the IBlockState
     * from an IBlockAccess and a BlockPos.
     *
     * @param state  The block's state
     * @param stack  The tool ItemStack
     * @param player The player harvesting the block
     * @return True if the tool can harvest the block
     */
    private static boolean canToolHarvestBlock(IBlockState state, ItemStack stack, EntityPlayer player) {
        ToolType tool = state.getHarvestTool();
        return !stack.isEmpty() && tool != null
                && stack.getItem().getHarvestLevel(stack, tool, player, state) >= state.getHarvestLevel();
    }

    /**
     * Is the player harvesting a log block without the correct tool?
     *
     * @param state  The block's state
     * @param player The player harvesting the block
     * @return True if the block is a log, the player isn't in creative mode and the player doesn't have the correct tool equipped
     */
    private static boolean isPlayerHarvestingLogWithoutCorrectTool(IBlockState state, EntityPlayer player) {
        return !player.abilities.isCreativeMode
                && state.isIn(BlockTags.LOGS)
                && !canToolHarvestBlock(state, player.getHeldItemMainhand(), player);
    }
}
