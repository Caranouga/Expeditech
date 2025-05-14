package fr.caranouga.expeditech.blocks;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.containers.CoalGeneratorContainer;
import fr.caranouga.expeditech.registry.ModTileEntities;
import fr.caranouga.expeditech.tiles.CoalGeneratorMachineTile;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;

public class CoalGeneratorMachine extends Block {
    public CoalGeneratorMachine() {
        super(AbstractBlock.Properties.of(Material.METAL)
                .strength(5.0F, 6.0F)
                .harvestTool(ToolType.PICKAXE)
                .harvestLevel(2)
                .requiresCorrectToolForDrops());
    }

    @Override
    public ActionResultType use(BlockState pState, World pLevel, BlockPos pPos, PlayerEntity pPlayer, Hand pHand, BlockRayTraceResult pHit) {
        if(!pLevel.isClientSide()) {
            TileEntity tileEntity = pLevel.getBlockEntity(pPos);

            if(tileEntity instanceof CoalGeneratorMachineTile) {
                INamedContainerProvider containerProvider = createContainerProvider(pLevel, pPos);

                NetworkHooks.openGui((ServerPlayerEntity) pPlayer, containerProvider, tileEntity.getBlockPos());
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return ModTileEntities.COAL_GENERATOR_TILE.get().create();
    }

    private INamedContainerProvider createContainerProvider(World pLevel, BlockPos pPos) {
        return new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new TranslationTextComponent("screen." + Expeditech.MODID + ".coal_generator");
            }

            @Nullable
            @Override
            public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
                return new CoalGeneratorContainer(i, pLevel, pPos, playerInventory, playerEntity);
            }
        };
    }
}
