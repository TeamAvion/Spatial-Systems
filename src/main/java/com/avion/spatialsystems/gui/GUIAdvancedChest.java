package com.avion.spatialsystems.gui;

import com.avion.spatialsystems.SpatialSystems;
import com.avion.spatialsystems.container.ContainerAdvancedChest;
import com.avion.spatialsystems.net.ChestMessage;
import com.avion.spatialsystems.tile.TileAdvancedChest;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

import java.io.IOException;

//Created by Bread10 at 12:02 on 17/04/2017
public class GUIAdvancedChest extends GuiContainer {

    private static final ResourceLocation BACKGROUND = new ResourceLocation(SpatialSystems.MODID, "textures/gui/advancedchest.png");

    private ContainerAdvancedChest container;

    private InventoryPlayer playerInv;
    private TileAdvancedChest tile;
    private final int pages;

    public GUIAdvancedChest(InventoryPlayer playerInv, TileAdvancedChest tile) {
        super(new ContainerAdvancedChest(playerInv, tile));

        pages = (int) Math.floor(tile.getSizeInventory() / 54);

        this.playerInv = playerInv;
        this.tile = tile;

        this.xSize = 175;
        this.ySize = 221;
    }

    @Override
    public void initGui() {
        super.initGui();
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.buttonList.add(new GuiButton(0, i+ 105, j+ 126, 14, 12, "<"));
        this.buttonList.add(new GuiButton(1, i+ 155, j+ 126, 14, 12, ">"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        int newPage = tile.getCurrentPage();
        switch (button.id) {
            case 0: //Back button
                newPage--;
                break;
            case 1: //Next button
                newPage++;
                break;
        }
        newPage = MathHelper.clamp(newPage, 1, pages);
        tile.setCurrentPage(newPage);
        SpatialSystems.instance.net.sendToServer(new ChestMessage(tile.getPos(), tile.getWorld(), tile, newPage));
        ((ContainerAdvancedChest) this.inventorySlots).setupSlots();
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        this.fontRendererObj.drawString(I18n.format("container.advancedchest"), 8, 6, 4210752);
        this.fontRendererObj.drawString(this.playerInv.getDisplayName().getUnformattedText(), 8, 129, 4210752);
        this.fontRendererObj.drawString(tile.getCurrentPage() + "/" + pages, 122, 129, 4210752);
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        this.drawDefaultBackground();
        GlStateManager.color(1F, 1F, 1F, 1F);
        this.mc.getTextureManager().bindTexture(BACKGROUND);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        for (int k = 0; k < getTileSlotsOnPage(); k++) {
            this.drawTexturedModalRect(18 * (k % 9) + 7 + i, 18 * (k / 9) + 17 + j, 176, 0, 18, 18);
        }
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    private int getTileSlotsOnPage() {
        return MathHelper.clamp(tile.getSizeInventory() - 54 * (tile.getCurrentPage() - 1), 0, 54);
    }

}
