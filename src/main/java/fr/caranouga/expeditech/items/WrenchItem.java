package fr.caranouga.expeditech.items;

import fr.caranouga.expeditech.tiles.mb.MasterMbTile;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WrenchItem extends Item {
    public WrenchItem(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResultType onItemUseFirst(ItemStack stack, ItemUseContext context) {
        BlockPos pos = context.getClickedPos();
        TileEntity tileEntity = context.getLevel().getBlockEntity(pos);
        World world = context.getLevel();
        Direction clickedFace = context.getClickedFace();

        if(world.isClientSide()) return ActionResultType.PASS;

        if(tileEntity instanceof MasterMbTile){
            MasterMbTile mbTile = (MasterMbTile) tileEntity;

            mbTile.tryBuild(clickedFace.getOpposite());
        }

        return ActionResultType.PASS;
    }
}
