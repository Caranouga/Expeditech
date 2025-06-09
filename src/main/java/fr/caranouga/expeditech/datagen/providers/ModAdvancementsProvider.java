package fr.caranouga.expeditech.datagen.providers;

import com.google.common.collect.ImmutableList;
import fr.caranouga.expeditech.datagen.providers.advancements.MainAdvancements;
import net.minecraft.advancements.Advancement;
import net.minecraft.data.AdvancementProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.function.Consumer;

public class ModAdvancementsProvider extends AdvancementProvider {
    private final List<Consumer<Consumer<Advancement>>> advancementTabs = ImmutableList.of(
            new MainAdvancements()
            // Add other advancement providers here
    );

    public ModAdvancementsProvider(DataGenerator generatorIn, ExistingFileHelper fileHelperIn) {
        super(generatorIn, fileHelperIn);
    }

    @Override
    protected void registerAdvancements(Consumer<Advancement> consumer, ExistingFileHelper fileHelper) {
        for(Consumer<Consumer<Advancement>> advConsumer : this.advancementTabs){
            advConsumer.accept(consumer);
        }
    }
}
