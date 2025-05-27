package fr.caranouga.expeditech.screens;

import com.mojang.blaze3d.matrix.MatrixStack;
import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.registry.ModCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Expeditech.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class Truc {
    private static final ResourceLocation gui = new ResourceLocation(Expeditech.MODID, "textures/gui/techlevel.png");
    private static final int WIDTH = 60;
    private static final int HEIGHT = 20;
    private static final int START_X = 0; // SUR L'ECRANT
    private static final int START_Y = 0; // SUR L'ECRANT

    @SubscribeEvent
    public static void onRenderGameOverlay(RenderGameOverlayEvent event) {
        if(event.getType() == RenderGameOverlayEvent.ElementType.EXPERIENCE){
            renderCustomXpBar(event.getMatrixStack());
        }
    }

    public static void register() {
        MinecraftForge.EVENT_BUS.register(new Truc());
    }

    private static void renderCustomXpBar(MatrixStack matrixStack) {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        player.getCapability(ModCapabilities.TECH_LEVEL).ifPresent(techLevel -> {
            int techLevelXp = techLevel.getTechXpToNextLevel();
            int techLevelMaxXp = techLevel.getTotalXpToNextLevel();
            if(techLevelMaxXp == 0) return;
            int xpBarWidth = (techLevelXp * WIDTH) / techLevelMaxXp;
            mc.getTextureManager().bind(gui);
            // 0, 64 -> sur la texture
            AbstractGui.blit(matrixStack, START_X, START_Y, 0, 20, WIDTH, HEIGHT, 256, 256);
            AbstractGui.blit(matrixStack, START_X, START_Y, 0, 0, xpBarWidth, HEIGHT, 256,256);
        });
    }

}
