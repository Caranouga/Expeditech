package fr.caranouga.expeditech.capability.techlevel;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.packets.TechLevelSyncPacket;
import fr.caranouga.expeditech.registry.ModCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.swing.*;

public class TechLevelUtils {
    public static int getTechLevel(Entity entity){
        return entity.getCapability(ModCapabilities.TECH_LEVEL).map(ITechLevel::getTechLevel).orElse(0);
    }

    public static int getTechXp(Entity entity){
        return entity.getCapability(ModCapabilities.TECH_LEVEL).map(ITechLevel::getTechXp).orElse(0);
    }

    public static void addTechXp(Entity entity, int amount){
        entity.getCapability(ModCapabilities.TECH_LEVEL).ifPresent(techLevel -> {
            techLevel.addTechXp(amount);

            update((PlayerEntity) entity, techLevel);
        });
    }

    public static void addTechLevel(Entity entity, int amount){
        entity.getCapability(ModCapabilities.TECH_LEVEL).ifPresent(techLevel -> {
            techLevel.addTechLevel(amount);

            update((PlayerEntity) entity, techLevel);
        });
    }

    public static void setTechXp(Entity entity, int amount){
        entity.getCapability(ModCapabilities.TECH_LEVEL).ifPresent(techLevel -> {
            techLevel.setTechXp(amount);

            update((PlayerEntity) entity, techLevel);
        });
    }

    public static void setTechLevel(Entity entity, int amount){
        entity.getCapability(ModCapabilities.TECH_LEVEL).ifPresent(techLevel -> {
            techLevel.setTechLevel(amount);

            update((PlayerEntity) entity, techLevel);
        });
    }

    private static void update(PlayerEntity player, ITechLevel techLevel) {
        player.sendMessage(new StringTextComponent("TechXP: " + techLevel.getTechXp() + " | TechLevel: " + techLevel.getTechLevel()), player.getUUID());
        // Adv
        Expeditech.NETWORK.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new TechLevelSyncPacket(techLevel.getTechLevel(), techLevel.getTechXp(), player.getId()));
    }
}
