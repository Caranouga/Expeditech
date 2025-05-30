package fr.caranouga.expeditech.events;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.capability.techlevel.TechLevelProvider;
import fr.caranouga.expeditech.capability.techlevel.TechLevelUtils;
import fr.caranouga.expeditech.client.ClientState;
import fr.caranouga.expeditech.commands.TechLevelCommand;
import fr.caranouga.expeditech.registry.ModCapabilities;
import fr.caranouga.expeditech.screens.TechLevelScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

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
            PlayerEntity player = event.getPlayer();
            player.getCapability(ModCapabilities.TECH_LEVEL).ifPresent(newTechLevel -> {
                newTechLevel.set(oldTechLevel);
                TechLevelUtils.update(player);
            });
        });
    }


    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Entity ent = event.getEntity();
        if(!ent.level.isClientSide()) {
            PlayerEntity player = (PlayerEntity) ent;

            TechLevelUtils.update(player);
        }
    }


    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        new TechLevelCommand(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.ALL){
            if(ClientState.isShowExpBar()){
                TechLevelScreen.render(event.getMatrixStack());
            }
        }
    }
}
