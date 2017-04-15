package com.avion.spatialsystems.container;

import com.avion.spatialsystems.reflection.Helper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

import static com.avion.spatialsystems.tile.TileAdvancedFurnace.FIELD_TIME;
import static com.avion.spatialsystems.tile.TileAdvancedFurnace.FIELD_MAXTIME;
import static com.avion.spatialsystems.tile.TileAdvancedFurnace.FIELD_BURN;
import static com.avion.spatialsystems.tile.TileAdvancedFurnace.FIELD_MAXBURN;

public class ContainerAdvancedFurnace extends ContainerFurnace {

    protected final IInventory furnaceInventory;

    public ContainerAdvancedFurnace(InventoryPlayer playerInventory, IInventory furnaceInventory){
        super(playerInventory, furnaceInventory);
        this.furnaceInventory = furnaceInventory;
    }

    @Override
    public void detectAndSendChanges() {
        //super.detectAndSendChanges(); // Complete override

        // Inventory management
        for (int i = 0; i < this.inventorySlots.size(); ++i)
        {
            ItemStack itemstack = this.inventorySlots.get(i).getStack();
            ItemStack itemstack1 = this.inventoryItemStacks.get(i);

            if (!ItemStack.areItemStacksEqual(itemstack1, itemstack))
            {
                this.inventoryItemStacks.set(i, itemstack1 = itemstack.isEmpty() ? ItemStack.EMPTY : itemstack.copy());
                for (IContainerListener listener : this.listeners) listener.sendSlotContents(this, i, itemstack1);
            }
        }

        // Furnace-specific
        for (IContainerListener listener : listeners) {
            checkAndSend(listener, "cookTime", FIELD_TIME);
            checkAndSend(listener, "totalCookTime", FIELD_MAXTIME);
            checkAndSend(listener, "furnaceBurnTime", FIELD_MAXBURN);
            checkAndSend(listener, "currentItemBurnTime", FIELD_BURN);
        }
    }

    @SuppressWarnings("ConstantConditions")
    protected final void checkAndSend(IContainerListener listener, String name, int val){
        int val1;
        if((Integer) Helper.getValue(name, this, ContainerFurnace.class)!=(val1=furnaceInventory.getField(val))) {
            listener.sendProgressBarUpdate(this, val, val1);
        }
    }
}
