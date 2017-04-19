package com.avion.spatialsystems.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class TileFurnaceBinder extends TileEntity{
    public final BlockPos boundSource;
    public TileFurnaceBinder(BlockPos bind){ super(); boundSource = bind; }
    public void triggerBreak(){
        TileEntity te = world.getTileEntity(boundSource);
        if(te instanceof TileAdvancedFurnace) ((TileAdvancedFurnace) te).triggerBreak();
    }
}
