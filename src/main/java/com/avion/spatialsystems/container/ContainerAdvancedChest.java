package com.avion.spatialsystems.container;

import com.avion.spatialsystems.tile.TileAdvancedChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;

//Created by Bread10 at 09:52 on 16/04/2017
public class ContainerAdvancedChest extends Container {

    private TileAdvancedChest tile;

    public ContainerAdvancedChest(InventoryPlayer player, TileAdvancedChest te) {
        this.tile = te;
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

}
