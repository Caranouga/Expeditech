package fr.caranouga.expeditech.world;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.world.gen.ModOreGeneration;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Expeditech.MODID)
public class ModWorldEvents {
    @SubscribeEvent
    public static void biomeLoadingEvent(BiomeLoadingEvent event) {
        // WARNING: Ore before trees
        ModOreGeneration.generateOres(event);
    }
}
