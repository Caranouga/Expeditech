package fr.caranouga.expeditech.common.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.caranouga.expeditech.common.Expeditech;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Arrays;

import static fr.caranouga.expeditech.common.utils.StringUtils.modLocation;

public abstract class AbstractMachineScreen<C extends Container> extends ContainerScreen<C> {
    private final ResourceLocation TEXTURE;
    private final String id;

    public AbstractMachineScreen(C pMenu, PlayerInventory pPlayerInventory, ITextComponent pTitle, String id) {
        super(pMenu, pPlayerInventory, pTitle);

        this.TEXTURE = modLocation("textures/gui/container/" + id + ".png");
        this.id = id;
    }

    @Override
    protected void renderBg(MatrixStack pMatrixStack, float pPartialTicks, int pX, int pY) {
        RenderSystem.color4f(1f, 1f, 1f, 1f);

        this.minecraft.getTextureManager().bind(TEXTURE);
        int i = this.leftPos;
        int j = this.topPos;

        this.blit(pMatrixStack, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void render(MatrixStack pMatrixStack, int pMouseX, int pMouseY, float pPartialTicks) {
        this.renderBackground(pMatrixStack);
        super.render(pMatrixStack, pMouseX, pMouseY, pPartialTicks);
        this.renderTooltip(pMatrixStack, pMouseX, pMouseY);
    }

    protected ITextComponent getTooltipText(String key, Object... args) {
        // Very very hacky way to add a percentage sign at the end of the tooltip

        Object[] newArgs = Arrays.copyOf(args, args.length + 1);
        newArgs[args.length] = "%";

        return new TranslationTextComponent("tooltip." + Expeditech.MODID + "." + id + "." + key, newArgs);
    }
}
