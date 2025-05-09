package fr.caranouga.expeditech.registry;

import fr.caranouga.expeditech.Expeditech;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static fr.caranouga.expeditech.registry.ModItems.registerItem;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Expeditech.MODID);

    public static final RegistryObject<Block> CARANITE_BLOCK = registerBlock("caranite_block");
    // Storage blocks
    public static final RegistryObject<Block> CARANITE_BLOCK = registerStorageBlock("caranite_block");

    // Ores
    public static final RegistryObject<Block> CARANITE_ORE = registerOre("caranite_ore", ModItems.IMPURE_CARANITE);

    // region Utility methods
    private static RegistryObject<Block> registerBlock(String name) {
    private static RegistryObject<Block> registerOre(String name, RegistryObject<Item> drop) {
        RegistryObject<Block> block = BLOCKS.register(name, () -> new Block(AbstractBlock.Properties.of(Material.STONE)));

        // Register the block item
        registerItemBlock(name, block);

        return block;
    }

    private static RegistryObject<Block> registerStorageBlock(String name) {
        RegistryObject<Block> block = BLOCKS.register(name, () -> new Block(AbstractBlock.Properties.of(Material.METAL)));

        // Register the block item
        registerItemBlock(name, block);

        return block;
    }

    /*private static RegistryObject<Block> registerBlock(String name) {
        RegistryObject<Block> block = BLOCKS.register(name, () -> new Block(AbstractBlock.Properties.of(Material.METAL)));

        // Register the block item
        registerItemBlock(name, block);

        return block;
    }*/

    // Utils
    private static void registerItemBlock(String name, RegistryObject<Block> block) {
        registerItem(name, () -> new BlockItem(block.get(), new Item.Properties().tab(ModTabs.EXPEDITECH)));
    }
    // endregion

    public static void register(IEventBus eBus){
        BLOCKS.register(eBus);
    }
}
