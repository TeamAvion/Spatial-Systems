package com.avion.spatialsystems.gui;

import com.avion.spatialsystems.container.ContainerAdvancedFurnace;
import com.avion.spatialsystems.tile.TileAdvancedFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;

public class GUIAdvancedFurnace extends GuiContainer{

    protected final TileAdvancedFurnace tile;

    public GUIAdvancedFurnace(InventoryPlayer player, TileAdvancedFurnace furnace){
        super(new ContainerAdvancedFurnace(player, furnace));
        tile = furnace;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        //super.drawGuiContainerForegroundLayer(mouseX, mouseY); // NOP
        fontRendererObj.drawString(tile.getName(), 8, 6, 0xFF0088);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {

    }
}
