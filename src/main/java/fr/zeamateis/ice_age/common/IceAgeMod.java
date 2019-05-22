package fr.zeamateis.ice_age.common;

import fr.zeamateis.amy.resources.ResourceBuilder;
import fr.zeamateis.ice_age.common.config.IceAgeConfig;
import fr.zeamateis.ice_age.init.IceAgeCapabilities;
import fr.zeamateis.ice_age.init.IceAgeGuiHandler;
import fr.zeamateis.ice_age.init.IceAgeNetwork;
import fr.zeamateis.ice_age.init.IceAgeRecipes;
import fr.zeamateis.ice_age.proxy.ClientProxy;
import fr.zeamateis.ice_age.proxy.CommonProxy;
import fr.zeamateis.ice_age.proxy.ServerProxy;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(IceAgeMod.MODID)
public class IceAgeMod {
    public static final String MODID = "ice_age";
    public static final ItemGroup ICE_AGE_GROUP = new ItemGroup("ice_age") {
        @OnlyIn(Dist.CLIENT)
        @Override
        public ItemStack createIcon() {
            return new ItemStack(Blocks.BLUE_ICE);
        }
    };
    private static final Logger LOGGER = LogManager.getLogger();
    private static final CommonProxy PROXY = DistExecutor.runForDist(() -> ClientProxy::new, () -> ServerProxy::new);
    private static final IceAgeConfig CONFIG = new IceAgeConfig();

    public IceAgeMod() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, CONFIG.CLIENT_SPECS);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CONFIG.COMMON_SPECS);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, CONFIG.SERVER_SPECS);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(IceAgeMod::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(IceAgeMod::setupClient);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onServerSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(IceAgeMod::onPostInit);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static void setup(FMLCommonSetupEvent event) {
        IceAgeGuiHandler.register();
        IceAgeNetwork.register();
        IceAgeCapabilities.register();
        IceAgeRecipes.register();
        LootTableList.register(ResourceBuilder.build("inject/nether_star_test"));
    }

    private static void setupClient(FMLClientSetupEvent event) {
        PROXY.onRegistryEntityRenderer();
    }

    private static void onPostInit(FMLLoadCompleteEvent event) {
        PROXY.onRegistryParticle();
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static CommonProxy getProxy() {
        return PROXY;
    }

    public static IceAgeConfig getConfig() {
        return CONFIG;
    }

    private void enqueueIMC(InterModEnqueueEvent event) {
    }

    private void processIMC(InterModProcessEvent event) {
    }

    private void onServerSetup(FMLDedicatedServerSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
    }

}
