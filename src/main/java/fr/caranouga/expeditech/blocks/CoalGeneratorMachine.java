package fr.caranouga.expeditech.blocks;

import fr.caranouga.expeditech.tiles.CoalGeneratorMachineTile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class CoalGeneratorMachine extends Block {
    private final BooleanProperty lit = BlockStateProperties.LIT;

    public CoalGeneratorMachine() {
        super(AbstractBlock.Properties.of(Material.METAL)
                .strength(5.0F, 6.0F)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)
                .requiresCorrectToolForDrops());

        this.registerDefaultState(this.stateDefinition.any().setValue(lit, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(lit);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new CoalGeneratorMachineTile();
    }
}
