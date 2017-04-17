package com.avion.spatialsystems.tile;

import com.avion.spatialsystems.util.BlockPosHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

//Created by Bread10 at 10:20 on 15/04/2017
public class TileAdvancedChest extends TileEntity {

    private NonNullList<ItemStack> inventory = NonNullList.withSize(27 * 9, ItemStack.EMPTY);

    private BlockPos bottomNorthWestCorner = BlockPosHelper.NULL;
    private int dimension = 0;

    @Override
    public NBTTagCompound getUpdateTag() {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);

        ItemStackHelper.loadAllItems(compound, inventory);

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        ItemStackHelper.saveAllItems(compound, inventory);

        return compound;
    }

    public NonNullList<ItemStack> getInventory() {
        return inventory;
    }
}
