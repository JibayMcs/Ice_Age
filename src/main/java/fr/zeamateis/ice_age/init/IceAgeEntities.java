package fr.zeamateis.ice_age.init;

import com.google.common.base.Function;
import fr.zeamateis.amy.resources.ResourceBuilder;
import fr.zeamateis.ice_age.common.IceAgeMod;
import fr.zeamateis.ice_age.common.entity.EntityFrozenDeadPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class IceAgeEntities {

    public static final EntityType<EntityFrozenDeadPlayer> FROZEN_DEAD_PLAYER_TYPE = createEntityType("frozen_dead_player", EntityFrozenDeadPlayer.class, EntityFrozenDeadPlayer::new, 64, 1, true);

    private static <T extends Entity> EntityType<T> createEntityType(String id, Class<? extends T> entityClass, Function<? super World, ? extends T> factory, int range, int updateFrequency,
                                                                     boolean sendsVelocityUpdates) {
        EntityType<T> type = EntityType.Builder.create(entityClass, factory).tracker(range, updateFrequency, sendsVelocityUpdates).build(String.format("%s:%s", IceAgeMod.MODID, id));
        type.setRegistryName(ResourceBuilder.build(id));
        return type;
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class EntityTypes {

        @SubscribeEvent
        public static void registerEntityTypes(RegistryEvent.Register<EntityType<?>> event) {
            event.getRegistry().register(IceAgeEntities.FROZEN_DEAD_PLAYER_TYPE);
        }
    }
}
