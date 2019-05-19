package fr.zeamateis.ice_age.common.entity;

import fr.zeamateis.amy.network.AmyNetwork;
import fr.zeamateis.ice_age.common.IceAgeMod;
import fr.zeamateis.ice_age.common.container.ContainerFrozenPlayer;
import fr.zeamateis.ice_age.init.IceAgeEntities;
import fr.zeamateis.ice_age.network.packet.PacketIceParticle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EntityFrozenDeadPlayer extends Entity implements IInventory, IInteractionObject {
    private static final DataParameter<String> USERNAME = EntityDataManager.createKey(EntityFrozenDeadPlayer.class, DataSerializers.STRING);
    private static final DataParameter<String> UUID = EntityDataManager.createKey(EntityFrozenDeadPlayer.class, DataSerializers.STRING);
    private static final DataParameter<String> RENDER_ANGLES = EntityDataManager.createKey(EntityFrozenDeadPlayer.class, DataSerializers.STRING);
    private static final DataParameter<Float> ROTATION_ANGLES = EntityDataManager.createKey(EntityFrozenDeadPlayer.class, DataSerializers.FLOAT);
    public final NonNullList<ItemStack> armorInventory = NonNullList.withSize(4, ItemStack.EMPTY);
    private final NonNullList<ItemStack> mainInventory = NonNullList.withSize(36, ItemStack.EMPTY);
    private final NonNullList<ItemStack> leftHand = NonNullList.withSize(1, ItemStack.EMPTY);
    private final Map<String, NonNullList<ItemStack>> allInventories;

    public EntityFrozenDeadPlayer(World world) {
        super(IceAgeEntities.FROZEN_DEAD_PLAYER_TYPE, world);
        allInventories = new HashMap<>();
        allInventories.put("ItemsMain", mainInventory);
        allInventories.put("ItemsArmor", armorInventory);
        allInventories.put("ItemsLeft", leftHand);
        setSize(1.0F, 2F);
    }

    public EntityFrozenDeadPlayer(World worldIn, double x, double y, double z) {
        this(worldIn);
        setPosition(x, y, z);
        motionX = 0.0D;
        motionY = 0.0D;
        motionZ = 0.0D;
        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;
    }

    private static void readInventoryToNBT(NBTTagList tagList, NonNullList<ItemStack> inventory) {
        for (int i = 0; i < tagList.size(); ++i) {
            NBTTagCompound compound = tagList.getCompound(i);
            int j = compound.getByte("Slot") & 255;
            ItemStack itemStack = ItemStack.read(compound);
            if (!itemStack.isEmpty()) {
                if (j < inventory.size()) {
                    inventory.set(j, itemStack);
                }
            }
        }
    }

    private static void writeInventoryToNBT(NBTTagList tagList, NonNullList<ItemStack> inventory) {
        for (int i = 0; i < inventory.size(); ++i) {
            if (!inventory.get(i).isEmpty()) {
                NBTTagCompound compound = new NBTTagCompound();
                compound.putByte("Slot", (byte) i);
                inventory.get(i).write(compound);
                tagList.add(compound);
            }
        }
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    protected void registerData() {
        dataManager.register(USERNAME, "");
        dataManager.register(UUID, "");
        dataManager.register(ROTATION_ANGLES, 0.0F);
        dataManager.register(RENDER_ANGLES, "");
    }

    @Override
    protected void readAdditional(NBTTagCompound compound) {
        dataManager.set(USERNAME, compound.getString("Username"));
        dataManager.set(UUID, compound.getString("UUID"));
        dataManager.set(ROTATION_ANGLES, compound.getFloat("RotationAngle"));
        dataManager.set(RENDER_ANGLES, compound.getString("RotationRender"));

        mainInventory.clear();
        armorInventory.clear();
        leftHand.clear();

        allInventories.forEach((key, value) -> {
            NBTTagList nbttaglist = compound.getList(key, 10);
            readInventoryToNBT(nbttaglist, value);
        });
    }

    @Override
    protected void writeAdditional(NBTTagCompound compound) {
        compound.putString("Username", dataManager.get(USERNAME));
        compound.putString("UUID", dataManager.get(UUID));
        compound.putFloat("RotationAngle", dataManager.get(ROTATION_ANGLES));
        compound.putString("RotationRender", dataManager.get(RENDER_ANGLES));

        allInventories.forEach((key, inventory) -> {
            NBTTagList nbttaglist = new NBTTagList();
            writeInventoryToNBT(nbttaglist, inventory);
            compound.put(key, nbttaglist);
        });
    }

    @Override
    public void tick() {
        super.tick();
        double d0 = 0.4D;
        motionX = MathHelper.clamp(motionX, -d0, d0);
        motionZ = MathHelper.clamp(motionZ, -d0, d0);

        double moveY = motionY;

        if (Math.abs(motionX) < 0.3f && Math.abs(motionZ) < 0.3f) {
            moveY = -0.5f;
            motionY = moveY;
        }

        move(MoverType.SELF, motionX, moveY, motionZ);

        if (!onGround) {
            motionX *= 0.94999998807907104D;
            motionY *= 0.94999998807907104D;
            motionZ *= 0.94999998807907104D;
        }

        //After 10 minutes remove entity from world
        if (ticksExisted >= 6000 * 2) {
            remove();
        }

    }

    public String getUsername() {
        return dataManager.get(USERNAME);
    }

    public void setUsername(String username) {
        dataManager.set(USERNAME, username);
    }

    public String getUUID() {
        return dataManager.get(UUID);
    }

    public void setUUID(String uuid) {
        dataManager.set(UUID, uuid);
    }

    public float getRotation() {
        return dataManager.get(ROTATION_ANGLES);
    }

    public void setRotation(float angle) {
        dataManager.set(ROTATION_ANGLES, angle);
    }

    public void setRenderRotation() {
        float head = (float) Math.toRadians(90.0F - (rand.nextFloat() * 45.0F));
        float leftArmY = (float) (Math.PI / 4) - (rand.nextFloat() * (float) (Math.PI / 8));
        float leftArmz = -(rand.nextFloat() * (float) (Math.PI / 2));
        float rightArmY = (float) (Math.PI / 4) - (rand.nextFloat() * (float) (Math.PI / 8));
        float rightArmz = (rand.nextFloat() * (float) (Math.PI / 2));
        float leftLegY = -(rand.nextFloat() * (float) (Math.PI / 8));
        float leftLegZ = -(rand.nextFloat() * (float) (Math.PI / 8));
        float rightLegY = (rand.nextFloat() * (float) (Math.PI / 8));
        float rightLegZ = (rand.nextFloat() * (float) (Math.PI / 8));

        String s = "" + head + ";" + leftArmY + ";" + leftArmz + ";" + rightArmY + ";" + rightArmz + ";" + leftLegY + ";" + leftLegZ + ";" + rightLegY + ";" + rightLegZ;
        dataManager.set(RENDER_ANGLES, s);
    }

    public float[] getRenderRotation() {
        String s = dataManager.get(RENDER_ANGLES);
        if (s.contains(";")) {
            String[] str = s.split(";");
            float[] f = new float[10];
            for (int i = 0; i < str.length; i++) {
                f[i] = Float.valueOf(str[i]);
            }
            return f;
        }
        return null;
    }

    public void setInventories(NonNullList<ItemStack> invmain, NonNullList<ItemStack> invarmor, NonNullList<ItemStack> offHandInventory) {
        Collections.copy(mainInventory, invmain);
        Collections.copy(armorInventory, invarmor);
        Collections.copy(leftHand, offHandInventory);
    }

    @Override
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return getBoundingBox();
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return getBoundingBox();
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (!player.world.isRemote()) {
            NetworkHooks.openGui((EntityPlayerMP) player, this, (buffer) -> {
                buffer.writeInt(getEntityId());
            });
        }
        return true;
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (isAlive()) {
            remove();
            playSound(SoundEvents.BLOCK_GLASS_BREAK, 1.0F, 1.0F);
            double d0 = posX - 0.5D;
            double d1 = posY + 1D;
            double d2 = posZ - 0.5D;
            for (int i = 0; i < 50; i++)
                AmyNetwork.sendPacketToServer(new PacketIceParticle(Math.random() + d0, Math.random() + d1, Math.random() + d2, 0.0D, 0.0D, 0.0D, Math.random() + d0 * i, Math.random() + d1 * i, Math.random() + d2 * i));
            return true;
        } else return false;
    }


    @Override
    public int getSizeInventory() {
        return mainInventory.size() + armorInventory.size() + leftHand.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        NonNullList<ItemStack> list = null;

        for (NonNullList<ItemStack> nonnulllist : allInventories.values()) {
            if (index < nonnulllist.size()) {
                list = nonnulllist;
                break;
            }
            index -= nonnulllist.size();
        }

        return list == null ? ItemStack.EMPTY : list.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        NonNullList<ItemStack> list = null;

        for (NonNullList<ItemStack> nonnulllist : allInventories.values()) {
            if (index < nonnulllist.size()) {
                list = nonnulllist;
                break;
            }
            index -= nonnulllist.size();
        }

        return list != null && !list.get(index).isEmpty() ? ItemStackHelper.getAndSplit(list, index, count) : ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        NonNullList<ItemStack> nonnulllist = null;

        for (NonNullList<ItemStack> nonnulllist1 : allInventories.values()) {
            if (index < nonnulllist1.size()) {
                nonnulllist = nonnulllist1;
                break;
            }
            index -= nonnulllist1.size();
        }

        if (nonnulllist != null && !nonnulllist.get(index).isEmpty()) {
            ItemStack itemstack = nonnulllist.get(index);
            nonnulllist.set(index, ItemStack.EMPTY);
            return itemstack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        NonNullList<ItemStack> nonnulllist = null;

        for (NonNullList<ItemStack> nonnulllist1 : allInventories.values()) {
            if (index < nonnulllist1.size()) {
                nonnulllist = nonnulllist1;
                break;
            }
            index -= nonnulllist1.size();
        }

        if (nonnulllist != null) {
            nonnulllist.set(index, stack);
        }
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void markDirty() {
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return isAlive() && player.getDistanceSq(this) <= 5.0D;
    }

    @Override
    public void openInventory(EntityPlayer player) {
    }

    @Override
    public void closeInventory(EntityPlayer player) {
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {
    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        allInventories.values().forEach(NonNullList::clear);
    }

    @Override
    public Container createContainer(InventoryPlayer inventoryPlayer, EntityPlayer entityPlayer) {
        return new ContainerFrozenPlayer(inventoryPlayer, entityPlayer.world, getEntityId());
    }

    @Override
    public String getGuiID() {
        return String.format("%s:%s", IceAgeMod.MODID, "gui_dead_player");
    }
}
