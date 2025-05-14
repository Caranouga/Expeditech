package fr.caranouga.expeditech;

import fr.caranouga.expeditech.registry.ModBlocks;
import fr.caranouga.expeditech.registry.ModItems;
import fr.caranouga.expeditech.registry.ModRecipes;
import fr.caranouga.expeditech.registry.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Expeditech.MODID)
public class Expeditech
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "et";

    public Expeditech() {
        IEventBus modEBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEBus.addListener(this::setup);
        modEBus.addListener(this::enqueueIMC);
        modEBus.addListener(this::processIMC);
        modEBus.addListener(this::doClientStuff);

        // Registries
        ModBlocks.register(modEBus);
        ModItems.register(modEBus);
        ModRecipes.register(modEBus);
        ModTileEntities.register(modEBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {}

    private void doClientStuff(final FMLClientSetupEvent event) {}

    private void enqueueIMC(final InterModEnqueueEvent event) {}

    private void processIMC(final InterModProcessEvent event) {}

    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
        }
    }
}
