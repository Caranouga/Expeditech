package fr.caranouga.expeditech.events;

import fr.caranouga.expeditech.Expeditech;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static fr.caranouga.expeditech.utils.StringUtils.modLocation;

@Mod.EventBusSubscriber(modid = Expeditech.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if(event.getObject() instanceof PlayerEntity){
            TechLevelProvider techLevelProvider = new TechLevelProvider();
            event.addCapability(modLocation("techLevel"), techLevelProvider);
            event.addListener(techLevelProvider::invalidate);
        }
    }

}
