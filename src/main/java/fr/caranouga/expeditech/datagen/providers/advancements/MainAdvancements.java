package fr.caranouga.expeditech.datagen.providers.advancements;

import fr.caranouga.expeditech.common.registry.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.TickTrigger;
import net.minecraft.item.Items;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Consumer;

import static fr.caranouga.expeditech.datagen.providers.advancements.AdvancementUtils.*;
import static fr.caranouga.expeditech.common.utils.StringUtils.modLocation;

public class MainAdvancements implements Consumer<Consumer<Advancement>> {
    @Override
    public void accept(Consumer<Advancement> advancementConsumer) {
        Advancement root = advancement("main", "root", ModItems.CARANITE.get(),
                modLocation("textures/block/caranite_block.png"),
                FrameType.GOAL, false, false, false)
                .addCriterion("on_start", new TickTrigger.Instance(EntityPredicate.AndPredicate.ANY))
                .save(advancementConsumer, id("root"));

        Advancement firstTechXp = generateDisplay(techLevelAdvancement(1, false), "main",
                "first_tech_xp", ModItems.CARANITE.get(), FrameType.GOAL, true, true,
                false)
                .parent(root)
                .save(advancementConsumer, id("first_tech_xp"));

        Advancement firstTechLevel = generateDisplay(techLevelAdvancement(1, true), "main",
                "first_tech_level", ModItems.CARANITE.get(), FrameType.GOAL, true, true,
                false)
                .parent(firstTechXp)
                .save(advancementConsumer, id("first_tech_level"));

        Advancement techLevelCheater = techLevelAdvancement(Integer.MAX_VALUE, false)
                .display(Items.COMMAND_BLOCK,
                        new TranslationTextComponent("advancements.expeditech.tech_level_cheater.title"),
                        new TranslationTextComponent("advancements.expeditech.tech_level_cheater.description"),
                        null,
                        FrameType.CHALLENGE,
                        true, true, true)
                .parent(firstTechLevel)
                .save(advancementConsumer, id("tech_level_cheater"));

    }
}
