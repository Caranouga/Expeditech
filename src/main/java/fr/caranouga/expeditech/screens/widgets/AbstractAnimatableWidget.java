package fr.caranouga.expeditech.screens.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;

public abstract class AbstractAnimatableWidget extends AbstractWidget {
    protected IDrawableAnimated drawable;

    protected AbstractAnimatableWidget(int x, int y, int height, int color) {
        super(x, y, height, color);
    }

    public void renderAnimated(MatrixStack matrixStack){
        setColor();

        this.drawable.draw(matrixStack, this.x, this.y);

        resetColor();
    }

    protected void setDrawable(IDrawableAnimated drawable) {
        this.drawable = drawable;
    }

    public abstract AbstractAnimatableWidget createAnimated(IGuiHelper helper, float progress, int time);
}
