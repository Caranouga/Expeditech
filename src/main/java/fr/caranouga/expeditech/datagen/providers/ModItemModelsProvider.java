package fr.caranouga.expeditech.datagen.providers;

import fr.caranouga.expeditech.common.Expeditech;
import fr.caranouga.expeditech.common.content.blocks.pipes.AbstractPipeBlock;
import fr.caranouga.expeditech.common.registry.ModBlocks;
import fr.caranouga.expeditech.common.registry.ModItems;
import fr.caranouga.expeditech.common.utils.BlockEntry;
import fr.caranouga.expeditech.common.utils.BlockStateType;
import net.minecraft.block.Block;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ModItemModelsProvider extends ItemModelProvider {
    public ModItemModelsProvider(DataGenerator generator, ExistingFileHelper exFileHelper) {
        super(generator, Expeditech.MODID, exFileHelper);
    }

    @Override
    protected void registerModels() {
        ModItems.ITEMS.getEntries().forEach(entry -> {
            String name = Objects.requireNonNull(entry.get().getRegistryName()).getPath();
            if(entry.get() instanceof BlockItem){
                Block associatedBlock = ((BlockItem) entry.get()).getBlock();
                // Transform Map<RegistryObject<Block>, BlockEntry> to Map<Block, BlockEntry>
                // map the registry object to the block instance and keep the block entry as is
                Map<Block, BlockEntry> result = ModBlocks.BLOCKS_ENTRIES.keySet()
                        .stream()
                        .collect(Collectors.toMap(RegistryObject::get, ModBlocks.BLOCKS_ENTRIES::get));

                if(!result.containsKey(associatedBlock)){
                    throw new RuntimeException("Block " + associatedBlock.getRegistryName() + " is not registered in ModBlocks.BLOCKS_ENTRIES");
                }


                BlockStateType blockStateType = result.get(associatedBlock).getBlockStateType();

                switch (blockStateType){
                    case SKIP:
                    case CUBE_ALL:
                    case MACHINE_BLOCK: {
                        withExistingParent(name, modLoc("block/" + name));
                        break;
                    }

                    case PIPE_BLOCK: {
                        if (associatedBlock instanceof AbstractPipeBlock) {
                            withExistingParent(name, modLoc("block/" + name + "_core"));
                        } else {
                            throw new RuntimeException("Block " + associatedBlock.getRegistryName() + " is not an instance of AbstractMachineBlock but is registered as a PIPE_BLOCK");
                        }
                        break;
                    }
                    default: {
                        throw new RuntimeException("Unknown block state type: " + blockStateType);
                    }
                }

                /*ModBlocks.BLOCKS_ENTRIES.forEach((block, blockEntry) -> {
                    BlockStateType blockStateType = blockEntry.getBlockStateType();
                    Block blockInstance = block.get();
                    switch (blockStateType){
                        case SKIP:
                        case CUBE_ALL:
                        case MACHINE_BLOCK: {
                            withExistingParent(name, modLoc("block/" + name));
                            break;
                        }

                        case PIPE_BLOCK: {
                            if (blockInstance instanceof AbstractPipeBlock) {
                                withExistingParent(name, modLoc("block/" + name + "_core"));
                            } else {
                                throw new RuntimeException("Block " + blockInstance.getRegistryName() + " is not an instance of AbstractMachineBlock but is registered as a PIPE_BLOCK");
                            }
                            break;
                        }
                        default: {
                            throw new RuntimeException("Unknown block state type: " + blockStateType);
                        }
                    }
                });*/

                return;
            }

            ModelFile itemGenerated = getExistingFile(mcLoc("item/generated"));
            builder(itemGenerated, name);
        });
    }

    private void builder(ModelFile itemGenerated, String name) {
        getBuilder(name).parent(itemGenerated).texture("layer0", "item/" + name);
    }
}
