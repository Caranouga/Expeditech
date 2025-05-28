package fr.caranouga.expeditech.registry;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.items.FuelItem;
import fr.caranouga.expeditech.items.SandingPaperItem;
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
    public static final RegistryObject<Item> IMPURE_CARANITE = registerItem("impure_caranite");
    public static final RegistryObject<Item> CARANITE_DUST = registerItem("caranite_dust");
    public static final RegistryObject<Item> CARANITE_NUGGET = registerItem("caranite_nugget");
    public static final RegistryObject<Item> CARANITE_COAL = registerItem("caranite_coal",
            () -> new FuelItem(new Item.Properties().tab(ModTabs.EXPEDITECH), 3200 /* 160 seconds */));

    // TODO: Add custom animation for the sanding paper
    public static final RegistryObject<SandingPaperItem> SANDING_PAPER = registerItem("sanding_paper",
            () -> new SandingPaperItem(new Item.Properties().tab(ModTabs.EXPEDITECH)));

    // region Utility methods
    private static RegistryObject<Item> registerItem(String name) {
        return registerItem(name, ModTabs.EXPEDITECH);
    }

    private static RegistryObject<Item> registerItem(String name, ItemGroup group) {
        return registerItem(name, () -> new Item(new Item.Properties().tab(group)));
    }

    protected static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> supplier) {
        return ITEMS.register(name, supplier);
    }
    // endregion

    public static void register(IEventBus eBus){
        ITEMS.register(eBus);
    }
}
