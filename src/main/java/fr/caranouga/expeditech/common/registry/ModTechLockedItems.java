package fr.caranouga.expeditech.common.registry;

import fr.caranouga.expeditech.common.Expeditech;
import fr.caranouga.expeditech.common.content.items.LockedItemRegistryKey;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.UUID;
import java.util.function.Supplier;

public class ModTechLockedItems {
    public static final DeferredRegister<LockedItemRegistryKey> LOCKED_ITEMS = DeferredRegister.create(LockedItemRegistryKey.class, Expeditech.MODID);

    public static final RegistryObject<LockedItemRegistryKey> SANDING_PAPER = register(() -> ModItems.SANDING_PAPER, 100);

    private static RegistryObject<LockedItemRegistryKey> register(Supplier<RegistryObject<? extends Item>> item, int techXp){
        return LOCKED_ITEMS.register(UUID.randomUUID().toString(), () -> new LockedItemRegistryKey(item, techXp));
    }

    public static boolean isLocked(ItemStack stack){
        for (RegistryObject<LockedItemRegistryKey> entry : LOCKED_ITEMS.getEntries()) {
            if(entry.get().getStack() == stack.getItem()) return true;
        }

        return false;
    }

    public static int getRequiredXp(ItemStack stack){
        for (RegistryObject<LockedItemRegistryKey> entry : LOCKED_ITEMS.getEntries()) {
            LockedItemRegistryKey lockedItemRegistryKey = entry.get();
            if(lockedItemRegistryKey.getStack() == stack.getItem()) return lockedItemRegistryKey.getTechXp();
        }

        return -1;
    }

    // TODO: Change that ("test")
    public static void register(IEventBus eBus){
        LOCKED_ITEMS.makeRegistry("test", RegistryBuilder::new);
        LOCKED_ITEMS.register(eBus);
    }
}
