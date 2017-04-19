package com.avion.spatialsystems.container;

import com.avion.spatialsystems.tile.TileAdvancedChest;
import com.avion.spatialsystems.util.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;

//Created by Bread10 at 09:52 on 16/04/2017
public class ContainerAdvancedChest extends Container {

    private final InventoryPlayer playerInv;
    private final TileAdvancedChest tile;

    public ContainerAdvancedChest(InventoryPlayer player, TileAdvancedChest te) {
        this.playerInv = player;
        this.tile = te;
        LogHelper.println("DEBUG >>> Side="+ FMLCommonHandler.instance().getSide().name());
        setupSlots();
    }

    public void setupSlots() {
        //reset slots
        this.inventorySlots.clear();
        this.inventoryItemStacks.clear();

        // Tile Iinventory, Slot 0 to getTileSlotsOnPage() - 1
        for (int x = 0; x < getTileSlotsOnPage(); x++) {
            this.addSlotToContainer(new Slot(tile, x + 54 * (tile.getCurrentPage() - 1), 18 * (x % 9) + 8, 18 * (x / 9) + 18));
        }

        // Player Inventory, Slot 9-35, Slot IDs getTileSlotsOnPage() to getTileSlotsOnPage() + 26
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, 140 + y * 18));
            }
        }

        // Player Inventory, Slot 0-8, Slot IDs getTileSlotsOnPage() + 27 to getTileSlotsOnPage + 35
        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(playerInv, x, 8 + x * 18, 198));
        }
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index < getTileSlotsOnPage()) {
                if (!this.mergeItemStack(itemstack1, getTileSlotsOnPage(), getTileSlotsOnPage() + 36, true))
                    return ItemStack.EMPTY;
            } else {
                if (!this.mergeItemStack(itemstack1, 0, getTileSlotsOnPage()-1, false))
                    return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
        }
        tile.sync();
        return itemstack;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        tile.sync();
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    private int getTileSlotsOnPage() {
        return MathHelper.clamp(tile.getSizeInventory() - 54 * (tile.getCurrentPage() - 1), 1, 54);
    }

}
