package fr.caranouga.expeditech.registry;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.blocks.CoalGeneratorMachine;
import fr.caranouga.expeditech.utils.BlockEntry;
import fr.caranouga.expeditech.utils.LootTypeEntry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.*;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static fr.caranouga.expeditech.registry.ModItems.registerItem;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Expeditech.MODID);
    public static final Map<RegistryObject<Block>, BlockEntry> BLOCKS_ENTRIES = new HashMap<>();

    // TODO: Check si les blocks ont le bon tool et level

    // Storage blocks
    public static final RegistryObject<Block> CARANITE_BLOCK = registerStorageBlock("caranite_block");

    // Ores
    public static final RegistryObject<Block> CARANITE_ORE = registerOre("caranite_ore", ModItems.IMPURE_CARANITE);

    // Machines
    public static final RegistryObject<Block> COAL_GENERATOR = registerMachineBlock("coal_generator", CoalGeneratorMachine::new);

    // region Utility methods
    private static <T extends Block> RegistryObject<Block> registerMachineBlock(String name, Supplier<T> blockSupplier) {
        RegistryObject<Block> block = BLOCKS.register(name, blockSupplier);

        // Register the block item
        registerItemBlock(name, block);
        addBlockEntry(block, new BlockEntry(new LootTypeEntry(LootTypeEntry.LootType.DROP_SELF)));

        return block;
    }

    private static RegistryObject<Block> registerOre(String name, RegistryObject<Item> drop) {
        return registerOre(name, AbstractBlock.Properties.of(Material.STONE)
                .strength(3.0F, 3.0F)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)
                .requiresCorrectToolForDrops(), drop);
    }

    private static RegistryObject<Block> registerOre(String name, AbstractBlock.Properties properties, RegistryObject<Item> drop) {
        RegistryObject<Block> block = BLOCKS.register(name, () -> new Block(properties));

        // Register the block item
        registerItemBlock(name, block);
        addBlockEntry(block, new BlockEntry(new LootTypeEntry(LootTypeEntry.LootType.ORE_DROP, drop)));

        return block;
    }

    private static RegistryObject<Block> registerStorageBlock(String name) {
        return registerStorageBlock(name, AbstractBlock.Properties.of(Material.METAL)
                .strength(5.0F, 6.0F)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)
                .requiresCorrectToolForDrops()
        );
    }

    private static RegistryObject<Block> registerStorageBlock(String name, AbstractBlock.Properties properties){
        RegistryObject<Block> block = BLOCKS.register(name, () -> new Block(properties));

        // Register the block item
        registerItemBlock(name, block);
        addBlockEntry(block, new BlockEntry(new LootTypeEntry(LootTypeEntry.LootType.DROP_SELF)));

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
    private static void addBlockEntry(RegistryObject<Block> block, BlockEntry entry) {
        BLOCKS_ENTRIES.put(block, entry);
    }
    // endregion

    public static void register(IEventBus eBus){
        BLOCKS.register(eBus);
    }
}
