/*package fr.caranouga.expeditech.tiles;

import fr.caranouga.expeditech.Expeditech;
import fr.caranouga.expeditech.blocks.EnergyStorages;
import fr.caranouga.expeditech.capability.CustomEnergyStorage;
import fr.caranouga.expeditech.registry.ModTileEntities;
import fr.caranouga.expeditech.tiles.machines.AbstractEnergyMachineTile;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.util.Constants;

public class VoidMachineTile extends AbstractEnergyMachineTile implements ITickableTileEntity {

    public VoidMachineTile() {
        super(ModTileEntities.VOID_MACHINE_TILE.get(), 0, 0);
    }

    @Override
    protected CustomEnergyStorage createEnergyStorage() {
        return new CustomEnergyStorage(50000, 50000, 0);
    }

    @Override
    public void tick() {
        if(level == null || level.isClientSide) {
            return;
        }

        setChanged();
    }
}*/