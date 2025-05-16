package fr.caranouga.expeditech.screens.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;

import static fr.caranouga.expeditech.utils.StringUtils.modLocation;

public class ProgressBarWidget extends AbstractWidget {
    private static final ResourceLocation PROGRESS_BAR_TEXTURE = modLocation("textures/gui/widgets/progress_bar.png");

    private static final int HEIGHT = 6;
    private static final int WIDTH = 80;

    public ProgressBarWidget(int x, int y, int color) {
        super(x, y, HEIGHT, color);
    }

    public ProgressBarWidget(int x, int y, String hexColor) {
        super(x, y, HEIGHT, hexColor);
    }

    @Override
    public void render(MatrixStack matrixStack, int progress) {
        // Extract RGB from your `this.color` integer
        float r = ((this.color >> 16) & 0xFF) / 255f;
        float g = ((this.color >> 8) & 0xFF) / 255f;
        float b = (this.color & 0xFF) / 255f;

        RenderSystem.color4f(r, g, b, 1.0f);
        Minecraft.getInstance().getTextureManager().bind(PROGRESS_BAR_TEXTURE);

        AbstractGui.blit(matrixStack, this.x, this.y, 0, 0, progress, this.height, WIDTH, HEIGHT);

        RenderSystem.color4f(1f, 1f, 1f, 1f);
    }
}
