package com.avion.spatialsystems.tile;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandlerModifiable;
import javax.annotation.Nonnull;

public class TileAdvancedFurnace extends TileEntity implements IItemHandlerModifiable, IInventory, ITickable{

    // Values defined for getting info from this class
    public static final int FIELD_TIME = 0x00;
    public static final int FIELD_MAXTIME = 0x01;
    public static final int FIELD_BURN = 0x02;
    public static final int FIELD_MAXBURN = 0x03;

    /**
     * Determines the characteristics of this tile. A higher value means a greater complexity.
     */
    protected final int complexity;
    /**
     * Stack 0: Input
     * Stack 1: Fuel
     * Stack 2: Output
     */
    protected final NonNullList<ItemStack> stacks = NonNullList.withSize(3, ItemStack.EMPTY);
    protected int cookTime = 0;
    protected int maxCookTime;
    protected int burnTime = 0;
    protected int itemMaxBurn;

    public TileAdvancedFurnace(int complexity){
        this.complexity = complexity; maxCookTime = 160/complexity;
    } // Calculate the cook time based on complexity

    @Override
    public void update() {

    }

    @Override public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        setInventorySlotContents(slot, stack);
    }
    @Override public int getSlots() {
        return 3;
    }
    @Override public int getSizeInventory() {
        return 3;
    }
    @Override public boolean isEmpty() {
        for (ItemStack stack : stacks) {
            if (!stack.isEmpty())
                return false;
        }
        return true;
    }
    @Nonnull @Override public ItemStack getStackInSlot(int slot) { return stacks.get(slot); }

    @MethodsReturnNonnullByDefault
    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = stacks.get(index);
        ItemStack i = new ItemStack(stack.getItem(), Math.min(stack.getCount(), count));
        stack.shrink(i.getCount());
        return i;
    }
    @MethodsReturnNonnullByDefault @Override public ItemStack removeStackFromSlot(int index) { return decrStackSize(index, stacks.get(index).getCount()); }
    @Override public void setInventorySlotContents(int index, ItemStack stack) { stacks.set(index, stack); }
    @Override public int getInventoryStackLimit() { return 64; }

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
        return (index == 1 && (TileEntityFurnace.isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack))) || (index==0 || index==2);
    }

    @Override
    public int getField(int id) { return id==FIELD_TIME?cookTime:id==FIELD_MAXTIME?maxCookTime:id==FIELD_BURN?burnTime:itemMaxBurn; }

    @Override
    public void setField(int id, int value) {
        if(id==FIELD_TIME) cookTime = value;
        else if(id==FIELD_MAXTIME) maxCookTime = value;
        else if(id==FIELD_BURN) burnTime = value;
        else if(id==FIELD_MAXBURN) itemMaxBurn = value;
    }

    @Override public int getFieldCount() {
        return 3;
    }

    @Override public void clear() {
        stacks.clear();
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        return null;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if(stacks.get(slot).isEmpty()) return ItemStack.EMPTY;
        ItemStack s = getStackInSlot(slot);
        if (simulate) stacks.set(slot, s);
        return s;
    }

    @Override public int getSlotLimit(int slot) {
        return !stacks.get(slot).isEmpty() ? stacks.get(slot).getMaxStackSize() : 64;
    }

    @Override public String getName() {
        return "Level " + complexity + " furnace";
    }

    @Override public boolean hasCustomName() {
        return false;
    }

}
