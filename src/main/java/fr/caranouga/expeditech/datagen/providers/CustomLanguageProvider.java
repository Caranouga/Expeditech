package fr.caranouga.expeditech.datagen.providers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fr.caranouga.expeditech.Expeditech;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import org.apache.commons.lang3.text.translate.JavaUnicodeEscaper;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

public abstract class CustomLanguageProvider implements IDataProvider {
    private static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().disableHtmlEscaping().create();
    // <lang, <key, value>>
    private final Map<String, Map<String, String>> data = new TreeMap<>();
    private final DataGenerator gen;
    private final String[] locales;
    private int currentLocaleIdx;

    public CustomLanguageProvider(DataGenerator gen, String... locales) {
        this.gen = gen;
        this.locales = locales;
        this.currentLocaleIdx = 0;
    }

    protected abstract void addTranslations();

    @Override
    public void run(DirectoryCache pCache) throws IOException {
        addTranslations();
        for(String locale : locales){
            if(data.isEmpty()) continue;
            save(pCache, data.get(locale), this.gen.getOutputFolder().resolve("assets/" + Expeditech.MODID + "/lang/" + locale + ".json"));
        }
    }

    @Override
    public String getName() {
        return Expeditech.MODID + " - Language Provider";
    }

    private void save(DirectoryCache cache, Map<String, String> dataMap, Path path) throws IOException {
        String data = GSON.toJson(dataMap);
        data = JavaUnicodeEscaper.outsideOf(0, 0x7f).translate(data); // Escape unicode after the fact so that it's not double escaped by GSON
        String hash = IDataProvider.SHA1.hashUnencodedChars(data).toString();
        if (!Objects.equals(cache.getHash(path), hash) || !Files.exists(path)) {
            Files.createDirectories(path.getParent());

            try (BufferedWriter bufferedwriter = Files.newBufferedWriter(path)) {
                bufferedwriter.write(data);
            }
        }

        cache.putNew(path, hash);
    }

    protected void switchLocale(){
        this.currentLocaleIdx++;
        if(this.currentLocaleIdx >= this.locales.length){
            this.currentLocaleIdx = 0;
            Expeditech.LOGGER.warn("No more locales available, switching back to default locale");
        }
    }


    protected <T extends Item> void addItem(RegistryObject<T> item, String translation){
        this.addItem(item.get(), translation);
    }

    protected void addItem(Item item, String translation){
        this.add(item.getDescriptionId(), translation);
    }

    protected <T extends Block> void addBlock(RegistryObject<T> block, String translation){
        this.addBlock(block.get(), translation);
    }

    protected void addBlock(Block block, String translation){
        this.add(block.getDescriptionId(), translation);
    }

    protected void addItemGroup(ItemGroup itemGroup, String translation){
        this.add("itemGroup." + itemGroup.getRecipeFolderName(), translation);
    }

    protected void addJeiCategory(String category, String translation){
        this.add("jei." + Expeditech.MODID + "." + category, translation);
    }

    protected void addJeiTooltip(String category, String key, String translation){
        this.addJeiCategory(category + ".tooltips." + key, translation);
    }

    protected void add(String key, String value){
        String currentLocale = this.locales[this.currentLocaleIdx];
        this.data.computeIfAbsent(currentLocale, k -> new TreeMap<>());
        if(this.data.get(currentLocale).put(key, value) != null){
            throw new IllegalStateException("Duplicate translation key " + key);
        }
    }
}
