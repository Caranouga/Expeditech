package fr.caranouga.expeditech.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.BlockPosArgument;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;

public class MultiblockSetupCommand {
    public MultiblockSetupCommand(CommandDispatcher<CommandSource> dispatcher) {
        LiteralCommandNode<CommandSource> literalCommandNode = dispatcher.register(
                Commands.literal("mbsetup")
                        .then(Commands.argument("master", BlockPosArgument.blockPos()).then(Commands.argument("bottomLeft", BlockPosArgument.blockPos()).executes((cmd) -> {
                            return setupMultiblock(cmd.getSource(), BlockPosArgument.getLoadedBlockPos(cmd, "master"), BlockPosArgument.getLoadedBlockPos(cmd, "bottomLeft"));
                        })))
        );

        dispatcher.register(Commands.literal("mbsetup").redirect(literalCommandNode));
    }

    private static int setupMultiblock(CommandSource source, BlockPos masterPos, BlockPos bottomLeft) {
        if (source.getLevel().isClientSide()) {
            source.sendFailure(new StringTextComponent("This command can only be run on the server side."));
            return 0;
        }

        int offsetX = bottomLeft.getX() - masterPos.getX();
        int offsetY = bottomLeft.getY() - masterPos.getY();
        int offsetZ = bottomLeft.getZ() - masterPos.getZ();

        source.sendSuccess(new StringTextComponent("x:" + offsetX + " y:" + offsetY + " z:" + offsetZ), false);
        return 1;
    }
}
