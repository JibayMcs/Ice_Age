package fr.zeamateis.ice_age.common.item.armor.material;

import fr.zeamateis.amy.resources.ResourceBuilder;
import fr.zeamateis.ice_age.init.IceAgeItems;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

public enum IceAgeArmorMaterial implements IArmorMaterial {

    FUR(
            "fur", 5, new int[]{1, 2, 3, 1},
            12, SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 0,
            () -> Ingredient.fromItems(IceAgeItems.FUR)
    ),

    IRON_FUR(
            "iron_fur", 15, new int[]{1, 4, 5, 2},
            12, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 0,
            () -> Ingredient.fromItems(IceAgeItems.FUR, Items.IRON_INGOT)
    ),

    GOLDEN_FUR(
            "golden_fur", 15, new int[]{1, 4, 5, 2},
            12, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0,
            () -> Ingredient.fromItems(IceAgeItems.FUR, Items.GOLD_INGOT)
    ),

    DIAMONG_FUR(
            "diamond_fur", 15, new int[]{1, 4, 5, 2},
            12, SoundEvents.ITEM_ARMOR_EQUIP_GOLD, 0,
            () -> Ingredient.fromItems(IceAgeItems.FUR, Items.DIAMOND)
    );


    /**
     * Holds the 'base' maxDamage that each armorType have.
     */
    private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};

    /**
     * The name of the armour material.
     */
    private final String name;

    /**
     * The maximum damage factor of the armour material, this is the item damage (how much it can absorb before it breaks).
     */
    private final int maxDamageFactor;

    /**
     * The damage reduction (each 1 point is half a shield on the GUI) of each piece of armour (helmet, plate, legs and boots).
     */
    private final int[] damageReductionAmountArray;

    /**
     * The enchantability factor of the armour material.
     */
    private final int enchantability;

    /**
     * The sound played when armour of the armour material is equipped.
     */
    private final SoundEvent soundEvent;

    /**
     * The armour toughness value of the armour material.
     */
    private final float toughness;

    /**
     * The repair material of the armour material.
     */
    private final LazyLoadBase<Ingredient> repairMaterial;

    IceAgeArmorMaterial(
            String name, int maxDamageFactor, int[] damageReductionAmountArray,
            int enchantability, SoundEvent soundEvent, float toughness,
            Supplier<Ingredient> repairMaterial
    ) {
        this.name = ResourceBuilder.build(name).toString();
        this.maxDamageFactor = maxDamageFactor;
        this.damageReductionAmountArray = damageReductionAmountArray;
        this.enchantability = enchantability;
        this.soundEvent = soundEvent;
        this.toughness = toughness;
        this.repairMaterial = new LazyLoadBase<>(repairMaterial);
    }

    @Override
    public int getDurability(EntityEquipmentSlot slotIn) {
        return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * maxDamageFactor;
    }

    @Override
    public int getDamageReductionAmount(EntityEquipmentSlot slotIn) {
        return damageReductionAmountArray[slotIn.getIndex()];
    }

    @Override
    public int getEnchantability() {
        return enchantability;
    }

    @Override
    public SoundEvent getSoundEvent() {
        return soundEvent;
    }

    @Override
    public Ingredient getRepairMaterial() {
        return repairMaterial.getValue();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public String getName() {
        return name;
    }

    @Override
    public float getToughness() {
        return toughness;
    }
}
