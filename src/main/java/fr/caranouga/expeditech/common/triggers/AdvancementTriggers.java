package fr.caranouga.expeditech.common.triggers;

import net.minecraft.advancements.CriteriaTriggers;

public class AdvancementTriggers {
    public static final TechLevelTrigger TECHLEVEL_TRIGGER = new TechLevelTrigger();

    private static final TechLevelTrigger[] TRIGGERS = new TechLevelTrigger[]{
            TECHLEVEL_TRIGGER
    };

    public static void registerTriggers() {
        for (TechLevelTrigger trigger : TRIGGERS) {
            CriteriaTriggers.register(trigger);
        }
    }
}
