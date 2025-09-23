package fr.caranouga.expeditech.common.registry;

import fr.caranouga.expeditech.common.Expeditech;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.function.Supplier;

public class ModTechLockedItems {
    public static final DeferredRegister<LockedItem> LOCKED_ITEMS = DeferredRegister.create(LockedItem.class, Expeditech.MODID);

    public static final RegistryObject<LockedItem> SANDING_PAPER = register(() -> ModItems.SANDING_PAPER, 100);

    private static RegistryObject<LockedItem> register(Supplier<RegistryObject<? extends Item>> item, int techXp){
        return LOCKED_ITEMS.register("test", () -> new LockedItem(item, techXp));
    }

    public static boolean isLocked(ItemStack stack){
        for (RegistryObject<LockedItem> entry : LOCKED_ITEMS.getEntries()) {
            if(entry.get().getStack() == stack.getItem()) return true;
        }

        return false;
    }

    public static int getRequiredXp(ItemStack stack){
        for (RegistryObject<LockedItem> entry : LOCKED_ITEMS.getEntries()) {
            LockedItem lockedItem = entry.get();
            if(lockedItem.getStack() == stack.getItem()) return lockedItem.getTechXp();
        }

        return -1;
    }

    // TODO: CHange that ("test")
    public static void register(IEventBus eBus){
        LOCKED_ITEMS.makeRegistry("test", RegistryBuilder::new);
        LOCKED_ITEMS.register(eBus);
    }
}
