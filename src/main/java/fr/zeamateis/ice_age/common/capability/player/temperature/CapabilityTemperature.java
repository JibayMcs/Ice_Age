package fr.zeamateis.ice_age.common.capability.player.temperature;


import fr.zeamateis.ice_age.common.IceAgeMod;
import fr.zeamateis.ice_age.common.capability.CapabilityProviderSerializable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;


public final class CapabilityTemperature {


    @CapabilityInject(ITemperature.class)
    public static final Capability<ITemperature> TEMPERATURE_CAPABILITY = null;

    static final ResourceLocation ID = new ResourceLocation(IceAgeMod.MODID, "temperature");
    private static final EnumFacing DEFAULT_FACING = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(ITemperature.class, new Capability.IStorage<ITemperature>() {
            @Override
            public INBTBase writeNBT(Capability<ITemperature> capability, ITemperature instance, EnumFacing side) {
                return new NBTTagFloat(instance.getTemperature());
            }

            @Override
            public void readNBT(Capability<ITemperature> capability, ITemperature instance, EnumFacing side, INBTBase nbt) {
                instance.set(((NBTTagFloat) nbt).getFloat());
            }
        }, () -> new Temperature(null));
    }


    static LazyOptional<ITemperature> getTemperature(EntityPlayer entity) {
        return entity.getCapability(TEMPERATURE_CAPABILITY, DEFAULT_FACING);
    }

    static ICapabilityProvider createProvider(ITemperature temperature) {
        return new CapabilityProviderSerializable<>(TEMPERATURE_CAPABILITY, DEFAULT_FACING, temperature);
    }

    public static String formatTemperature(float temperatureIn) {
        return ItemStack.DECIMALFORMAT.format(temperatureIn);
    }


    @Mod.EventBusSubscriber(modid = IceAgeMod.MODID)
    private static class EventHandler {


        @SubscribeEvent
        public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof EntityPlayer) {
                Temperature temperature = new Temperature((EntityPlayer) event.getObject());
                event.addCapability(CapabilityTemperature.ID, CapabilityTemperature.createProvider(temperature));
                temperature.set(temperature.getMaxTemperature());
            }
        }


        @SubscribeEvent
        public static void playerClone(PlayerEvent.Clone event) {
            CapabilityTemperature.getTemperature(event.getOriginal()).ifPresent(oldTemperature -> {
                CapabilityTemperature.getTemperature(event.getEntityPlayer()).ifPresent(newTemperature -> {
                    newTemperature.set(oldTemperature.getTemperature());
                });
            });
        }


        @SubscribeEvent
        public static void playerChangeDimension(PlayerChangedDimensionEvent event) {
            CapabilityTemperature.getTemperature(event.getPlayer())
                    .ifPresent(ITemperature::synchronize);
        }
    }
}

