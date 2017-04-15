package com.avion.spatialsystems.gui;

import com.avion.spatialsystems.container.ContainerAdvancedFurnace;
import com.avion.spatialsystems.reflection.Helper;
import com.avion.spatialsystems.tile.TileAdvancedFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
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
        //super.drawGuiContainerForegroundLayer(mouseX, mouseY); // NOP
        fontRendererObj.drawString(tile.getName(), 8, 6, 0xFF0088);
        //LEL I <3 reflection
        this.fontRendererObj.drawString(((InventoryPlayer)Helper.getValue("playerInventory", this, GuiFurnace.class)).getDisplayName().getUnformattedText(), 8, this.ySize - 96 + 2, 4210752);
    }
}
