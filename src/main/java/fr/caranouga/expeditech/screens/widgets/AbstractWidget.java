package fr.caranouga.expeditech.screens.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.caranouga.expeditech.utils.ColorUtils;

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

    protected AbstractWidget(int x, int y, int height, String hexColor) {
        this(x, y, height, ColorUtils.getColor(hexColor));
    }


    public abstract void render(MatrixStack matrixStack, int progress);
}
