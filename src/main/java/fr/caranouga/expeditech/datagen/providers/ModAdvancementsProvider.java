package fr.caranouga.expeditech.datagen.providers;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.registry.ModItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.DisplayInfo;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.data.AdvancementProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.function.Consumer;

import static fr.caranouga.expeditech.utils.StringUtils.modLocation;

public class ModAdvancementsProvider extends AdvancementProvider {
    public ModAdvancementsProvider(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn);
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        Advancement adv = Advancement.Builder.advancement()
                .display(new DisplayInfo(
                        new ItemStack(ModItems.CARANITE.get()),
                    new TranslationTextComponent("advancements." + Expeditech.MODID + ".root.title"),
                    new TranslationTextComponent("advancements." + Expeditech.MODID + ".root.description"),
                    null,
                    FrameType.GOAL,
                    true,
                    true,
                    false
                ))
                .addCriterion("caranite", new InventoryChangeTrigger.Instance(
                        EntityPredicate.AndPredicate.ANY,
                        MinMaxBounds.IntBound.ANY,
                        MinMaxBounds.IntBound.ANY,
                        MinMaxBounds.IntBound.ANY,
                        getItemPredicate(ModItems.CARANITE.get())
                ))
                .build(getAdvancementLocation("root"));
        consumer.accept(adv);
    }

    private ResourceLocation getAdvancementLocation(String name) {
        return modLocation(name);
    }

    private ItemPredicate[] getItemPredicate(Item... items) {
        ItemPredicate[] predicates = new ItemPredicate[items.length];
        for (int i = 0; i < items.length; i++) {
            predicates[i] = ItemPredicate.Builder.item().of(items[i]).build();
        }
        return predicates;
    }
}
