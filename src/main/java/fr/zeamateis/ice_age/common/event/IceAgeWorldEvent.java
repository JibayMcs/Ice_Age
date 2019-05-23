package fr.zeamateis.ice_age.common.event;

import fr.zeamateis.ice_age.common.IceAgeMod;
import fr.zeamateis.ice_age.common.world.DayCounterWorldSavedData;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber(modid = IceAgeMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class IceAgeWorldEvent {


    @SubscribeEvent
    public static void onTickWorld(TickEvent.WorldTickEvent event) {
        World world = event.world;

        if (world != null) {
            if (!world.isRemote()) {
                if (world.getDimension().isSurfaceWorld()) {

                    if (event.phase == TickEvent.Phase.END) {
                        return;
                    }

                    DayCounterWorldSavedData.get(world).update(world.getWorldInfo().getGameTime());

                    for (BiomeDictionary.Type types : BiomeDictionary.Type.getAll()) {
                        for (Biome b : BiomeDictionary.getBiomes(types)) {
                            if (DayCounterWorldSavedData.get(world).getAgeInDays() > 5 && DayCounterWorldSavedData.get(world).getAgeInDays() < 10) {
                                b.temperature = 0.0F;
                                b.downfall = 1.0F;
                                b.precipitation = Biome.RainType.SNOW;
                            } else if (DayCounterWorldSavedData.get(world).getAgeInDays() > 10 && DayCounterWorldSavedData.get(world).getAgeInDays() < 15) {
                                b.temperature = -0.5F;
                                b.downfall = 1.0F;
                                b.precipitation = Biome.RainType.SNOW;
                            } else if (DayCounterWorldSavedData.get(world).getAgeInDays() >= 15) {
                                b.temperature = -1.0F;
                                b.downfall = 1.0F;
                                b.precipitation = Biome.RainType.SNOW;

                                if (!world.getWorldInfo().isRaining()) {
                                    world.setRainStrength(1.0F);
                                    world.getWorldInfo().setRaining(true);
                                    world.getWorldInfo().setRainTime(100000);
                                }

                            }
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWorldSave(WorldEvent.Save event) {
        DayCounterWorldSavedData.get(event.getWorld().getWorld()).markDirty();
    }

}
