package com.avion.spatialsystems.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileFurnaceBinder extends BoundTileImpl{
    @Override
    protected void _triggerBreak(){
        TileEntity te = world.getTileEntity(boundSource);
        if(te instanceof TileAdvancedFurnace) ((TileAdvancedFurnace) te).triggerBreak(pos);
    }
}
