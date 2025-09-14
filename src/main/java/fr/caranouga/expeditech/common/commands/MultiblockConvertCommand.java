package fr.caranouga.expeditech.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.caranouga.expeditech.common.Expeditech;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.text.StringTextComponent;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static net.minecraft.state.StateHolder.PROPERTY_ENTRY_TO_STRING_FUNCTION;

public class MultiblockConvertCommand {
    public MultiblockConvertCommand(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> literalCommandNode = dispatcher.register(
                Commands.literal("mbconvert")
                        .then(Commands.argument("from", BlockPosArgument.blockPos()).then(Commands.argument("to", BlockPosArgument.blockPos()).then(Commands.argument("name", StringArgumentType.string()).executes((cmd) -> {
                            return convertMultiblock(cmd.getSource(), new MutableBoundingBox(BlockPosArgument.getLoadedBlockPos(cmd, "from"), BlockPosArgument.getLoadedBlockPos(cmd, "to")), StringArgumentType.getString(cmd, "name"));
                        }))))
        );

        dispatcher.register(Commands.literal("mbsetup").redirect(literalCommandNode));
    }

    private static int convertMultiblock(CommandSource source, MutableBoundingBox area, String name) {
        if (source.getLevel().isClientSide()) {
            source.sendFailure(new StringTextComponent("This command can only be run on the server side."));
            return 0;
        }

        HashMap<BlockState, Character> mappings = getMappings(source, area);
        if(mappings == null){
            source.sendFailure(new StringTextComponent("Not enough characters to generate mappings"));
            return 0;
        }

        StringBuilder mappingsString = new StringBuilder();
        mappings.forEach((state, character) -> {
            mappingsString.append("               \"").append(character).append("\": \"").append(state.getBlock().getRegistryName());

            if(!state.getValues().isEmpty()) {
                    mappingsString.append("[").append(state.getValues().entrySet().stream().map(PROPERTY_ENTRY_TO_STRING_FUNCTION).collect(Collectors.joining(",")))
                        .append("]");
            }

            mappingsString.append("\"\n");
        });

        Expeditech.LOGGER.debug(mappingsString);

        int ySize = Math.abs(area.y1 - area.y0) + 1;
        int xSize = Math.abs(area.x1 - area.x0) + 1;
        int zSize = Math.abs(area.z1 - area.z0) + 1;

        //       y,x,z
        char[][][] layers = new char[xSize][ySize][zSize];

        for(int y = 0; y <= Math.abs(area.y0 - area.y1); y++){
            for(int x = 0; x <= Math.abs(area.x0 - area.x1); x++){
                for(int z = 0; z <= Math.abs(area.z0 - area.z1); z++){
                    layers[y][x][z] = mappings.get(source.getLevel().getBlockState(new BlockPos(area.x0 + x, area.y0 + y, area.z0 + z)));
                }
            }
        }

        // TODO: Finish this

        /*source.getServer().storageSource.getLevelPath(FolderName.GENERATED_DIR).normalize();
        Path pathToConvertedFile = pLevelStorage.getLevelPath(FolderName.GENERATED_DIR).normalize();*/

        /*Expeditech.LOGGER.info("\n{\n" +
                "        \"type\": \"patchouli:multiblock\",\n" +
                "        \"name\": \"" + name + "\",\n" +
                "        \"multiblock_id\": \"multiblock\",\n" +
                "        \"multiblock\": {\n" +
                "            \"pattern\": [\n" +
                "                [\"SSS\", \"SSS\", \"SSS\"],\n" +
                "                [\"SSS\", \"SSM\", \"SSS\"],\n" +
                "                [\"SSS\", \"SS0\", \"SSS\"]\n" +
                "            ],\n" +
                "            \"mapping\": {\n" +
                "                \"S\": \"minecraft:stone\",\n" +
                "                \"M\": \"et:mb_master\",\n" +
                "                \"0\": \"minecraft:stone\"\n" +
                "            },\n" +
                "            \"symmetrical\": false\n" +
                "        },\n" +
                "        \"enable_visualize\": true,\n" +
                "        \"text\": \"The Caranite Multiblock is used to process Caranite and Impure Caranite into pure Caranite ingots. It requires a redstone signal to operate.\"\n" +
                "}");*/
        Expeditech.LOGGER.info("\n{\n" +
                "        \"type\": \"patchouli:multiblock\",\n" +
                "        \"name\": \"" + name + "\",\n" +
                "        \"multiblock_id\": \"multiblock\",\n" +
                "        \"multiblock\": {\n" +
                "            \"pattern\": [\n" +
                "                [\"SSS\", \"SSS\", \"SSS\"],\n" +
                "                [\"SSS\", \"SSM\", \"SSS\"],\n" +
                "                [\"SSS\", \"SS0\", \"SSS\"]\n" +
                "            ],\n" +
                "            \"mapping\": {\n" + mappingsString +
                "            },\n" +
                "            \"symmetrical\": false\n" +
                "        },\n" +
                "        \"enable_visualize\": true,\n" +
                "        \"text\": \"The Caranite Multiblock is used to process Caranite and Impure Caranite into pure Caranite ingots. It requires a redstone signal to operate.\"\n" +
                "}");



        return 1;
    }

    private static HashMap<BlockState, Character> getMappings(CommandSource source, MutableBoundingBox area){
        HashMap<BlockState, Character> added = new HashMap<>();
        List<Character> availableCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".chars().mapToObj(c -> (char) c).collect(Collectors.toList());

        for(BlockPos blockpos : BlockPos.betweenClosed(area.x0, area.y0, area.z0, area.x1, area.y1, area.z1)) {
            BlockState state = source.getLevel().getBlockState(blockpos);

            if(!added.containsKey(state)){
                if(availableCharacters.isEmpty()) return null;
                Character currentChar = availableCharacters.remove(0);

                added.put(state, currentChar);
            }
        }

        return added;
    }
}
