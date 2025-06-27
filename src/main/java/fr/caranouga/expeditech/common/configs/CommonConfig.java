package fr.caranouga.expeditech.common.configs;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec COMMON_CONFIG;

    public static final ForgeConfigSpec.ConfigValue<Integer> exampleInteger;
    public static final ForgeConfigSpec.ConfigValue<Boolean> exampleBoolean;

    static {
        BUILDER.push("Expeditech Common Config");

        exampleInteger = BUILDER
                .comment("An example integer config value.")
                .defineInRange("exampleInteger", 42, 0, Integer.MAX_VALUE);
        exampleBoolean = BUILDER
                .comment("An example boolean config value.")
                .define("exampleBoolean", true);

        BUILDER.pop();
        COMMON_CONFIG = BUILDER.build();
    }
}
