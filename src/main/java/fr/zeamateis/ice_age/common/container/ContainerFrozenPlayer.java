package fr.zeamateis.ice_age.common.container;

import fr.zeamateis.ice_age.common.entity.EntityFrozenDeadPlayer;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class ContainerFrozenPlayer extends Container {
    private static final String[] ARMOR_SLOT_TEXTURES = {"item/empty_armor_slot_boots", "item/empty_armor_slot_leggings", "item/empty_armor_slot_chestplate", "item/empty_armor_slot_helmet"};
    private static final EntityEquipmentSlot[] VALID_EQUIPMENT_SLOTS = {EntityEquipmentSlot.HEAD, EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.FEET};

    public ContainerFrozenPlayer(InventoryPlayer playerInventory, World world, int dpID) {
        EntityFrozenDeadPlayer entity = (EntityFrozenDeadPlayer) world.getEntityByID(dpID);

        // Main inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(entity, j + i * 9 + 9, 84 + j * 18, 9 + i * 18));
            }
        }

        // Hotbar
        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(entity, i, 84 + i * 18, 67));
        }

        // Armor
        int k = 0;
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                EntityEquipmentSlot currentArmor = VALID_EQUIPMENT_SLOTS[k];
                addSlot(new Slot(entity, 39 - k, 42 + j * 18, 49 + i * 18) {
                    @Override
                    public int getSlotStackLimit() {
                        return 1;
                    }

                    @Override
                    public boolean isItemValid(ItemStack stack) {
                        return stack.getItem().canEquip(stack, currentArmor, playerInventory.player);
                    }

                    @Override
                    public boolean canTakeStack(EntityPlayer player) {
                        ItemStack itemstack = getStack();
                        return (itemstack.isEmpty() || player.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.canTakeStack(player);
                    }

                    @Override
                    @OnlyIn(Dist.CLIENT)
                    public String getSlotTexture() {
                        return ContainerFrozenPlayer.ARMOR_SLOT_TEXTURES[currentArmor.getIndex()];
                    }
                });
                k++;
            }
        }
        // Shield
        addSlot(new Slot(entity, 40, 10, 67) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return super.isItemValid(stack);
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            public String getSlotTexture() {
                return "item/empty_armor_slot_shield";
            }
        });

        // Player Inventory
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 84 + j * 18, 97 + i * 18));
            }
        }

        // Player hotbar
        for (int i = 0; i < 9; ++i) {
            addSlot(new Slot(playerInventory, i, 84 + i * 18, 155));
        }

        // Player Armor
        k = 0;
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 2; ++j) {
                EntityEquipmentSlot currentArmor = VALID_EQUIPMENT_SLOTS[k];
                addSlot(new Slot(playerInventory, 39 - k, 42 + j * 18, 137 + i * 18) {
                    @Override
                    public int getSlotStackLimit() {
                        return 1;
                    }

                    @Override
                    public boolean isItemValid(ItemStack stack) {
                        return stack.getItem().canEquip(stack, currentArmor, playerInventory.player);
                    }

                    @Override
                    public boolean canTakeStack(EntityPlayer player) {
                        ItemStack itemstack = getStack();
                        return (itemstack.isEmpty() || player.isCreative() || !EnchantmentHelper.hasBindingCurse(itemstack)) && super.canTakeStack(player);
                    }

                    @Override
                    @OnlyIn(Dist.CLIENT)
                    public String getSlotTexture() {
                        return ContainerFrozenPlayer.ARMOR_SLOT_TEXTURES[currentArmor.getIndex()];
                    }
                });
                k++;
            }
        }

        // Player Shield
        addSlot(new Slot(playerInventory, 40, 10, 155) {
            @Override
            public boolean isItemValid(ItemStack stack) {
                return super.isItemValid(stack);
            }

            @Override
            @OnlyIn(Dist.CLIENT)
            public String getSlotTexture() {
                return "item/empty_armor_slot_shield";
            }
        });
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < 40) {
                if (!mergeItemStack(itemstack1, 40, inventorySlots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeItemStack(itemstack1, 0, 40, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }

        return itemstack;
    }
}
