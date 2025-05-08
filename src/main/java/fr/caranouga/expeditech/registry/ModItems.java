package fr.caranouga.expeditech.registry;

import fr.caranouga.expeditech.Expeditech;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Expeditech.MODID);

    public static final RegistryObject<Item> CARANITE = registerItem("caranite");

    // region Utility methods
    private static RegistryObject<Item> registerItem(String name) {
        return registerItem(name, ModTabs.EXPEDITECH);
    }

    private static RegistryObject<Item> registerItem(String name, ItemGroup group) {
        return registerItem(name, () -> new Item(new Item.Properties().tab(group)));
    }

    protected static RegistryObject<Item> registerItem(String name, Supplier<Item> supplier) {
        return ITEMS.register(name, supplier);
    }
    // endregion

    public static void register(IEventBus eBus){
        ITEMS.register(eBus);
    }
}
