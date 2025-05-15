package fr.caranouga.expeditech.blocks;

import fr.caranouga.expeditech.containers.CoalGeneratorContainer;
import fr.caranouga.expeditech.registry.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class CoalGeneratorMachine extends AbstractMachineBlock {
    public CoalGeneratorMachine() {
        super("coal_generator");
    }

    @Override
    protected TileEntityType<? extends TileEntity> getTileEntityType(BlockState state, IBlockReader world) {
        return ModTileEntities.COAL_GENERATOR_TILE.get();
    }

    @Override
    protected Container getContainer(int i, World pLevel, BlockPos pPos, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CoalGeneratorContainer(i, pLevel, pPos, playerInventory, playerEntity);
    }
}