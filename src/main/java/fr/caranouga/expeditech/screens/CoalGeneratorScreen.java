package fr.caranouga.expeditech.screens;

import fr.caranouga.expeditech.containers.CoalGeneratorContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class CoalGeneratorScreen extends AbstractMachineScreen<CoalGeneratorContainer> {
    public CoalGeneratorScreen(CoalGeneratorContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
        super(pMenu, pPlayerInventory, pTitle, "coal_generator");
    }
}