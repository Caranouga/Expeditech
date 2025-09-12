package fr.caranouga.expeditech.client.screens.widgets;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.caranouga.expeditech.client.screens.widgets.jei.AbstractJeiAnimatableWidget;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.helpers.IGuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.util.ResourceLocation;

import static fr.caranouga.expeditech.common.utils.StringUtils.modLocation;

public class ProgressBarWidget extends AbstractJeiAnimatableWidget {
    private static final ResourceLocation PROGRESS_BAR_TEXTURE = modLocation("textures/gui/widgets/progress_bar.png");

    public static final int HEIGHT = 6;
    public static final int WIDTH = 80;

    public ProgressBarWidget(int x, int y, int color) {
        super(x, y, HEIGHT, color);
    }

    @Override
    public void render(MatrixStack matrixStack, int i, int j, float progress) {
        // Extract RGB from your `this.color` integer
        setColor();

        Minecraft.getInstance().getTextureManager().bind(PROGRESS_BAR_TEXTURE);

        AbstractGui.blit(matrixStack, i + this.x, j + this.y, 0, 0, (int) (progress * WIDTH), this.height, WIDTH, HEIGHT);

        resetColor();
    }

    @Override
    public AbstractJeiAnimatableWidget createAnimated(IGuiHelper helper, float progress, int time){
        setDrawable(helper.drawableBuilder(PROGRESS_BAR_TEXTURE, 0, 0, (int) (progress * WIDTH), HEIGHT)
                .buildAnimated(time, IDrawableAnimated.StartDirection.LEFT, false));

        return this;
    }

    public AbstractJeiAnimatableWidget createAnimatedWithoutWidth(IGuiHelper helper, int progress, int time){
        setDrawable(helper.drawableBuilder(PROGRESS_BAR_TEXTURE, 0, 0, progress, HEIGHT)
                .buildAnimated(time, IDrawableAnimated.StartDirection.LEFT, false));

        return this;
    }

    @Override
    public AbstractJeiAnimatableWidget createStatic(IGuiHelper helper, float progress) {
        setDrawable(helper.drawableBuilder(PROGRESS_BAR_TEXTURE, 0, 0, (int) (progress * WIDTH), HEIGHT)
                .build());

        return this;
    }
}
