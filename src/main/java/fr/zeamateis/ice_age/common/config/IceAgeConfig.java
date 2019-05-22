package fr.zeamateis.ice_age.common.config;

import com.google.common.collect.Lists;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

public class IceAgeConfig {

    public static final ForgeConfigSpec CLIENT_SPECS;
    public static final Client CLIENT;

    public static final ForgeConfigSpec COMMON_SPECS;
    public static final Common COMMON;

    public static final ForgeConfigSpec SERVER_SPECS;
    public static final Server SERVER;

    static {

        Pair<Client, ForgeConfigSpec> clientPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPECS = clientPair.getRight();
        CLIENT = clientPair.getLeft();

        Pair<Common, ForgeConfigSpec> commonPair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON_SPECS = commonPair.getRight();
        COMMON = commonPair.getLeft();

        Pair<Server, ForgeConfigSpec> serverPair = new ForgeConfigSpec.Builder().configure(Server::new);
        SERVER_SPECS = serverPair.getRight();
        SERVER = serverPair.getLeft();
    }

    public static class Client {

        public Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Client config")
                    .push("client");

            builder.pop();
        }
    }

    public static class Common {

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> furDropEntities;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Common config")
                    .push("common");

            furDropEntities = builder
                    .comment("List of entity that can drop fur item").defineList("furDropEntities", Lists.newArrayList(), o -> o instanceof String);

            builder.pop();
        }

    }

    public static class Server {


        public Server(ForgeConfigSpec.Builder builder) {
            builder.comment("Server config")
                    .push("server");

            builder.pop();
        }

    }


}