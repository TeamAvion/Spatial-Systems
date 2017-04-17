package com.avion.spatialsystems.tile;

import com.avion.spatialsystems.blocks.ModBlocks;
import com.avion.spatialsystems.blocks.Properties;
import com.avion.spatialsystems.util.WorldHelper;
import com.avion.spatialsystems.util.LogHelper;
import com.avion.spatialsystems.util.MiscHelper;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

//Created by Bread10 at 10:20 on 15/04/2017
public class TileAdvancedChest extends TileEntity {

    private NonNullList<ItemStack> inventory = NonNullList.withSize(27 * 9, ItemStack.EMPTY);

    private BlockPos bottomNorthWestCorner = WorldHelper.NULL;
    private int dimension = 0;

    public void findMultiBlockStructure() {
        EnumFacing blockFacing = this.getWorld().getBlockState(this.getPos()).getValue(Properties.FACING);
        for (EnumFacing f : MiscHelper.exceptArray(EnumFacing.values(), blockFacing)) {
            LogHelper.info(f.getName()+" -> "+this.getWorld().getBlockState(this.getPos().offset(f)).getBlock().equals(ModBlocks.advancedChestBlock));
            if (!this.getWorld().getBlockState(this.getPos().offset(f)).getBlock().equals(ModBlocks.advancedChestBlock)) {
                structureNotFound();
                return;
            }
        }

        EnumFacing back = blockFacing.getOpposite();
        BlockPos currPos = WorldHelper.copy(this.getPos());
        int dim = 0;
        for (int i = 0; i < 16; i++) {
            currPos = currPos.offset(back);
            if (this.getWorld().getBlockState(currPos).getBlock() != ModBlocks.advancedChestBlock) {
                dim = i + 1;
                break;
            }
        }

        if (dim < 3) {
            structureNotFound();
            return;
        }

        Axis[] axesToCheck = MiscHelper.exceptArray(EnumFacing.Axis.values(), back.getAxis()).toArray(new Axis[0]);
        for (Axis axis : axesToCheck) {
            EnumFacing[] directionsToCheck = WorldHelper.getDirectionsFromAxis(axis);
            for (EnumFacing f : directionsToCheck) {

            }
        }
    }

    private void structureNotFound() {
        LogHelper.warning("Multiblock structure not found!");
    }

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
