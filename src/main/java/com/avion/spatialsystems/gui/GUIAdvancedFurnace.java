package com.avion.spatialsystems.gui;

import com.avion.spatialsystems.container.ContainerAdvancedFurnace;
import com.avion.spatialsystems.util.RefHelper;
import com.avion.spatialsystems.tile.TileAdvancedFurnace;
import net.minecraft.client.gui.inventory.GuiFurnace;
import net.minecraft.entity.player.InventoryPlayer;

public class GUIAdvancedFurnace extends GuiFurnace{

    protected final TileAdvancedFurnace tile;

    public GUIAdvancedFurnace(InventoryPlayer player, TileAdvancedFurnace furnace){
        //super(new ContainerAdvancedFurnace(player, furnace));
        super(player, furnace);
        inventorySlots = new ContainerAdvancedFurnace(player, furnace); // Basically override half of what the superconstructor did
        tile = furnace;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(tile.getName(), 8, 6, 0xFF0088); // Top left label
        this.fontRendererObj.drawString(((InventoryPlayer) RefHelper.getValue("playerInventory", this, GuiFurnace.class)).getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }
}
