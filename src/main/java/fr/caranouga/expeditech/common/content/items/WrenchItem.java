package fr.caranouga.expeditech.common.content.items;

import fr.caranouga.expeditech.common.content.tiles.mb.AbstractMultiblockTile;
import fr.caranouga.expeditech.common.content.tiles.mb.SlaveMbTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WrenchItem extends Item {
    public WrenchItem(Properties properties) {
        super(properties.durability(60));
    }

    // TODO: Use durability
    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        TileEntity tileEntity = context.getLevel().getBlockEntity(pos);
        World world = context.getLevel();
        Direction clickedFace = context.getClickedFace();
        PlayerEntity player = context.getPlayer();

        if(world.isClientSide()) return ActionResultType.PASS;

        AbstractMultiblockTile mbTile = null;
        if(tileEntity instanceof AbstractMultiblockTile){
            mbTile = (AbstractMultiblockTile) tileEntity;

            /*boolean success = mbTile.tryBuild(clickedFace.getOpposite());

            if(success){
                // world.playSound(player, pos, SoundEvents.ANVIL_LAND, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if(player != null){
                    player.playSound(SoundEvents.ANVIL_LAND, 1.0F, 1.0F);
                    stack.hurtAndBreak(1, player, (playerEntity) -> {
                        playerEntity.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
                    });
                }
            }*/
        } else if (tileEntity instanceof SlaveMbTile) {
            SlaveMbTile slaveTile = (SlaveMbTile) tileEntity;

            mbTile = slaveTile.getMasterTile();
        }

        if(mbTile != null){
            if(mbTile.isBuilt()) return destroy(mbTile, player, stack, world, pos);
            return build(mbTile, clickedFace, player, stack, world, pos);
        }

        return ActionResultType.PASS;
    }

    private ActionResultType build(AbstractMultiblockTile mbTile, Direction clickedFace, PlayerEntity player, ItemStack stack, World world, BlockPos pos){
        boolean success = mbTile.tryBuild(clickedFace.getOpposite());

        if(success){
            onActionSuccess(player, stack, world, pos);

            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }

    private ActionResultType destroy(AbstractMultiblockTile mbTile, PlayerEntity player, ItemStack stack, World world, BlockPos pos){
        boolean success = mbTile.unform();

        if(success){
            onActionSuccess(player, stack, world, pos);

            return ActionResultType.SUCCESS;
        }

        return ActionResultType.PASS;
    }

    private void onActionSuccess(PlayerEntity player, ItemStack stack, World world, BlockPos pos){
        if(player != null){
            world.playSound(null, pos, SoundEvents.ANVIL_LAND, SoundCategory.BLOCKS, 1.0F, 1.0F);
            stack.hurtAndBreak(1, player, (playerEntity) -> {
                playerEntity.broadcastBreakEvent(EquipmentSlotType.MAINHAND);
            });
        }
    }
}
