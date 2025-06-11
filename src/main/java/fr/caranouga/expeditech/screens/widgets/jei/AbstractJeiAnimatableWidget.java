package fr.caranouga.expeditech.screens.widgets.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.caranouga.expeditech.screens.widgets.AbstractWidget;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;

public abstract class AbstractJeiAnimatableWidget extends AbstractWidget {
    protected IDrawable drawable;

    protected AbstractJeiAnimatableWidget(int x, int y, int height, int color) {
        super(x, y, height, color);
    }

    public void renderAnimated(MatrixStack matrixStack){
        setColor();

        this.drawable.draw(matrixStack, this.x, this.y);

        resetColor();
    }

    public void renderStatic(MatrixStack matrixStack) {
        // Same as renderAnimated but renamed to avoid confusion
        renderAnimated(matrixStack);
    }

    protected void setDrawable(IDrawable drawable) {
        this.drawable = drawable;
    }

    public abstract AbstractJeiAnimatableWidget createAnimated(IGuiHelper helper, float progress, int time);
    public abstract AbstractJeiAnimatableWidget createStatic(IGuiHelper helper, float progress);
}
