package fr.caranouga.expeditech.registry;

import fr.caranouga.expeditech.screens.CoalGeneratorScreen;
import fr.caranouga.expeditech.screens.SandingMachineScreen;
import fr.caranouga.expeditech.screens.TechLevelScreen;
import net.minecraft.client.gui.ScreenManager;

public class ModScreens {
    public static void register() {
        ScreenManager.register(ModContainers.COAL_GENERATOR_CONTAINER.get(), CoalGeneratorScreen::new);
        ScreenManager.register(ModContainers.SANDING_MACHINE_CONTAINER.get(), SandingMachineScreen::new);
        TechLevelScreen.register();
    }
}
