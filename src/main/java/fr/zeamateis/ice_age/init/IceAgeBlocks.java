package fr.zeamateis.ice_age.init;

import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class IceAgeBlocks {


    @SubscribeEvent
    public static void registerBlock(RegistryEvent.Register<net.minecraft.block.Block> event) {
    }
}
