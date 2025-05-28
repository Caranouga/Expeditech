package fr.caranouga.expeditech.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.caranouga.expeditech.registry.ModCapabilities;
import fr.caranouga.expeditech.screens.widgets.ProgressBarWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import static fr.caranouga.expeditech.utils.StringUtils.modLocation;

public class TechLevelScreen {
    private static final ResourceLocation TEXTURE = modLocation("textures/gui/widgets/progress_bar_bg.png");

    public static void render(MatrixStack pMatrixStack) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        player.getCapability(ModCapabilities.TECH_LEVEL).ifPresent(techLevel -> {
            float techLevelXp = techLevel.getTechXpToNextLevel();
            float techLevelMaxXp = techLevel.getTotalXpToNextLevel();
            if(techLevelMaxXp == 0) return;
            float xpBarWidth = techLevelXp / techLevelMaxXp;

            mc.getTextureManager().bind(TEXTURE);

            AbstractGui.blit(pMatrixStack, 0, 0, 0, 0, 82, 8, 82, 8);
            new ProgressBarWidget(1, 1, 0xFFEE00).render(pMatrixStack, 0, 0, xpBarWidth);
        });
    }

    public static void register(){
        MinecraftForge.EVENT_BUS.register(new TechLevelScreen());
    }
}
