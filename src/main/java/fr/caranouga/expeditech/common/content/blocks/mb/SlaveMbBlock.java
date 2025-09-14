package fr.caranouga.expeditech.common.content.blocks.mb;

import fr.caranouga.expeditech.common.Expeditech;
import fr.caranouga.expeditech.common.content.tiles.mb.SlaveMbTile;
import fr.caranouga.expeditech.common.registry.ModBlocks;
import fr.caranouga.expeditech.common.registry.ModTileEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class SlaveMbBlock extends Block {
    public static final IntegerProperty TOOL_TYPE = IntegerProperty.create("tool_type", 0, 4);
    // TODO: Change that
    public static final IntegerProperty HARVEST_LEVEL = IntegerProperty.create("harvest_level", 0, 10);

    public SlaveMbBlock() {
        super(
                Properties.of(Material.BARRIER)
                        //.strength(-1.0F, 3600000.0F)
                        .isValidSpawn(ModBlocks::never)
        );

        registerDefaultState(this.stateDefinition.any()
                .setValue(TOOL_TYPE, 0)
                .setValue(HARVEST_LEVEL, 0));
    }

    // region BlockState
    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(TOOL_TYPE, HARVEST_LEVEL);
    }
    // endregion

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.MB_SLAVE.get().create();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public ItemStack getPickBlock(BlockState state, RayTraceResult target, IBlockReader world, BlockPos pos, PlayerEntity player) {
        Block originalBlock = getOriginalBlock(world, pos);
        if (originalBlock != null) return originalBlock.getPickBlock(state, target, world, pos, player);

        return super.getPickBlock(state, target, world, pos, player);
    }

    @Nullable
    private SlaveMbTile getSlaveTile(IBlockReader world, BlockPos pos) {
        TileEntity tile = world.getBlockEntity(pos);
        if (tile instanceof SlaveMbTile) {
            return (SlaveMbTile) tile;
        }
        return null;
    }

    @Nullable
    private Block getOriginalBlock(IBlockReader world, BlockPos pos){
        /*SlaveMbTile tile = getSlaveTile(world, pos);
        if(tile != null){
            BlockState state = tile.getOriginalBlockState();
            if(state != null) return state.getBlock();
            return null;
        }

        return null;*/
        BlockState state = getOriginalBlockstate(world, pos);
        if(state != null) return state.getBlock();
        return null;
    }

    @Nullable
    private BlockState getOriginalBlockstate(IBlockReader world, BlockPos pos){
        SlaveMbTile tile = getSlaveTile(world, pos);
        if(tile != null){
            return tile.getOriginalBlockState();
        }

        return null;
    }

    // TODO: Find a way to make this work
    @Override
    public float getExplosionResistance(BlockState state, IBlockReader world, BlockPos pos, Explosion explosion) {
            Expeditech.LOGGER.debug("getexplres");
        // TODO: idem que en bas
        Block originalBlock = getOriginalBlock(world, pos);
        if (originalBlock != null) return originalBlock.getExplosionResistance(state, world, pos, explosion);

        return super.getExplosionResistance(state, world, pos, explosion);
    }

    @Override
    public float getDestroyProgress(BlockState state, PlayerEntity player, IBlockReader world, BlockPos pos) {
        // TODO: Check si ça marche + patch sinon
        BlockState originalState = getOriginalBlockstate(world, pos);
        if(originalState != null) super.getDestroyProgress(originalState, player, world, pos);
        return super.getDestroyProgress(state, player, world, pos);
    }

    @Nullable
    @Override
    public ToolType getHarvestTool(BlockState state) {
        return convertToolType(state.getValue(TOOL_TYPE));
    }

    @Override
    public int getHarvestLevel(BlockState state) {
        return state.getValue(HARVEST_LEVEL);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState pState) {
        return PushReaction.BLOCK; // Prevents the block from being pushed by pistons
    }

    private ToolType convertToolType(int toolTypeIdx){
        switch (toolTypeIdx){
            case 1: return ToolType.AXE;
            case 2: return ToolType.PICKAXE;
            case 3: return ToolType.SHOVEL;
            case 4: return ToolType.HOE;
            default: return null; // also takes care of 0 case
        }
    }

    public static int convertToolType(@Nullable ToolType toolType){
        if(toolType == null) return 0;
        if (toolType.equals(ToolType.AXE)) {
            return 1;
        } else if (toolType.equals(ToolType.PICKAXE)) {
            return 2;
        } else if (toolType.equals(ToolType.SHOVEL)) {
            return 3;
        } else if (toolType.equals(ToolType.HOE)) {
            return 4;
        }

        return 0;
    }
}
