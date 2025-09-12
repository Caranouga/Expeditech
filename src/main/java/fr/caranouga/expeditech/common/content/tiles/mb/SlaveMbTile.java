package fr.caranouga.expeditech.common.content.tiles.mb;

import fr.caranouga.expeditech.common.registry.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;

// TODO: Ajouter inté theoneprobe (afficher le nom du multiblock et son état (complet ou non)) et pas slaveTile

public class SlaveMbTile extends TileEntity {
    private AbstractMultiblockTile masterTile;
    private BlockState originalBlock;

    private BlockPos pendingMasterPos = null;

    public SlaveMbTile() {
        super(ModTileEntities.MB_SLAVE.get());
    }

    public void setMaster(BlockPos masterPos, World masterWorld) {
        TileEntity tile = masterWorld.getBlockEntity(masterPos);
        if (tile instanceof AbstractMultiblockTile) {
            this.masterTile = (AbstractMultiblockTile) tile;
        } else {
            throw new IllegalArgumentException("The provided master position does not contain a MasterMbTile.");
        }

        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.NOTIFY_NEIGHBORS);
        setChanged();
    }

    public void setOriginalBlock(BlockState state){
        this.originalBlock = state;

        level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.NOTIFY_NEIGHBORS);
        setChanged();
    }

    public void broken(){
        if (masterTile != null) {
            masterTile.slaveBroken(this);
        }
    }

    @Override
    public void setRemoved() {
        broken();
        super.setRemoved();
    }

    @Nullable
    public BlockState getOriginalBlockState(){
        return originalBlock;
    }

    // region Data Saving (World load/save)
    // 2 fois: server puis client
    @Override
    public void onLoad() {
        super.onLoad();

        if (pendingMasterPos != null && this.level != null) {
            setMaster(pendingMasterPos, this.level);
            masterTile.registerNewSlave(this);
            pendingMasterPos = null; // Clear after setting
        }

        this.level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE | Constants.BlockFlags.NOTIFY_NEIGHBORS);
        setChanged();
    }

    @Override
    public void load(BlockState state, CompoundNBT nbt) {
        super.load(state, nbt);

        readTagServer(nbt);
        readTagClient(nbt);
    }

    @Override
    public CompoundNBT save(CompoundNBT pCompound) {
        CompoundNBT tag = createTagServer(super.save(pCompound));
        return createTagClient(tag);
    }

    // serverside
    @Override
    public SUpdateTileEntityPacket getUpdatePacket(){
        return new SUpdateTileEntityPacket(getBlockPos(), -1, createTagClient(new CompoundNBT()));
    }

    // clientside
    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
        CompoundNBT tag = pkt.getTag();

        readTagClient(tag);
    }

    // serverside
    // We use the default handleUpdateTag
    @Override
    public CompoundNBT getUpdateTag() {
        return createTagClient(super.getUpdateTag());
    }

    private CompoundNBT createTagClient(CompoundNBT tag){
        if(originalBlock != null) tag.put("ogBlock", NBTUtil.writeBlockState(originalBlock));

        return tag;
    }

    private CompoundNBT createTagServer(CompoundNBT tag){
        if(masterTile != null) tag.put("masterPos", NBTUtil.writeBlockPos(masterTile.getBlockPos()));

        return tag;
    }

    private void readTagServer(CompoundNBT tag){
        if (tag.contains("masterPos")) {
            pendingMasterPos = NBTUtil.readBlockPos(tag.getCompound("masterPos"));
        }
    }

    private void readTagClient(CompoundNBT tag){
        if (tag.contains("ogBlock")) {
            originalBlock = NBTUtil.readBlockState(tag.getCompound("ogBlock"));
        }
    }
    // endregion
}