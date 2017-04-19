package com.avion.spatialsystems.tile;

import com.avion.spatialsystems.util.WorldHelper;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.SlotFurnaceFuel;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandlerModifiable;
import javax.annotation.Nonnull;
import java.util.ArrayList;

public class TileAdvancedFurnace extends TileEntity implements IItemHandlerModifiable, IInventory, ITickable{

    // Values defined for getting info from this class
    public static final int FIELD_TIME = 0x00;
    public static final int FIELD_MAXTIME = 0x01;
    public static final int FIELD_BURN = 0x02;
    public static final int FIELD_MAXBURN = 0x03;

    /**
     * Stack 0: Input
     * Stack 1: Fuel
     * Stack 2: Output
     */
    protected NonNullList<ItemStack> stacks = NonNullList.withSize(3, ItemStack.EMPTY);
    protected final ArrayList<EntityPlayer> tracker = new ArrayList<EntityPlayer>();
    protected int cookTime = 0; // Current cook timer
    protected int maxCookTime = 100; // Time after cook starts that an item gets cooked
    protected int burnTime = -1; // Current burn timer
    protected int itemMaxBurn = -1; // Burn timer stop/reset time (relative ticks)
    protected boolean isSmelting = false;
    private BlockPos[] bound;

    @Override
    public void update() {

        // Burn time calculation
        if(burnTime>0) burnTime-=2;

        if(canConsumeFuel()){
            itemMaxBurn = TileEntityFurnace.getItemBurnTime(stacks.get(1))+2;
            burnTime = itemMaxBurn;
            stacks.get(1).shrink(1);
            markDirty();
        }

        // Smelting calculation
        boolean possible = smeltPossible();

        if(isSmelting) cookTime += 2;
        if(!isSmelting && (isSmelting=possible)){
            cookTime = 0;
            maxCookTime = 100;
        }
        else if(possible && cookTime >= maxCookTime){
            cookTime = 0;
            isSmelting = false;
            smeltItem();
        }

        if(!possible){
            cookTime = 0;
            isSmelting = false;
            markDirty();
        }
    }

    public BlockPos[] getBound(){ return bound; }
    public void bind(BlockPos[] bound){ this.bound = bound; }
    public boolean isBound(){ return bound!=null; }
    public void unbind(){ bound = null; }
    public void triggerBreak(BlockPos at){
        //InventoryHelper.dropInventoryItems(world, pos, this);
        WorldHelper.dropItem(getWorld(), at, stacks);
        for(EntityPlayer e : tracker)
            if(e.openContainer!=null) e.openContainer.onContainerClosed(e);
            else tracker.remove(e);
        markDirty();
    }

    protected boolean smeltPossible(){
        ItemStack i;
        return
                !stacks.get(0).isEmpty() &&
                (TileEntityFurnace.isItemFuel(stacks.get(1)) || burnTime>0) &&
                !(i=FurnaceRecipes.instance().getSmeltingResult(stacks.get(0))).isEmpty() &&
                (i.getItem().equals(stacks.get(2).getItem()) || stacks.get(2).isEmpty() || stacks.get(2).getItem().equals(Items.AIR)) &&
                i.getCount()+(stacks.get(2).getItem()==Items.AIR?0:stacks.get(2).getCount())<=i.getMaxStackSize(); // Meh. Should work :P
    }

    protected boolean canConsumeFuel(){
        return burnTime<2 && !stacks.get(1).isEmpty() && TileEntityFurnace.isItemFuel(stacks.get(1)) && !stacks.get(0).isEmpty() && !FurnaceRecipes.instance().getSmeltingResult(stacks.get(0)).isEmpty();
    }

    protected void smeltItem(){
        ItemStack i = stacks.get(2), tmp; // Temp variable give absolutely insignificant (yet existent) optimization :P
        stacks.set(2, tmp=FurnaceRecipes.instance().getSmeltingResult(stacks.get(0)).copy());
        tmp.setCount(tmp.getCount()+(i.getItem().equals(Items.AIR)?0:i.getCount()));
        (tmp=stacks.get(0)).setCount(tmp.getCount()-1);
        markDirty();
    }


    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        stacks = NonNullList.withSize(getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(compound, stacks);
        burnTime = compound.getInteger("CBurn");
        itemMaxBurn = compound.getInteger("BurnTime");
        cookTime = compound.getInteger("CookTime");
        maxCookTime = compound.getInteger("CookTimeTotal");
        //cookTime = getItemBurnTime(stacks.get(1));
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        compound.setInteger("CBurn", burnTime);
        compound.setInteger("BurnTime", itemMaxBurn);
        compound.setInteger("CookTime", cookTime);
        compound.setInteger("CookTimeTotal", maxCookTime);
        ItemStackHelper.saveAllItems(compound, stacks);
        return compound;
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
        for (ItemStack stack : stacks) if (!stack.isEmpty()) return false;
        return true;
    }
    @Nonnull @Override public ItemStack getStackInSlot(int slot) { return stacks.get(slot); }

    @MethodsReturnNonnullByDefault
    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = stacks.get(index);
        ItemStack i = new ItemStack(stack.getItem(), Math.min(stack.getCount(), count));
        stack.shrink(i.getCount());
        markDirty();
        return i;
    }
    @MethodsReturnNonnullByDefault @Override public ItemStack removeStackFromSlot(int index) { return decrStackSize(index, stacks.get(index).getCount()); }
    @Override public void setInventorySlotContents(int index, ItemStack stack) { markDirty(); stacks.set(index, stack); }
    @Override public int getInventoryStackLimit() { return 64; }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override public void openInventory(EntityPlayer player) {
        //player.openGui(SpatialSystems.instance, SpatialSystems.GUI_FURNACE, getWorld(), getPos().getX(), getPos().getY(), getPos().getZ());
        tracker.add(player);
    }

    @Override
    public void closeInventory(EntityPlayer player) {
        tracker.remove(player);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack stack) {
        return (index == 1 && (TileEntityFurnace.isItemFuel(stack) || SlotFurnaceFuel.isBucket(stack))) || (index==0 || index==2);
    }

    @Override
    public int getField(int id) { return id==FIELD_TIME?burnTime:id==FIELD_MAXTIME?itemMaxBurn:id==FIELD_BURN?cookTime:maxCookTime; }

    @Override
    public void setField(int id, int value) {
        if(id==FIELD_TIME) burnTime = value;
        else if(id==FIELD_MAXTIME) itemMaxBurn = value;
        else if(id==FIELD_BURN) cookTime = value;
        else if(id==FIELD_MAXBURN) maxCookTime = value;
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
        if(!stacks.get(slot).getItem().equals(stack.getItem())) return stack;
        int i;
        stack.setCount(Math.min(0, (i=stack.getCount())-stack.getMaxStackSize()+stacks.get(slot).getCount()));
        if(!simulate) {
            stacks.get(slot).setCount(stacks.get(slot).getCount() + i - stack.getCount());
            markDirty();
        }
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate){
        if(stacks.get(slot).isEmpty()) return ItemStack.EMPTY;
        ItemStack s = getStackInSlot(slot);
        if (simulate) stacks.set(slot, s);
        else markDirty();
        return s;
    }

    //@Override
    public int getSlotLimit(int slot){ return !stacks.get(slot).isEmpty() ? stacks.get(slot).getMaxStackSize() : 64; }

    @Override public String getName(){ return "Advanced Furnace"; }
    @Override public boolean hasCustomName(){ return false; }
}
