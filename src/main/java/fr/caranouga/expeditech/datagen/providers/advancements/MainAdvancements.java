package fr.caranouga.expeditech.datagen.providers.advancements;

import fr.caranouga.expeditech.triggers.TechLevelTrigger;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.TickTrigger;
import net.minecraft.item.Items;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.function.Consumer;

import static fr.caranouga.expeditech.datagen.providers.advancements.AdvancementUtils.id;
import static fr.caranouga.expeditech.datagen.providers.advancements.AdvancementUtils.techLevelAdvancement;

public class MainAdvancements implements Consumer<Consumer<Advancement>> {
    @Override
    public void accept(Consumer<Advancement> advancementConsumer) {
        Advancement root = Advancement.Builder.advancement()
                .display(Items.ACACIA_LEAVES,
                        new TranslationTextComponent("advancements.expeditech.root.title"),
                        new TranslationTextComponent("advancements.expeditech.root.description"),
                        null,
                        FrameType.GOAL,
                        true, true, false)
                .addCriterion("on_start", new TickTrigger.Instance(EntityPredicate.AndPredicate.ANY))
                .save(advancementConsumer, id("root"));

        Advancement techLevelCheater = techLevelAdvancement(Integer.MAX_VALUE, false)
                .display(Items.COMMAND_BLOCK,
                        new TranslationTextComponent("advancements.expeditech.tech_level_cheater.title"),
                        new TranslationTextComponent("advancements.expeditech.tech_level_cheater.description"),
                        null,
                        FrameType.CHALLENGE,
                        true, true, true)
                .parent(root)
                .save(advancementConsumer, id("tech_level_cheater"));

    }
}
