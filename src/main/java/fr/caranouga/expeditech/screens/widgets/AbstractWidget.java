package fr.caranouga.expeditech.screens.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.gui.drawable.IDrawableAnimated;

public abstract class AbstractWidget {
    protected final int x;
    protected final int y;
    protected final int height;
    protected final int color;

    protected AbstractWidget(int x, int y, int height, int color) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.color = color;
    }

    protected void setColor() {
        float r = ((this.color >> 16) & 0xFF) / 255f;
        float g = ((this.color >> 8) & 0xFF) / 255f;
        float b = (this.color & 0xFF) / 255f;

        RenderSystem.color4f(r, g, b, 1.0f);
    }

    protected void resetColor() {
        RenderSystem.color4f(1f, 1f, 1f, 1f);
    }


    //public abstract void render(MatrixStack matrixStack, float progress);
    public abstract void render(MatrixStack matrixStack, int i, int j, float progress);
}
