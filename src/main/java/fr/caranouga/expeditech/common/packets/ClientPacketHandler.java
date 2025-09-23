package fr.caranouga.expeditech.common.packets;

import fr.caranouga.expeditech.common.entities.TechLevelExperienceOrb;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;

public class ClientPacketHandler {
    public static void handleSpawnTechXpOrb(SpawnTechLevelExperienceOrbPacket packet){
        Minecraft mc = Minecraft.getInstance();
        ClientWorld world = mc.level;

        if(world == null) return;

        TechLevelExperienceOrb orb = TechLevelExperienceOrb.createEntity(world, packet.getX(), packet.getY(), packet.getZ(), packet.getValue());

        world.putNonPlayerEntity(packet.getId(), orb);
    }
}
