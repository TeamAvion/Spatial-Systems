package com.avion.spatialsystems.tile;

import com.avion.spatialsystems.util.RefHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import java.util.List;

@SuppressWarnings({"ConstantConditions", "unchecked"})
public class TileChestBinder extends BoundTileImpl implements IInventory, ISidedInventory{
    public void _triggerBreak(){
        TileEntity te = world.getTileEntity(boundSource);
        if(te instanceof TileAdvancedChest) ((TileAdvancedChest) te).triggerBreak(pos);
    }

    @Override
    public int[] getSlotsForFace(EnumFacing side) {
        if(boundSource==null) return new int[0];
        List<ItemStack> inventory = (List<ItemStack>) RefHelper.getValue("inventory", world.getTileEntity(boundSource),TileAdvancedChest.class);
        int[] i = new int[inventory.size()];
        for(int j = 0; j<i.length; ++j) i[j] = inventory.get(j).getCount();
        return i;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
        if(boundSource==null) return false;
        List<ItemStack> inventory = (List<ItemStack>) RefHelper.getValue("inventory", world.getTileEntity(boundSource),TileAdvancedChest.class);
        ItemStack s = inventory.get(index);
        return s.isEmpty() || (s.getItem() == itemStackIn.getItem() && s.getMaxStackSize() - s.getCount()>=itemStackIn.getCount());
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
        if(boundSource==null) return false;
        List<ItemStack> inventory = (List<ItemStack>) RefHelper.getValue("inventory", world.getTileEntity(boundSource),TileAdvancedChest.class);
        ItemStack i;
        return !(i=inventory.get(index)).isEmpty() && i.getItem() == stack.getItem() && i.getCount()>=stack.getCount();
    }

    @Override
    public int getSizeInventory() {
        return boundSource==null?0:((List<ItemStack>) RefHelper.getValue("inventory", world.getTileEntity(boundSource),TileAdvancedChest.class)).size();
    }

    @Override
    public boolean isEmpty() {
        if(boundSource==null) return true;
        List<ItemStack> inventory = (List<ItemStack>) RefHelper.getValue("inventory", world.getTileEntity(boundSource),TileAdvancedChest.class);
        for(ItemStack i : inventory) if(!i.isEmpty()) return false;
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if(boundSource==null) return ItemStack.EMPTY;
        List<ItemStack> inventory = (List<ItemStack>) RefHelper.getValue("inventory", world.getTileEntity(boundSource),TileAdvancedChest.class);
        return inventory.get(index).copy();
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if(boundSource==null) return ItemStack.EMPTY;
        List<ItemStack> inventory = (List<ItemStack>) RefHelper.getValue("inventory", world.getTileEntity(boundSource),TileAdvancedChest.class);
        ItemStack stack = inventory.get(index);
        ItemStack i = stack.copy();
        stack.setCount(Math.min(0, stack.getCount()-count));
        world.getTileEntity(boundSource).markDirty();
        return i.copy();
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if(boundSource==null) return ItemStack.EMPTY;
        List<ItemStack> inventory = (List<ItemStack>) RefHelper.getValue("inventory", world.getTileEntity(boundSource),TileAdvancedChest.class);
        return decrStackSize(index, inventory.get(index).getCount()).copy();
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        List<ItemStack> inventory = (List<ItemStack>) RefHelper.getValue("inventory", world.getTileEntity(boundSource),TileAdvancedChest.class);
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

    }

    @Override
    public void closeInventory(EntityPlayer player) {

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

    }

    @Override
    public String getName() {
        return "tile.boundchest";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }
}
