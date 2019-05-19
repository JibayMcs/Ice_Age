package fr.zeamateis.ice_age.common.capability;

import net.minecraft.nbt.INBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;

/**
 * A simple implementation of {@link ICapabilityProvider} and {@link INBTSerializable} that supports a single {@link Capability} handler instance.
 * <p>
 * Uses the {@link Capability}'s {@link IStorage} to serialise/deserialise NBT.
 *
 * @author Choonster
 */
public class CapabilityProviderSerializable<HANDLER> extends CapabilityProviderSimple<HANDLER> implements INBTSerializable<INBTBase> {

    /**
     * Create a provider for the default handler instance.
     *
     * @param capability The Capability instance to provide the handler for
     * @param facing     The EnumFacing to provide the handler for
     */
    public CapabilityProviderSerializable(Capability<HANDLER> capability, @Nullable EnumFacing facing) {
        this(capability, facing, capability.getDefaultInstance());
    }

    /**
     * Create a provider for the specified handler instance.
     *
     * @param capability The Capability instance to provide the handler for
     * @param facing     The EnumFacing to provide the handler for
     * @param instance   The handler instance to provide
     */
    public CapabilityProviderSerializable(Capability<HANDLER> capability, @Nullable EnumFacing facing, @Nullable HANDLER instance) {
        super(capability, facing, instance);
    }

    @Nullable
    @Override
    public INBTBase serializeNBT() {
        HANDLER instance = getInstance();

        if (instance == null) {
            return null;
        }

        return getCapability().writeNBT(instance, getFacing());
    }

    @Override
    public void deserializeNBT(INBTBase nbt) {
        HANDLER instance = getInstance();

        if (instance == null) {
            return;
        }

        getCapability().readNBT(instance, getFacing(), nbt);
    }

}