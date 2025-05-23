package fr.caranouga.expeditech.registry;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.blocks.pipes.PipeType;
import fr.caranouga.expeditech.tiles.machines.CoalGeneratorMachineTile;
import fr.caranouga.expeditech.tiles.pipe.energy.IronEnergyPipeTile;
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

    // Pipes
    public static final RegistryObject<TileEntityType<IronEnergyPipeTile>> IRON_ENERGY_PIPE_TILE = registerPipe("iron", PipeType.ENERGY,
            () -> TileEntityType.Builder.of(IronEnergyPipeTile::new, ModBlocks.IRON_ENERGY_PIPE.get()).build(null));

    // region Utility methods
    private static <T extends TileEntityType<? extends TileEntity>> RegistryObject<T> registerPipe(String name, PipeType type, Supplier<T> tileEntityType) {
        return TILE_ENTITIES.register(type.getName(name), tileEntityType);
    }

    private static <T extends TileEntityType<? extends TileEntity>> RegistryObject<T> register(String name, Supplier<T> tileEntityType) {
        return TILE_ENTITIES.register(name, tileEntityType);
    }

    public static void register(IEventBus eventBus) {
        TILE_ENTITIES.register(eventBus);
    }
}
