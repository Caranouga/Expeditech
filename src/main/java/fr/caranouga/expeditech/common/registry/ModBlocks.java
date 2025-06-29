package fr.caranouga.expeditech.common.registry;

import fr.caranouga.expeditech.common.Expeditech;
import fr.caranouga.expeditech.common.content.blocks.PipeTypes;
import fr.caranouga.expeditech.common.content.blocks.machines.AbstractMachineBlock;
import fr.caranouga.expeditech.common.content.blocks.machines.CoalGeneratorMachine;
import fr.caranouga.expeditech.common.content.blocks.machines.SandingMachine;
import fr.caranouga.expeditech.common.content.blocks.mb.MasterMbBlock;
import fr.caranouga.expeditech.common.content.blocks.mb.SlaveMbBlock;
import fr.caranouga.expeditech.common.content.blocks.pipes.AbstractPipeBlock;
import fr.caranouga.expeditech.common.content.blocks.pipes.energy.IronEnergyPipe;
import fr.caranouga.expeditech.common.utils.BlockEntry;
import fr.caranouga.expeditech.common.utils.BlockStateType;
import fr.caranouga.expeditech.common.utils.LootTypeEntry;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.item.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static fr.caranouga.expeditech.common.registry.ModItems.registerItem;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Expeditech.MODID);
    public static final Map<RegistryObject<? extends Block>, BlockEntry> BLOCKS_ENTRIES = new HashMap<>();

    // Storage blocks
    public static final RegistryObject<Block> CARANITE_BLOCK = registerStorageBlock("caranite_block", AbstractBlock.Properties.of(Material.METAL)
                .strength(5.0F, 6.0F)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)
                .requiresCorrectToolForDrops()
    );
    public static final RegistryObject<Block> COPPER_BLOCK = registerStorageBlock("copper_block", AbstractBlock.Properties.of(Material.METAL)
            .strength(5.0F, 6.0F)
            .harvestTool(ToolType.PICKAXE)
            .harvestLevel(1)
            .requiresCorrectToolForDrops()
    );

    // Ores
    public static final RegistryObject<Block> CARANITE_ORE = registerOre("caranite_ore", AbstractBlock.Properties.of(Material.STONE)
            .strength(3.0F, 3.0F)
            .harvestTool(ToolType.PICKAXE)
            .harvestLevel(2)
            .requiresCorrectToolForDrops(), ModItems.IMPURE_CARANITE);
    public static final RegistryObject<Block> COPPER_ORE = registerOre("copper_ore", AbstractBlock.Properties.of(Material.STONE)
            .strength(3.0F, 3.0F)
            .harvestTool(ToolType.PICKAXE)
            .harvestLevel(1)
            .requiresCorrectToolForDrops());

    // Machines
    public static final RegistryObject<CoalGeneratorMachine> COAL_GENERATOR = registerMachineBlock("coal_generator", CoalGeneratorMachine::new);
    public static final RegistryObject<SandingMachine> SANDING_MACHINE = registerMachineBlock("sanding_machine", SandingMachine::new);

    // Pipes
    public static final RegistryObject<IronEnergyPipe> IRON_ENERGY_PIPE = registerPipe("iron", PipeTypes.ENERGY, IronEnergyPipe::new);

    // Multiblocks
    public static final RegistryObject<MasterMbBlock> MB_MASTER = registerMb("mb_master", MasterMbBlock::new);

    // Internal blocks
    public static final RegistryObject<SlaveMbBlock> MB_SLAVE = registerSlaveMb(SlaveMbBlock::new);


    // region Utility methods
    private static <T extends Block> RegistryObject<T> registerMb(String name, Supplier<T> blockSupplier) {
        RegistryObject<T> block = BLOCKS.register(name, blockSupplier);

        // Register the block item
        registerItemBlock(name, block);
        addBlockEntry(block, new BlockEntry(new LootTypeEntry(LootTypeEntry.LootType.DROP_SELF), BlockStateType.CUBE_ALL));

        return block;
    }

    private static RegistryObject<SlaveMbBlock> registerSlaveMb(Supplier<SlaveMbBlock> blockSupplier){
        RegistryObject<SlaveMbBlock> block = BLOCKS.register("mb_slave", blockSupplier);

        // We do not need to register the item block for SlaveMbBlock, as it is not intended to be placed by players.
        addBlockEntry(block, new BlockEntry(new LootTypeEntry(LootTypeEntry.LootType.DO_NOT_DROP), BlockStateType.SKIP));

        return block;
    }

    private static <T extends AbstractPipeBlock> RegistryObject<T> registerPipe(String name, PipeTypes type, Supplier<T> blockSupplier){
        String formattedName = type.getName(name) + "_pipe";
        RegistryObject<T> block = BLOCKS.register(formattedName, blockSupplier);

        // Register the block item
        registerItemBlock(formattedName, block);
        addBlockEntry(block, new BlockEntry(new LootTypeEntry(LootTypeEntry.LootType.DROP_SELF), BlockStateType.PIPE_BLOCK));

        return block;
    }

    private static <T extends AbstractMachineBlock> RegistryObject<T> registerMachineBlock(String name, Supplier<T> blockSupplier) {
        RegistryObject<T> block = BLOCKS.register(name, blockSupplier);

        // Register the block item
        registerItemBlock(name, block);
        addBlockEntry(block, new BlockEntry(new LootTypeEntry(LootTypeEntry.LootType.DROP_SELF), BlockStateType.MACHINE_BLOCK));

        return block;
    }

    private static RegistryObject<Block> registerOre(String name, AbstractBlock.Properties properties, RegistryObject<Item> drop) {
        RegistryObject<Block> block = BLOCKS.register(name, () -> new Block(properties));

        // Register the block item
        registerItemBlock(name, block);
        addBlockEntry(block, new BlockEntry(new LootTypeEntry(LootTypeEntry.LootType.ORE_DROP, drop), BlockStateType.CUBE_ALL));

        return block;
    }

    private static RegistryObject<Block> registerOre(String name, AbstractBlock.Properties properties) {
        RegistryObject<Block> block = BLOCKS.register(name, () -> new Block(properties));

        // Register the block item
        registerItemBlock(name, block);
        addBlockEntry(block, new BlockEntry(new LootTypeEntry(LootTypeEntry.LootType.DROP_SELF), BlockStateType.CUBE_ALL));

        return block;
    }

    private static RegistryObject<Block> registerStorageBlock(String name, AbstractBlock.Properties properties){
        RegistryObject<Block> block = BLOCKS.register(name, () -> new Block(properties));

        // Register the block item
        registerItemBlock(name, block);
        addBlockEntry(block, new BlockEntry(new LootTypeEntry(LootTypeEntry.LootType.DROP_SELF), BlockStateType.CUBE_ALL));

        return block;
    }

    // Utils
    private static <T extends Block> void registerItemBlock(String name, RegistryObject<T> block) {
        registerItem(name, () -> new BlockItem(block.get(), new Item.Properties().tab(ModTabs.EXPEDITECH)));
    }
    private static <T extends Block> void addBlockEntry(RegistryObject<T> block, BlockEntry entry) {
        BLOCKS_ENTRIES.put(block, entry);
    }
    public static boolean never(BlockState state, IBlockReader world, BlockPos pos, EntityType<?> entType) {
        return false;
    }
    // endregion

    public static void register(IEventBus eBus){
        BLOCKS.register(eBus);
    }
}
