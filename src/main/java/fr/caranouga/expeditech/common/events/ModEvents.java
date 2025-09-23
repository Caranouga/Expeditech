package fr.caranouga.expeditech.common.events;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import fr.caranouga.expeditech.client.ClientState;
import fr.caranouga.expeditech.client.screens.TechLevelScreen;
import fr.caranouga.expeditech.common.Expeditech;
import fr.caranouga.expeditech.common.capability.techlevel.TechLevelProvider;
import fr.caranouga.expeditech.common.capability.techlevel.TechLevelUtils;
import fr.caranouga.expeditech.common.commands.MultiblockConvertCommand;
import fr.caranouga.expeditech.common.commands.MultiblockSetupCommand;
import fr.caranouga.expeditech.common.commands.TechLevelCommand;
import fr.caranouga.expeditech.common.entities.TechLevelExperienceOrb;
import fr.caranouga.expeditech.common.registry.ModCapabilities;
import fr.caranouga.expeditech.common.registry.ModEntityTypes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static fr.caranouga.expeditech.common.registry.ModCapabilities.TECH_LEVEL_ID;

@Mod.EventBusSubscriber(modid = Expeditech.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEvents {
}
