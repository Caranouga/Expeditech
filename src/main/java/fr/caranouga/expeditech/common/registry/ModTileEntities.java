package fr.caranouga.expeditech.common.registry;

import fr.caranouga.expeditech.common.Expeditech;
import fr.caranouga.expeditech.common.content.blocks.PipeTypes;
import fr.caranouga.expeditech.common.content.tiles.machines.CoalGeneratorMachineTile;
import fr.caranouga.expeditech.common.content.tiles.machines.SandingMachineTile;
import fr.caranouga.expeditech.common.content.tiles.mb.MasterMbTile;
import fr.caranouga.expeditech.common.content.tiles.mb.SlaveMbTile;
import fr.caranouga.expeditech.common.content.tiles.pipes.energy.IronEnergyPipeTile;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModTileEntities {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, Expeditech.MODID);

    // Machines
    public static final RegistryObject<TileEntityType<CoalGeneratorMachineTile>> COAL_GENERATOR_TILE = register("coal_generator_tile",
            () -> TileEntityType.Builder.of(CoalGeneratorMachineTile::new, ModBlocks.COAL_GENERATOR.get()).build(null));
    public static final RegistryObject<TileEntityType<SandingMachineTile>> SANDING_MACHINE_TILE = register("sanding_machine_tile",
            () -> TileEntityType.Builder.of(SandingMachineTile::new, ModBlocks.SANDING_MACHINE.get()).build(null));

    /*public static final RegistryObject<TileEntityType<VoidMachineTile>> VOID_MACHINE_TILE = register("void_machine_tile",
            () -> TileEntityType.Builder.of(VoidMachineTile::new, ModBlocks.VOID_MACHINE.get()).build(null));*/

    // Pipes
    public static final RegistryObject<TileEntityType<IronEnergyPipeTile>> IRON_ENERGY_PIPE_TILE = registerPipe("iron",
            PipeTypes.ENERGY, () -> TileEntityType.Builder.of(IronEnergyPipeTile::new, ModBlocks.IRON_ENERGY_PIPE.get()).build(null));

    // Multiblocks
    public static final RegistryObject<TileEntityType<MasterMbTile>> MB_MASTER = register("mb_master_tile",
            () -> TileEntityType.Builder.of(MasterMbTile::new, ModBlocks.MB_MASTER.get()).build(null));

    // Internal tile entities
    public static final RegistryObject<TileEntityType<SlaveMbTile>> MB_SLAVE = register("mb_slave_tile",
            () -> TileEntityType.Builder.of(SlaveMbTile::new, ModBlocks.MB_SLAVE.get()).build(null));

    // region Utility methods
    private static <T extends TileEntityType<? extends TileEntity>> RegistryObject<T> registerPipe(String name, PipeTypes type, Supplier<T> tileEntityType) {
        return TILE_ENTITIES.register(type.getName(name) + "_pipe_tile", tileEntityType);
    }

    private static <T extends TileEntityType<? extends TileEntity>> RegistryObject<T> register(String name, Supplier<T> tileEntityType) {
        return TILE_ENTITIES.register(name, tileEntityType);
    }

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}
