package fr.caranouga.expeditech.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SERVER_CONFIG;

    public static final ForgeConfigSpec.ConfigValue<Integer> exampleInteger;
    public static final ForgeConfigSpec.ConfigValue<Boolean> exampleBoolean;
    public static final ForgeConfigSpec.ConfigValue<Boolean> machineDurabilitySwitch;

    static {
        BUILDER.push("Expeditech Server Config");

        exampleInteger = BUILDER
                .comment("An example integer config value.")
                .defineInRange("exampleInteger", 42, 0, Integer.MAX_VALUE);
        exampleBoolean = BUILDER
                .comment("An example boolean config value.")
                .define("exampleBoolean", true);

        machineDurabilitySwitch = BUILDER
                .comment("Enable or disable machine durability.")
                .define("machineDurabilitySwitch", true);

        BUILDER.pop();
        SERVER_CONFIG = BUILDER.build();
    }
}
