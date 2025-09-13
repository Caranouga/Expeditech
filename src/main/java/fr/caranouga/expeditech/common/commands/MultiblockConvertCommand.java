package fr.caranouga.expeditech.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import fr.caranouga.expeditech.common.Expeditech;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.command.arguments.BlockStateInput;
import net.minecraft.command.arguments.MessageArgument;
import net.minecraft.inventory.IClearable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.storage.FolderName;

import java.nio.file.Path;

public class MultiblockConvertCommand {
    public MultiblockConvertCommand(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> literalCommandNode = dispatcher.register(
                Commands.literal("mbconvert")
                        .then(Commands.argument("from", BlockPosArgument.blockPos()).then(Commands.argument("to", BlockPosArgument.blockPos()).then(Commands.argument("name", StringArgumentType.string())).executes((cmd) -> {
                            return convertMultiblock(cmd.getSource(), new MutableBoundingBox(BlockPosArgument.getLoadedBlockPos(cmd, "from"), BlockPosArgument.getLoadedBlockPos(cmd, "to")), StringArgumentType.getString(cmd, "name"));
                        })))
        );

        dispatcher.register(Commands.literal("mbsetup").redirect(literalCommandNode));
    }

    private static int convertMultiblock(CommandSource source, MutableBoundingBox area, String name) {
        if (source.getLevel().isClientSide()) {
            source.sendFailure(new StringTextComponent("This command can only be run on the server side."));
            return 0;
        }

        StringBuilder pattern = new StringBuilder();
        for(BlockPos blockpos : BlockPos.betweenClosed(area.x0, area.y0, area.z0, area.x1, area.y1, area.z1)) {

        }

        /*source.getServer().storageSource.getLevelPath(FolderName.GENERATED_DIR).normalize();
        Path pathToConvertedFile = pLevelStorage.getLevelPath(FolderName.GENERATED_DIR).normalize();*/

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
                "            \"mapping\": {\n" +
                "                \"S\": \"minecraft:stone\",\n" +
                "                \"M\": \"et:mb_master\",\n" +
                "                \"0\": \"minecraft:stone\"\n" +
                "            },\n" +
                "            \"symmetrical\": false\n" +
                "        },\n" +
                "        \"enable_visualize\": true,\n" +
                "        \"text\": \"The Caranite Multiblock is used to process Caranite and Impure Caranite into pure Caranite ingots. It requires a redstone signal to operate.\"\n" +
                "}");



        return 1;
    }
}
