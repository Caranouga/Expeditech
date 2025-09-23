package fr.caranouga.expeditech.common.content.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.function.Supplier;

public class LockedItemRegistryKey extends ForgeRegistryEntry<LockedItemRegistryKey> {
    private final Item stack;
    private final int techXp;

    public LockedItemRegistryKey(Supplier<RegistryObject<? extends Item>> stack, int techXp) {
        this.stack = stack.get().get();
        this.techXp = techXp;
    }

    public Item getStack() {
        return stack;
    }

    public int getTechXp() {
        return techXp;
    }
}
