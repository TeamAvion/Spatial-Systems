package com.avion.spatialsystems.tile;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

//Created by Bread10 at 04:57 on 19/04/2017
public class TileChestBinder extends TileEntity {
    public final BlockPos boundSource;
    public TileChestBinder(BlockPos bind){ super(); boundSource = bind; }
    public void triggerBreak(){
        TileEntity te = world.getTileEntity(boundSource);
        if(te instanceof TileAdvancedChest) ((TileAdvancedChest) te).triggerBreak();
    }
}
