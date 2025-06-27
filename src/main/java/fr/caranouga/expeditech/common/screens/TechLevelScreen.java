package fr.caranouga.expeditech.common.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.caranouga.expeditech.common.registry.ModCapabilities;
import fr.caranouga.expeditech.common.screens.widgets.ProgressBarWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

import static fr.caranouga.expeditech.common.utils.StringUtils.modLocation;

public class TechLevelScreen {
    private static final ResourceLocation TEXTURE = modLocation("textures/gui/widgets/progress_bar_bg.png");

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

            AbstractGui.blit(pMatrixStack, 0, 0, 0, 0, 82, 8, 82, 8);
            new ProgressBarWidget(1, 1, 0xFFEE00).render(pMatrixStack, 0, 0, xpBarWidth);
            // TODO: Render the tech level text
        });
    }

    public static void register(){
        MinecraftForge.EVENT_BUS.register(new TechLevelScreen());
    }
}
