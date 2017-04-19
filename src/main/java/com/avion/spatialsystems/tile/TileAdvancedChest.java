package com.avion.spatialsystems.tile;

import com.avion.spatialsystems.util.WorldHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.ArrayList;

//Created by Bread10 at 10:20 on 15/04/2017
public class TileAdvancedChest extends TileEntity implements IInventory {

    private NonNullList<ItemStack> inventory = NonNullList.withSize(84, ItemStack.EMPTY);
    protected final ArrayList<EntityPlayer> tracker = new ArrayList<EntityPlayer>();
    private int currentPage = 1;
    private BlockPos[] bound;

    public BlockPos[] getBound(){ return bound; }
    public void bind(BlockPos[] bound){ this.bound = bound; }
    public boolean isBound(){ return bound!=null; }
    public void unbind(){ bound = null; }
    public void triggerBreak(BlockPos at){
        //InventoryHelper.dropInventoryItems(world, pos, this);
        WorldHelper.dropItem(getWorld(), at, inventory);
        for(EntityPlayer e : tracker)
            if(e.openContainer!=null) e.openContainer.onContainerClosed(e);
            else tracker.remove(e);
        markDirty();
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

        this.currentPage = compound.getInteger("page");
        ItemStackHelper.loadAllItems(compound, inventory);

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);

        compound.setInteger("page", currentPage);
        ItemStackHelper.saveAllItems(compound, inventory);

        return compound;
    }

    //* ==================== IInventory Starts Here ======================== *//


    @Override
    public int getSizeInventory() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack s : inventory) if (!s.isEmpty()) return false;
        return true;
    }

    @Override public ItemStack getStackInSlot(int index) { return inventory.get(index); }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = inventory.get(index);
        ItemStack i = new ItemStack(stack.getItem(), Math.min(stack.getCount(), count));
        stack.shrink(i.getCount());
        markDirty();
        return i;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return decrStackSize(index, inventory.get(index).getCount());
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventory.set(index, stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {
        tracker.add(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        tracker.add(player);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        inventory.clear();
    }

    @Override
    public String getName() {
        return "tile.advancedchest";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        sync();
    }

    public void sync() {
        this.markDirty();
        IBlockState state = this.getWorld().getBlockState(this.getPos());
        this.getWorld().notifyBlockUpdate(this.getPos(), state, state, 3);
    }

}
