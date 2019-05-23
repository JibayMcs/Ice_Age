package fr.zeamateis.ice_age.common.event;

import fr.zeamateis.amy.network.AmyNetwork;
import fr.zeamateis.ice_age.common.IceAgeMod;
import fr.zeamateis.ice_age.common.capability.player.temperature.CapabilityTemperature;
import fr.zeamateis.ice_age.common.entity.EntityFrozenDeadPlayer;
import fr.zeamateis.ice_age.common.item.armor.ItemFurArmor;
import fr.zeamateis.ice_age.common.world.DayCounterWorldSavedData;
import fr.zeamateis.ice_age.init.IceAgeDamageSource;
import fr.zeamateis.ice_age.init.IceAgeItems;
import fr.zeamateis.ice_age.network.packet.PacketSnowStorm;
import fr.zeamateis.ice_age.network.packet.PacketWorldDay;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.IRegistry;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.EnumLightType;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import javax.annotation.Nullable;
import java.util.Random;

@EventBusSubscriber(modid = IceAgeMod.MODID, bus = EventBusSubscriber.Bus.FORGE)
public class IceAgeEntityEvent {

    private static final Random random = new Random();

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {

    }

    @SubscribeEvent
    public static void onLivingDeathEvent(LivingDeathEvent event) {
        if (!event.getEntityLiving().world.isRemote()) {
            World world = event.getEntityLiving().world;

            if (event.getEntityLiving() instanceof EntityPlayer) {

                EntityPlayer player = (EntityPlayer) event.getEntityLiving();
                EntityFrozenDeadPlayer deadPlayer = new EntityFrozenDeadPlayer(world, player.prevPosX, player.prevPosY, player.prevPosZ);

                deadPlayer.setUsername(player.getDisplayName().getFormattedText());
                deadPlayer.setUUID(player.getGameProfile().getId().toString());
                deadPlayer.setInventories(player.inventory.mainInventory, player.inventory.armorInventory, player.inventory.offHandInventory);

                if (event.getSource().equals(IceAgeDamageSource.COLD_DAMAGE)) {
                    deadPlayer.setRotation(player.rotationYaw);
                    deadPlayer.setRenderRotation();
                    player.inventory.clear();
                    world.spawnEntity(deadPlayer);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {

        World world = event.player.getEntityWorld();

        if (!event.player.isCreative() && !world.getWorldInfo().getDifficulty().equals(EnumDifficulty.PEACEFUL)) {

            event.player.getCapability(CapabilityTemperature.TEMPERATURE_CAPABILITY).ifPresent(temperature -> {
                if (temperature.getTemperature() <= 0) {
                    event.player.attackEntityFrom(IceAgeDamageSource.COLD_DAMAGE, 10F);
                }
            });

            if (DayCounterWorldSavedData.get(world).getAgeInDays() > 10 && DayCounterWorldSavedData.get(world).getAgeInDays() < 15) {
                event.player.getCapability(CapabilityTemperature.TEMPERATURE_CAPABILITY).ifPresent(temperature -> {
                    temperature.consume(0.05F);
                });
            } else if (DayCounterWorldSavedData.get(world).getAgeInDays() >= 15) {

                int randX = world.rand.nextInt(16);
                int randZ = world.rand.nextInt(16);

                int playerPosX = MathHelper.floor(event.player.posX);
                int playerPosZ = MathHelper.floor(event.player.posZ);
                int randY = getTopBlock(world, randX + playerPosX, randZ + playerPosZ);
                byte effectRange = 7;
                for (int xOffset = -effectRange; xOffset <= effectRange; xOffset++) {
                    for (int zOffset = -effectRange; zOffset <= effectRange; zOffset++) {

                        BlockPos blockPos = new BlockPos(randX + playerPosX, randY, randZ + playerPosZ);
                        Block block = world.getBlockState(blockPos).getBlock();

                        if (world.canBlockSeeSky(blockPos)) {

                            if (block == Blocks.LAVA) {
                                System.out.println("YAY LAVA");

                                world.setBlockState(blockPos, Blocks.OBSIDIAN.getDefaultState());
                            }
                        }
                    }
                }

                event.player.getCapability(CapabilityTemperature.TEMPERATURE_CAPABILITY).ifPresent(temperature -> {

                    temperature.consume(0.05F);

                    int range = 10;

                    for (int i = -range; i < range + 1; i++) {
                        for (int j = -range; j < range + 1; j++) {
                            for (int k = -range; k < range + 1; k++) {
                                int x = MathHelper.floor(event.player.posX) + i;
                                int y = MathHelper.floor(event.player.posY) + j;
                                int z = MathHelper.floor(event.player.posZ) + k;
                                Block blockNear = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                                if (blockNear == Blocks.LAVA) {
                                    if (world.getLightFor(EnumLightType.BLOCK, event.player.getPosition()) >= 13) {
                                        temperature.warm(0.05F);
                                    }
                                        /*if (new Random().nextInt(2000) == 0)
                                        {
                                            player.inventory.armorInventory[0].damageItem(1, player);
                                            player.inventory.armorInventory[1].damageItem(1, player);
                                            player.inventory.armorInventory[2].damageItem(1, player);
                                            player.inventory.armorInventory[3].damageItem(1, player);
                                        }*/
                                }
                            }
                        }
                    }

                });
            }

            ItemStack helmet = event.player.inventory.armorItemInSlot(3);
            ItemStack chestPlate = event.player.inventory.armorItemInSlot(2);
            ItemStack leggings = event.player.inventory.armorItemInSlot(1);
            ItemStack boots = event.player.inventory.armorItemInSlot(0);

            if (helmet != null && chestPlate != null && leggings != null && boots != null) {
                if (helmet.getItem() instanceof ItemFurArmor &&
                        chestPlate.getItem() instanceof ItemFurArmor &&
                        leggings.getItem() instanceof ItemFurArmor &&
                        boots.getItem() instanceof ItemFurArmor) {
                    event.player.getCapability(CapabilityTemperature.TEMPERATURE_CAPABILITY).ifPresent(temperature -> {
                        temperature.warm(0.5F);
                    });
                }
            }

            if (!world.canSeeSky(event.player.getPosition())) {
                if (world.getLightFor(EnumLightType.SKY, event.player.getPosition()) < 8) {
                    if (DayCounterWorldSavedData.get(world).getAgeInDays() > 10 && DayCounterWorldSavedData.get(world).getAgeInDays() < 15) {
                        event.player.getCapability(CapabilityTemperature.TEMPERATURE_CAPABILITY).ifPresent(temperature -> {
                            temperature.warm(0.005F);
                        });
                    } else if (DayCounterWorldSavedData.get(world).getAgeInDays() >= 15) {
                        event.player.getCapability(CapabilityTemperature.TEMPERATURE_CAPABILITY).ifPresent(temperature -> {

                            int range = 10;

                            for (int i = -range; i < range + 1; i++) {
                                for (int j = -range; j < range + 1; j++) {
                                    for (int k = -range; k < range + 1; k++) {
                                        int x = MathHelper.floor(event.player.posX) + i;
                                        int y = MathHelper.floor(event.player.posY) + j;
                                        int z = MathHelper.floor(event.player.posZ) + k;
                                        Block blockNear = world.getBlockState(new BlockPos(x, y, z)).getBlock();
                                        if (blockNear == Blocks.LAVA) {
                                            if (world.getLightFor(EnumLightType.BLOCK, event.player.getPosition()) >= 13) {
                                                temperature.warm(0.005F);
                                            }
                                        /*if (new Random().nextInt(2000) == 0)
                                        {
                                            player.inventory.armorInventory[0].damageItem(1, player);
                                            player.inventory.armorInventory[1].damageItem(1, player);
                                            player.inventory.armorInventory[2].damageItem(1, player);
                                            player.inventory.armorInventory[3].damageItem(1, player);
                                        }*/
                                        }
                                    }
                                }
                            }

                        });
                    }
                }
            }
        } else {
            event.player.getCapability(CapabilityTemperature.TEMPERATURE_CAPABILITY).ifPresent(temperature -> {
                temperature.warm(0.5F);
            });
        }
    }

    @SubscribeEvent
    public static void onLivingUpdate(LivingUpdateEvent event) {

        World world = event.getEntityLiving().getEntityWorld();

        EntityLivingBase entity = event.getEntityLiving();

        if (DayCounterWorldSavedData.get(world).getAgeInDays() >= 15)
            if (world.canSeeSky(entity.getPosition())) {
                if (entity.isBurning()) {
                    entity.extinguish();
                }
            }
    }


    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        AmyNetwork.sendPacketToDimension(DimensionType.OVERWORLD, new PacketWorldDay(DayCounterWorldSavedData.get(event.getPlayer().world).getAgeInDays()));
        AmyNetwork.sendPacketToDimension(DimensionType.OVERWORLD, new PacketSnowStorm(DayCounterWorldSavedData.get(event.getPlayer().world).isSnowStorm()));

        Minecraft.getInstance().getBlockColors().register(new IBlockColor() {
            @Override
            public int getColor(IBlockState iBlockState, @Nullable IWorldReaderBase iWorldReaderBase, @Nullable BlockPos blockPos, int i) {

                if (DayCounterWorldSavedData.get(event.getPlayer().world).getAgeInDays() > 5 && DayCounterWorldSavedData.get(event.getPlayer().world).getAgeInDays() < 10) {
                    return 0x723802;
                } else if (DayCounterWorldSavedData.get(event.getPlayer().world).getAgeInDays() > 10 && DayCounterWorldSavedData.get(event.getPlayer().world).getAgeInDays() < 15) {
                    return 0x184740;
                } else if (DayCounterWorldSavedData.get(event.getPlayer().world).getAgeInDays() >= 15) {
                    return 0x405b87;
                }
                return 0;
            }

        }, Blocks.JUNGLE_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.BIRCH_LEAVES, Blocks.ACACIA_LEAVES, Blocks.OAK_LEAVES);

        Minecraft.getInstance().getBlockColors().register(new IBlockColor() {
            @Override
            public int getColor(IBlockState iBlockState, @Nullable IWorldReaderBase iWorldReaderBase, @Nullable BlockPos blockPos, int i) {

                if (DayCounterWorldSavedData.get(event.getPlayer().world).getAgeInDays() >= 15) {
                    return 0x405b87;
                }
                return 0;
            }

        }, Blocks.SPRUCE_LEAVES);

        Minecraft.getInstance().getBlockColors().register(new IBlockColor() {
            @Override
            public int getColor(IBlockState iBlockState, @Nullable IWorldReaderBase iWorldReaderBase, @Nullable BlockPos blockPos, int i) {

                if (DayCounterWorldSavedData.get(event.getPlayer().world).getAgeInDays() >= 15) {
                    return 0x25698e;
                }
                return 0;
            }

        }, Blocks.GRASS, Blocks.TALL_GRASS, Blocks.FERN, Blocks.LARGE_FERN);

    }


    /**
     * TODO Enable loot table load event
     * <p>
     * Apparently is desactivated in 1.13.x
     * https://github.com/MinecraftForge/MinecraftForge/issues/5671
     *
     * @param event
     */
    /*@SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {

        LootEntry entry = new LootEntryTable(ResourceBuilder.build("inject/nether_star_test"), 1, 1, new LootCondition[0], "nether_star_test");


        if (event.getName().equals(LootTableList.ENTITIES_POLAR_BEAR)) {
            System.out.println("YAYA TRY TO ADD LOOT");

            LootPool pool = new LootPool(new LootEntry[]{entry}, new LootCondition[0], new RandomValueRange(0, 1), new RandomValueRange(0), "nether_star_test");

            event.getTable().addPool(pool);

            System.out.println("YAYA ADDED LOOT");
        }
    }*/
    @SubscribeEvent
    public static void onEntityDrop(LivingDropsEvent event) {
        EntityItem itemFur = new EntityItem(event.getEntityLiving().world, event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ, new ItemStack(IceAgeItems.FUR, random.nextInt(3)));

        for (String entityRegistryName : IceAgeMod.getConfig().COMMON.furDropEntities.get()) {
            if (IRegistry.ENTITY_TYPE.containsKey(ResourceLocation.tryCreate(entityRegistryName))) {
                event.getDrops().add(itemFur);
            }
        }

    }

    public static int getTopBlock(World world, int chunkX, int chunkZ) {
        try {
            Chunk chunk = world.getChunk(chunkX, chunkZ);
            int k = 255;

            while (k > 0) {
                Block l = chunk.getBlockState(chunkX & 15, k, chunkZ & 15).getBlock();
                if (l == Blocks.AIR) k--;
                else return k;
            }

            return -1;
        } catch (NullPointerException e) {
            e.printStackTrace();
            return -1;
        }
    }
}


