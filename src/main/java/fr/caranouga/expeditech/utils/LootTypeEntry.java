package fr.caranouga.expeditech.utils;


import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class LootTypeEntry {
    private final LootType lootType;
    private final RegistryObject<Item> drop;

    public LootTypeEntry(@Nonnull LootType lootType, @Nullable RegistryObject<Item> drop) {
        if(lootType.requiresDrop() && drop == null) {
            throw new IllegalArgumentException("LootType " + lootType + " requires a drop item");
        }

        this.lootType = lootType;
        this.drop = drop;
    }

    public LootTypeEntry(@Nonnull LootType lootType) {
        this(lootType, null);
    }

    public LootType getLootType() {
        return lootType;
    }

    public RegistryObject<Item> getDrop() {
        return drop;
    }

    public enum LootType {
        DROP_SELF,
        ORE_DROP(true);

        private final boolean requiresDrop;

        LootType(boolean requiresDrop) {
            this.requiresDrop = requiresDrop;
        }

        LootType() {
            this(false);
        }

        public boolean requiresDrop() {
            return requiresDrop;
        }
    }
}
