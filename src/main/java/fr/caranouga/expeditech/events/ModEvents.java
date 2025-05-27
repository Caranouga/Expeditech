package fr.caranouga.expeditech.events;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.capability.techlevel.TechLevelProvider;
import fr.caranouga.expeditech.commands.TechLevelCommand;
import fr.caranouga.expeditech.registry.ModCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static fr.caranouga.expeditech.capability.techlevel.TechLevelUtils.getTechLevel;
import static fr.caranouga.expeditech.capability.techlevel.TechLevelUtils.getTechXp;
import static fr.caranouga.expeditech.registry.ModCapabilities.TECH_LEVEL_ID;

@Mod.EventBusSubscriber(modid = Expeditech.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModEvents {
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if(!(event.getObject() instanceof PlayerEntity)) return;

        event.addCapability(TECH_LEVEL_ID, new TechLevelProvider());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        event.getOriginal().getCapability(ModCapabilities.TECH_LEVEL).ifPresent(oldTechLevel -> {
            event.getPlayer().getCapability(ModCapabilities.TECH_LEVEL).ifPresent(newTechLevel -> {
                newTechLevel.set(oldTechLevel);
            });
        });
    }


    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Entity ent = event.getEntity();
        if(!ent.level.isClientSide()) {
            int techLevel = getTechLevel(ent);
            int techXp = getTechXp(ent);
            PlayerEntity player = (PlayerEntity) ent;
            player.sendMessage(new StringTextComponent("Your current Tech Level is: " + techLevel + " with " + techXp + " XP."), player.getUUID());
        }
    }


    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        new TechLevelCommand(event.getDispatcher());
    }

}
