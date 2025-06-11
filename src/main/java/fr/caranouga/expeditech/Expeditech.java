package fr.caranouga.expeditech;

import fr.caranouga.expeditech.configs.ClientConfig;
import fr.caranouga.expeditech.configs.CommonConfig;
import fr.caranouga.expeditech.configs.ServerConfig;
import fr.caranouga.expeditech.registry.*;
import fr.caranouga.expeditech.triggers.AdvancementTriggers;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static fr.caranouga.expeditech.utils.StringUtils.modLocation;

@Mod(Expeditech.MODID)
public class Expeditech
{
    // TODO: Advancements
    // TODO: Patchouli (changer texture du livre)
    // TODO: Add different level of pipes (copper, iron, gold, diamond, netherite)
    // each with different speed and capacity
    // TODO: Add a way to limit transfer rate of items/fluids/energy
    // TODO: Changer la texture de la sanding machine
    // TODO: Ajouter une anim sur le feu du coal generator
    // TODO: Use tout les pipes dans les grids pas seulement le master pipe

    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "et";

    public static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel NETWORK = NetworkRegistry.newSimpleChannel(
            modLocation("channel"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public Expeditech() {
        IEventBus modEBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModLoadingContext modLoadingCtx = ModLoadingContext.get();

        modEBus.addListener(this::setup);
        modEBus.addListener(this::enqueueIMC);
        modEBus.addListener(this::processIMC);
        modEBus.addListener(this::doClientStuff);

        // Registries
        ModBlocks.register(modEBus);
        ModItems.register(modEBus);
        ModRecipes.register(modEBus);
        ModTileEntities.register(modEBus);
        ModContainers.register(modEBus);

        MinecraftForge.EVENT_BUS.register(this);

        modLoadingCtx.registerConfig(ModConfig.Type.COMMON, CommonConfig.COMMON_CONFIG, MODID + "-common.toml");
        modLoadingCtx.registerConfig(ModConfig.Type.CLIENT, ClientConfig.CLIENT_CONFIG, MODID + "-client.toml");
        modLoadingCtx.registerConfig(ModConfig.Type.SERVER, ServerConfig.SERVER_CONFIG, MODID + "-server.toml");

    }

    private void setup(final FMLCommonSetupEvent event) {
        ModCapabilities.register();
        ModPackets.register();
        AdvancementTriggers.registerTriggers();
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            ModScreens.register();
            ModKeybinds.register();
        });
    }

    private void enqueueIMC(final InterModEnqueueEvent event) {}

    private void processIMC(final InterModProcessEvent event) {}
}
