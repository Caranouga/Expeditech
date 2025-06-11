package fr.caranouga.expeditech.registry;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.containers.CoalGeneratorContainer;
import fr.caranouga.expeditech.containers.SandingMachineContainer;
import fr.caranouga.expeditech.screens.SandingMachineScreen;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ModContainers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Expeditech.MODID);

    public static final RegistryObject<ContainerType<CoalGeneratorContainer>> COAL_GENERATOR_CONTAINER = register("coal_generator_container",
            () -> IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.level;
                return new CoalGeneratorContainer(windowId, world, pos, inv, inv.player);
            })));
    public static final RegistryObject<ContainerType<SandingMachineContainer>> SANDING_MACHINE_CONTAINER = register("sanding_machine_container",
            () -> IForgeContainerType.create(((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                World world = inv.player.level;
                return new SandingMachineContainer(windowId, world, pos, inv, inv.player);
            })));

    // region Utility methods
    private static <T extends ContainerType<? extends Container>> RegistryObject<T> register(String name, Supplier<T> tileEntityType) {
        return CONTAINERS.register(name, tileEntityType);
    }

    public static void register(IEventBus eventBus) {
        CONTAINERS.register(eventBus);
    }
}
