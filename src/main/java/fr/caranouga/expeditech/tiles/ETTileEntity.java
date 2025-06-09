package fr.caranouga.expeditech.tiles;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.configs.ServerConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TranslationTextComponent;

public abstract class ETTileEntity extends TileEntity {
    private int maxUses;

    public ETTileEntity(TileEntityType<?> tileEntityType, int maxUses) {
        super(tileEntityType);

        this.maxUses = maxUses;
    }

    // region Data Saving (World load/save)
    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        if(nbt.contains("maxUses")) {
            maxUses = nbt.getInt("maxUses");
        }

        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT pCompound) {
        pCompound.putInt("maxUses", maxUses);

        return super.save(pCompound);
    }
    // endregion

    protected void use(){
        maxUses--;

        if(maxUses <= 0 && level != null && !level.isClientSide && ServerConfig.machineDurability.get()) {
            BlockState state = level.getBlockState(getBlockPos());
            Block block = state.getBlock();

            this.level.playSound(null, getBlockPos(), SoundEvents.ANVIL_LAND, SoundCategory.BLOCKS, 1f, 1f);

            if(ServerConfig.broadcastMachineDurability.get()){
                level.getServer().getPlayerList().broadcastMessage(
                        new TranslationTextComponent(Expeditech.MODID + ".machine_broken",  block.getName(), getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ()),
                        ChatType.CHAT,
                        Util.NIL_UUID
                );
            }

            block.onRemove(state, level, getBlockPos(), state, false);
            this.level.removeBlock(getBlockPos(), false);
        }
    }
}
