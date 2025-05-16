package fr.caranouga.expeditech.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.caranouga.expeditech.containers.CoalGeneratorContainer;
import fr.caranouga.expeditech.screens.widgets.ProgressBarWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class CoalGeneratorScreen extends AbstractMachineScreen<CoalGeneratorContainer> {
    public CoalGeneratorScreen(CoalGeneratorContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
        super(pMenu, pPlayerInventory, pTitle, "coal_generator");
    }

    @Override
    protected void renderBg(MatrixStack pMatrixStack, float pPartialTicks, int pX, int pY) {
        super.renderBg(pMatrixStack, pPartialTicks, pX, pY);

        int i = this.leftPos;
        int j = this.topPos;

        // Render the progress bar
        new ProgressBarWidget(i + 48, j + 18, "3da000").render(pMatrixStack, this.menu.getScaledProgress());

        // Render the energy bar
        new ProgressBarWidget(i + 48, j + 62, "a02000").render(pMatrixStack, this.menu.getScaledEnergy());
    }
}