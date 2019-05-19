package fr.zeamateis.ice_age.common.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
import net.minecraftforge.common.ForgeConfigSpec.EnumValue;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class IceAgeConfig {

    private final ForgeConfigSpec clientSpec;
    private final Client client;

    public IceAgeConfig() {
        Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        clientSpec = specPair.getRight();
        client = specPair.getLeft();
    }

    public Client getClient() {
        return client;
    }

    public void register(ModLoadingContext context) {
        context.registerConfig(ModConfig.Type.CLIENT, clientSpec);
    }

    public static class Client {

        final BooleanValue showInGameOverlay, showEntityNameInOverlay, showEntityStatsInOverlay;

        final ConfigValue<String> damageParticleColor;

        final EnumValue<EnumGuiPos> overlayPosition;

        Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Client-only settings").push("client");

            showInGameOverlay = builder.comment("Show InGame Damage Indicator Overlay ?").translation("ice_age.config.client.showInGameOverlay").define("showInGameOverlay", true);
            showEntityNameInOverlay = builder.comment("Show Entity Name On Damage Indicator Overlay ?").translation("ice_age.config.client.showEntityNameInOverlay")
                    .define("showEntityNameInOverlay", true);
            showEntityStatsInOverlay = builder.comment("Show Entity Stats On Damage Indicator Overlay ?").translation("ice_age.config.client.showEntityStatsInOverlay")
                    .define("showEntityStatsInOverlay", true);

            damageParticleColor = builder.comment("Use hexadecimals colors for damage particles color").translation("ice_age.config.client.damageParticleColor").define("damageParticleColor",
                    "0xff1616");

            overlayPosition = builder.comment("Only use 'TOP_LEFT', 'TOP_RIGHT', 'BOTTOM_LEFT', or 'BOTTOM_RIGHT' positions").translation("ice_age.config.client.overlayPosition")
                    .defineEnum("overlayPosition", EnumGuiPos.TOP_LEFT);

            builder.pop();
        }

        public enum EnumGuiPos {
            TOP_LEFT,
            TOP_RIGHT,
            BOTTOM_LEFT,
            BOTTOM_RIGHT
        }

    }

}