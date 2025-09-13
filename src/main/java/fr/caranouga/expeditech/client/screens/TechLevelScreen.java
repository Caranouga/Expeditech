package fr.caranouga.expeditech.client.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.caranouga.expeditech.common.registry.ModCapabilities;
import fr.caranouga.expeditech.client.screens.widgets.ProgressBarWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import static fr.caranouga.expeditech.common.utils.StringUtils.modLocation;

public class TechLevelScreen {
    private static final ResourceLocation TEXTURE = modLocation("textures/gui/widgets/progress_bar_bg.png");

    private static final int progressBarX = 1;
    private static final int progressBarY = 1;
    private static final ProgressBarWidget progressBarWidget = new ProgressBarWidget(progressBarX, progressBarY, 0xFFEE00);

    public static void render(MatrixStack pMatrixStack) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        if(player == null || !player.isAlive()) return;

        player.getCapability(ModCapabilities.TECH_LEVEL).ifPresent(techLevel -> {
            float techLevelXp = techLevel.getTechXpToNextLevel();
            float techLevelMaxXp = techLevel.getTotalXpToNextLevel();
            if(techLevelMaxXp == 0) return;
            float xpBarWidth = techLevelXp / techLevelMaxXp;

            mc.getTextureManager().bind(TEXTURE);

            int bgWidth = ProgressBarWidget.WIDTH + 2 * progressBarX;
            int bgHeight = ProgressBarWidget.HEIGHT + 2 * progressBarY;

            AbstractGui.blit(pMatrixStack, 0, 0, 0, 0, bgWidth, bgHeight, bgWidth, bgHeight);
            progressBarWidget.render(pMatrixStack, 0, 0, xpBarWidth);

            mc.font.drawShadow(pMatrixStack, techLevel.getTechLevel() + "", bgWidth + 1, progressBarY, 0xFFFFFF);
        });
    }

    public static void register(){
        MinecraftForge.EVENT_BUS.register(new TechLevelScreen());
    }
}
