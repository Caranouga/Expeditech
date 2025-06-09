package fr.caranouga.expeditech.datagen.providers.advancements;

import fr.caranouga.expeditech.capability.techlevel.TechLevelImplementation;
import fr.caranouga.expeditech.triggers.TechLevelTrigger;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.item.Item;

import static fr.caranouga.expeditech.utils.StringUtils.modLocation;

public class AdvancementUtils {
    protected static String id(String s) {
        return modLocation(s).toString();
    }

    protected ItemPredicate[] getItemPredicate(Item... items) {
        ItemPredicate[] predicates = new ItemPredicate[items.length];
        for (int i = 0; i < items.length; i++) {
            predicates[i] = ItemPredicate.Builder.item().of(items[i]).build();
        }
        return predicates;
    }

    protected static Advancement.Builder techLevelAdvancement(int required, boolean isLevels) {
        int requiredTechXp = isLevels ? TechLevelImplementation.getXpForLevel(required) : required;

        return Advancement.Builder.advancement().addCriterion("0", new TechLevelTrigger.Instance(requiredTechXp));
    }
}
