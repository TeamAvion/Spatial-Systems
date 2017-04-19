package com.avion.spatialsystems.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileChestBinder extends BoundTileImpl {
    public void _triggerBreak(){
        TileEntity te = world.getTileEntity(boundSource);
        if(te instanceof TileAdvancedChest) ((TileAdvancedChest) te).triggerBreak(pos);
    }
}
