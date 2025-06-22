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
    private int usesLeft;

    public ETTileEntity(TileEntityType<?> tileEntityType) {
        super(tileEntityType);

        if(hasDurability()) this.usesLeft = ((IHasDurability) this).getMaxUses();
        else this.usesLeft = -1; // No durability
    }

    // region Data Saving (World load/save)
    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        if(nbt.contains("maxUses") && hasDurability()) {
            usesLeft = nbt.getInt("maxUses");
        }

        super.load(state, nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT pCompound) {
        if(hasDurability()) {
            pCompound.putInt("maxUses", usesLeft);
        }

        return super.save(pCompound);
    }
    // endregion

    public void use(){
        if(!hasDurability()) {
            throw new IllegalStateException("use() can only be called on IHasDurability implementations");
        }

        usesLeft--;

        if(usesLeft <= 0 && level != null && !level.isClientSide && ServerConfig.machineDurability.get()) {
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

    private boolean hasDurability() {
        return this instanceof IHasDurability;
    }
}
