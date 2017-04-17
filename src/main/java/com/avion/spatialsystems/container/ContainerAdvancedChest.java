package com.avion.spatialsystems.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;

//Created by Bread10 at 09:52 on 16/04/2017
public class ContainerAdvancedChest extends Container {

    private IInventory tile;
    private int pageNumber;

    public ContainerAdvancedChest(InventoryPlayer player, IInventory te) {
        this.tile = te;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

}
