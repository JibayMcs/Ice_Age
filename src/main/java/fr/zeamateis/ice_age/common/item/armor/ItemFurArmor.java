package fr.zeamateis.ice_age.common.item.armor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;


public class ItemFurArmor extends ItemArmor {

    public ItemFurArmor(IArmorMaterial material, EntityEquipmentSlot equipmentSlot, Item.Properties properties) {
        super(material, equipmentSlot, properties);
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, EntityPlayer player) {

    }
}