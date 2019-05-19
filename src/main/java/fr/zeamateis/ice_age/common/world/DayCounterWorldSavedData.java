package fr.zeamateis.ice_age.common.world;

import fr.zeamateis.amy.network.AmyNetwork;
import fr.zeamateis.ice_age.common.IceAgeMod;
import fr.zeamateis.ice_age.network.packet.PacketSnowStorm;
import fr.zeamateis.ice_age.network.packet.PacketWorldDay;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraft.world.storage.WorldSavedDataStorage;

import java.util.Random;

public class DayCounterWorldSavedData extends WorldSavedData {
    private static final String DATA_NAME = IceAgeMod.MODID;
    private static World world;
    private final Random random = new Random();
    private int ageInDays = 0;
    private long lastTime = 0;
    private boolean snowStorm = false;
    private int snowStormTime = 0;


    private DayCounterWorldSavedData(String s) {
        super(s);
    }

    public static DayCounterWorldSavedData get(World worldIn) {
        world = worldIn;

        WorldSavedDataStorage storage = worldIn.getSavedDataStorage();
        DayCounterWorldSavedData instance = storage.get(worldIn.dimension.getType(), DayCounterWorldSavedData::new, DATA_NAME);

        if (instance == null) {
            instance = new DayCounterWorldSavedData(DATA_NAME);
            storage.set(worldIn.dimension.getType(), DATA_NAME, instance);
        }

        return instance;
    }

    /**
     * reads in data from the NBTTagCompound into this MapDataBase
     *
     * @param nbt
     */
    @Override
    public void read(NBTTagCompound nbt) {
        ageInDays = nbt.getInt("ageInDays");
        lastTime = nbt.getLong("lastTime");
        snowStorm = nbt.getBoolean("snowStorm");
        snowStormTime = nbt.getInt("snowStormTime");
    }

    @Override
    public NBTTagCompound write(NBTTagCompound compound) {
        NBTTagCompound nbt = new NBTTagCompound();

        nbt.putInt("ageInDays", ageInDays);
        nbt.putLong("lastTime", lastTime);
        nbt.putBoolean("snowStorm", snowStorm);
        nbt.putInt("snowStormTime", snowStormTime);

        return nbt;
    }

    public int getAgeInDays() {
        return ageInDays;
    }

    public void setAgeInDays(int ageInDaysIn) {
        ageInDays = ageInDaysIn;
    }

    public boolean isSnowStorm() {
        return snowStorm;
    }

    public void setSnowStorm(boolean isSnowStorm) {
        snowStorm = isSnowStorm;
        markDirty();
    }

    public int getSnowStormTime() {
        return snowStormTime;
    }

    public void update(long currentTime) {
        if (ageInDays < 15) {
            //System.out.println("last time: " + lastTime);
            //System.out.println("current time: " + currentTime);

            currentTime = currentTime % 24000;

            if (currentTime < lastTime) {
                addDay();
                AmyNetwork.sendPacketToEveryone(new PacketWorldDay(getAgeInDays()));
            }

            lastTime = currentTime;
        } else if (ageInDays >= 15) {
            currentTime = currentTime % 24000;


            if (currentTime < lastTime) {
                setSnowStorm(!isSnowStorm());
                AmyNetwork.sendPacketToEveryone(new PacketSnowStorm(isSnowStorm()));
            }

            lastTime = currentTime;
        }
    }

    private void addDay() {
        addDays(1);
    }

    private void addDays(int i) {
        ageInDays += i;
        markDirty();
    }
}