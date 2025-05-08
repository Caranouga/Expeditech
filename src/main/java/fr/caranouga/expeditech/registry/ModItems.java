package fr.caranouga.expeditech.registry;

import fr.caranouga.expeditech.Expeditech;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Expeditech.MODID);

    public static final RegistryObject<Item> CARANITE = registerItem("caranite");

    // region Utility methods
    private static RegistryObject<Item> registerItem(String name) {
        return ITEMS.register(name, () -> new Item(new Item.Properties()));
    }
    // endregion

    public static void register(IEventBus eBus){
        ITEMS.register(eBus);
    }
}
