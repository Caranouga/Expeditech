package fr.caranouga.expeditech.utils;


public class BlockEntry {
    private final LootTypeEntry lootType;

    public BlockEntry(LootTypeEntry lootType) {
        this.lootType = lootType;
    }

    public LootTypeEntry getLootType() {
        return lootType;
    }
}
