package com.avion.spatialsystems.gui;

import com.avion.spatialsystems.SpatialSystems;
import com.avion.spatialsystems.container.ContainerAdvancedChest;
import com.avion.spatialsystems.container.ContainerAdvancedFurnace;
import com.avion.spatialsystems.tile.TileAdvancedChest;
import com.avion.spatialsystems.tile.TileAdvancedFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class GUIHandler implements IGuiHandler{

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        switch(ID){
            case SpatialSystems.GUI_FURNACE:
                return new ContainerAdvancedFurnace(player.inventory, (TileAdvancedFurnace) te);
            case SpatialSystems.GUI_CHEST:
                return new ContainerAdvancedChest(player.inventory, (TileAdvancedChest) te);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
        switch (ID){
            case SpatialSystems.GUI_FURNACE:
                return new GUIAdvancedFurnace(player.inventory, (TileAdvancedFurnace) te);
            case SpatialSystems.GUI_CHEST:
                return new GUIAdvancedChest(player.inventory, (TileAdvancedChest) te);
        }
        return null;
    }
}
