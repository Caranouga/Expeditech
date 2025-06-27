package fr.caranouga.expeditech.common.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.caranouga.expeditech.common.content.containers.CoalGeneratorContainer;
import fr.caranouga.expeditech.common.screens.widgets.ProgressBarWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class CoalGeneratorScreen extends AbstractMachineScreen<CoalGeneratorContainer> {
    private final ProgressBarWidget progressBarWidget = new ProgressBarWidget(48, 18, 0x3da000);
    private final ProgressBarWidget energyBarWidget = new ProgressBarWidget(48, 62, 0xa02000);

    public CoalGeneratorScreen(CoalGeneratorContainer pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle) {
        super(pMenu, pPlayerInventory, pTitle, "coal_generator");
    }

    @Override
    protected void renderBg(MatrixStack pMatrixStack, float pPartialTicks, int pX, int pY) {
        super.renderBg(pMatrixStack, pPartialTicks, pX, pY);

        int i = this.leftPos;
        int j = this.topPos;

        // Render the progress bar
        this.progressBarWidget.render(pMatrixStack, i, j, this.menu.getScaledProgress());

        // Render the energy bar
        this.energyBarWidget.render(pMatrixStack, i, j, this.menu.getScaledEnergy());
    }

    @Override
    protected void renderTooltip(MatrixStack pPoseStack, int pX, int pY) {
        if(pX >= this.leftPos + 48 && pX <= this.leftPos + 48 + ProgressBarWidget.WIDTH &&
           pY >= this.topPos + 18 && pY <= this.topPos + 18 + ProgressBarWidget.HEIGHT) {
            int timeRemaining = this.menu.getProgress();
            int totalTime = this.menu.getMaxProgress();
            int percentage = (int) (100 * (totalTime - (this.menu.getMaxProgress() - this.menu.getProgress())) / (totalTime + 0.0000001));

            renderTooltip(pPoseStack, getTooltipText("progress", timeRemaining, totalTime, percentage), pX, pY);
        }
        if(pX >= this.leftPos + 48 && pX <= this.leftPos + 48 + ProgressBarWidget.WIDTH &&
           pY >= this.topPos + 62 && pY <= this.topPos + 62 + ProgressBarWidget.HEIGHT) {
            int energyStored = this.menu.getEnergyStored();
            int energyCapacity = this.menu.getMaxEnergyStored();

            renderTooltip(pPoseStack, getTooltipText("energy", energyStored, energyCapacity), pX, pY);
        }

        super.renderTooltip(pPoseStack, pX, pY);
    }
}