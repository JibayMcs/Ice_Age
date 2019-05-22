package fr.zeamateis.ice_age.init;

import fr.zeamateis.amy.resources.ResourceBuilder;
import fr.zeamateis.ice_age.common.IceAgeMod;
import fr.zeamateis.ice_age.common.item.armor.ItemFurArmor;
import fr.zeamateis.ice_age.common.item.armor.material.IceAgeArmorMaterial;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashSet;
import java.util.Set;

import static fr.zeamateis.amy.resources.ResourceBuilder.Null;

@ObjectHolder(IceAgeMod.MODID)
public class IceAgeItems {

    public static final Item FUR = Null();

    public static final Item FUR_HEAD = Null();
    public static final Item FUR_CHEST = Null();
    public static final Item FUR_LEGS = Null();
    public static final Item FUR_FEET = Null();

    public static final Item IRON_FUR_HEAD = Null();
    public static final Item IRON_FUR_CHEST = Null();
    public static final Item IRON_FUR_LEGS = Null();
    public static final Item IRON_FUR_FEET = Null();

    public static final Item GOLDEN_FUR_HEAD = Null();
    public static final Item GOLDEN_FUR_CHEST = Null();
    public static final Item GOLDEN_FUR_LEGS = Null();
    public static final Item GOLDEN_FUR_FEET = Null();

    public static final Item DIAMOND_FUR_HEAD = Null();
    public static final Item DIAMOND_FUR_CHEST = Null();
    public static final Item DIAMOND_FUR_LEGS = Null();
    public static final Item DIAMOND_FUR_FEET = Null();


    @Mod.EventBusSubscriber(modid = IceAgeMod.MODID, bus = Bus.MOD)
    public static class RegistrationHandler {
        public static final Set<Item> ICE_AGE_ITEMS = new HashSet<>();

        @SubscribeEvent
        public static void onRegisterItems(RegistryEvent.Register<Item> event) {

            Item[] items = {

                    new Item(defaultItemProperties()).setRegistryName(ResourceBuilder.build("fur")),

                    new ItemFurArmor(IceAgeArmorMaterial.FUR, EntityEquipmentSlot.HEAD, defaultItemProperties()).setRegistryName(ResourceBuilder.build("fur_helmet")),
                    new ItemFurArmor(IceAgeArmorMaterial.FUR, EntityEquipmentSlot.CHEST, defaultItemProperties()).setRegistryName(ResourceBuilder.build("fur_chestplate")),
                    new ItemFurArmor(IceAgeArmorMaterial.FUR, EntityEquipmentSlot.LEGS, defaultItemProperties()).setRegistryName(ResourceBuilder.build("fur_leggings")),
                    new ItemFurArmor(IceAgeArmorMaterial.FUR, EntityEquipmentSlot.FEET, defaultItemProperties()).setRegistryName(ResourceBuilder.build("fur_boots")),

                    new ItemFurArmor(IceAgeArmorMaterial.IRON_FUR, EntityEquipmentSlot.HEAD, defaultItemProperties()).setRegistryName(ResourceBuilder.build("iron_fur_helmet")),
                    new ItemFurArmor(IceAgeArmorMaterial.IRON_FUR, EntityEquipmentSlot.CHEST, defaultItemProperties()).setRegistryName(ResourceBuilder.build("iron_fur_chestplate")),
                    new ItemFurArmor(IceAgeArmorMaterial.IRON_FUR, EntityEquipmentSlot.LEGS, defaultItemProperties()).setRegistryName(ResourceBuilder.build("iron_fur_leggings")),
                    new ItemFurArmor(IceAgeArmorMaterial.IRON_FUR, EntityEquipmentSlot.FEET, defaultItemProperties()).setRegistryName(ResourceBuilder.build("iron_fur_boots")),

                    new ItemFurArmor(IceAgeArmorMaterial.GOLDEN_FUR, EntityEquipmentSlot.HEAD, defaultItemProperties()).setRegistryName(ResourceBuilder.build("golden_fur_helmet")),
                    new ItemFurArmor(IceAgeArmorMaterial.GOLDEN_FUR, EntityEquipmentSlot.CHEST, defaultItemProperties()).setRegistryName(ResourceBuilder.build("golden_fur_chestplate")),
                    new ItemFurArmor(IceAgeArmorMaterial.GOLDEN_FUR, EntityEquipmentSlot.LEGS, defaultItemProperties()).setRegistryName(ResourceBuilder.build("golden_fur_leggings")),
                    new ItemFurArmor(IceAgeArmorMaterial.GOLDEN_FUR, EntityEquipmentSlot.FEET, defaultItemProperties()).setRegistryName(ResourceBuilder.build("golden_fur_boots")),

                    new ItemFurArmor(IceAgeArmorMaterial.DIAMONG_FUR, EntityEquipmentSlot.HEAD, defaultItemProperties()).setRegistryName(ResourceBuilder.build("diamond_fur_helmet")),
                    new ItemFurArmor(IceAgeArmorMaterial.DIAMONG_FUR, EntityEquipmentSlot.CHEST, defaultItemProperties()).setRegistryName(ResourceBuilder.build("diamond_fur_chestplate")),
                    new ItemFurArmor(IceAgeArmorMaterial.DIAMONG_FUR, EntityEquipmentSlot.LEGS, defaultItemProperties()).setRegistryName(ResourceBuilder.build("diamond_fur_leggings")),
                    new ItemFurArmor(IceAgeArmorMaterial.DIAMONG_FUR, EntityEquipmentSlot.FEET, defaultItemProperties()).setRegistryName(ResourceBuilder.build("diamond_fur_boots"))
            };

            IForgeRegistry<Item> registry = event.getRegistry();

            for (Item item : items) {
                registry.register(item);
                ICE_AGE_ITEMS.add(item);
            }
        }

        private static Item.Properties defaultItemProperties() {
            return new Item.Properties().group(IceAgeMod.ICE_AGE_GROUP);
        }

    }
}
