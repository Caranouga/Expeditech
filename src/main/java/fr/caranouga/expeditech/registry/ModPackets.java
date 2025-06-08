package fr.caranouga.expeditech.registry;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.packets.TechLevelSyncPacket;

public class ModPackets {
    public static void register() {
        int idx = 0;

        Expeditech.NETWORK.registerMessage(idx++, TechLevelSyncPacket.class, TechLevelSyncPacket::encode, TechLevelSyncPacket::decode, TechLevelSyncPacket::handle);
    }
}
