package com.avion.spatialsystems.tile;

import com.avion.spatialsystems.container.ContainerAdvancedChest;
import com.avion.spatialsystems.util.RefHelper;
import com.avion.spatialsystems.util.WorldHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class TileAdvancedChest extends TileEntity implements IInventory, ISidedInventory{

    private volatile NonNullList<ItemStack> inventory = NonNullList.withSize(54, ItemStack.EMPTY);
    protected final ArrayList<EntityPlayer> tracker = new ArrayList<EntityPlayer>();
    protected ContainerAdvancedChest pageTracker = null;
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

    public void setSize(int size){
        NonNullList<ItemStack> n = NonNullList.withSize(size, ItemStack.EMPTY);
        int min = Math.min(n.size(), inventory.size());
        for(int i = 0; i<min; ++i) n.set(i, inventory.get(i));
        inventory = n;
        markDirty();
        if(pageTracker!=null) pageTracker.setupSlots();
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

    @Override public ItemStack getStackInSlot(int index) {
        try {
            if(TileEntity.class.isAssignableFrom(RefHelper.getCallerClass())) System.out.println(RefHelper.getCallerClass());
            return inventory.get(index + (currentPage - 1) * 54 - (currentPage==1?0:1));
        }catch(Exception e){
            e.printStackTrace();
            return ItemStack.EMPTY;
        }
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = inventory.get(index + (currentPage - 1) * 54 - (currentPage==1?0:1));
        ItemStack i = stack.copy();
        stack.setCount(Math.min(0, stack.getCount()-count));
        markDirty();
        return i;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return decrStackSize(index + (currentPage - 1) * 54 - (currentPage==1?0:1), inventory.get(index).getCount());
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        inventory.set(index + (currentPage - 1) * 54 - (currentPage==1?0:1), stack);
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return world.getTileEntity(getPos()) == this && player.getDistanceSq(getPos().getX() + 0.5, getPos().getY() + 0.5,getPos().getZ() + 0.5) < 64;
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

    @Override public void clear() { inventory.clear(); }

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
        if(FMLCommonHandler.instance().getSide()==Side.SERVER && pageTracker!=null) pageTracker.setupSlots();
        //sync();
    }



    public void sync() {
        this.markDirty();
        IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
    }

    public void registerPageTracker(ContainerAdvancedChest pageTracker){
        this.pageTracker = pageTracker;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        int[] i = new int[inventory.size()];
        for(int j = 0; j<i.length; ++j) i[j] = inventory.get(j).getCount();
        return i;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        ItemStack s = inventory.get(index);
        return s.isEmpty() || (s.getItem() == itemStackIn.getItem() && s.getMaxStackSize() - s.getCount()>=itemStackIn.getCount());
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        ItemStack i;
        return !(i=inventory.get(index)).isEmpty() && i.getItem() == stack.getItem() && i.getCount()>=stack.getCount();
    }
}
