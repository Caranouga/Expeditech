package fr.caranouga.expeditech.common.registry;

import fr.caranouga.expeditech.common.Expeditech;
import fr.caranouga.expeditech.common.packets.MultiblockErrorPacket;
import fr.caranouga.expeditech.common.packets.TechLevelSyncPacket;

public class ModPackets {
    public static void register() {
        int idx = 0;

        Expeditech.NETWORK.registerMessage(idx++, TechLevelSyncPacket.class, TechLevelSyncPacket::encode, TechLevelSyncPacket::decode, TechLevelSyncPacket::handle);
        Expeditech.NETWORK.registerMessage(idx++, MultiblockErrorPacket.class, MultiblockErrorPacket::encode, MultiblockErrorPacket::decode, MultiblockErrorPacket::handle);
    }
}
