package com.avion.spatialsystems.tile;

import com.avion.spatialsystems.util.LogHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public abstract class BoundTile extends TileEntity{
    public abstract boolean isBound();
    public abstract void unbind();
    public abstract boolean isBound(BlockPos to);
    public final void bind(BlockPos to){
        if(!isBound() && to!=null) _bind(to);
        else if(to==null) throw new NullPointerException("Tried to bind tile at "+getPos()+" to a null position!");
        else LogHelper.error("Attempting to bind position "+getBoundPosition()+" to a bound tile at "+getPos());
    }
    protected abstract void _bind(BlockPos to);
    protected abstract BlockPos getBoundPosition();
}
