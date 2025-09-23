package fr.caranouga.expeditech.common.packets;

import fr.caranouga.expeditech.common.entities.TechLevelExperienceOrb;
import net.minecraft.client.network.play.IClientPlayNetHandler;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

public class SpawnTechLevelExperienceOrbPacket implements IPacket<IClientPlayNetHandler> {
    private int id;
    private double x;
    private double y;
    private double z;
    private int value;

    public SpawnTechLevelExperienceOrbPacket(PacketBuffer buffer) {
        this.id = buffer.readInt();
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        this.value = buffer.readShort();
    }

    public SpawnTechLevelExperienceOrbPacket(TechLevelExperienceOrb orb) {
        this.id = orb.getId();
        this.x = orb.getX();
        this.y = orb.getY();
        this.z = orb.getZ();
        this.value = orb.getValue();
    }

    @Override
    public void read(PacketBuffer buffer) {
        this.id = buffer.readVarInt();
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        this.value = buffer.readShort();
    }

    @Override
    public void write(PacketBuffer buffer) {
        buffer.writeVarInt(this.id);
        buffer.writeDouble(this.x);
        buffer.writeDouble(this.y);
        buffer.writeDouble(this.z);
        buffer.writeShort(this.value);
    }

    @Override
    public void handle(IClientPlayNetHandler handler) {
        ClientPacketHandler.handleSpawnTechXpOrb(this);
    }

    // --- Client getters ---
    @OnlyIn(Dist.CLIENT)
    public int getId() { return this.id; }

    @OnlyIn(Dist.CLIENT)
    public double getX() { return this.x; }

    @OnlyIn(Dist.CLIENT)
    public double getY() { return this.y; }

    @OnlyIn(Dist.CLIENT)
    public double getZ() { return this.z; }

    @OnlyIn(Dist.CLIENT)
    public int getValue() { return this.value; }
}
