package com.avion.spatialsystems.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

public abstract class BoundTileImpl extends BoundTile{
    protected BlockPos boundSource;
    public final void triggerBreak(){ if(boundSource!=null) _triggerBreak(); }
    protected abstract void _triggerBreak();
    @Override public boolean isBound() { return boundSource!=null; }
    @Override public void unbind() { boundSource = null; }
    @Override public boolean isBound(BlockPos to) { return to!=null && to.equals(boundSource); }
    @Override protected void _bind(BlockPos to) { boundSource = to; }
    @Override protected BlockPos getBoundPosition() { return boundSource; }
    public BlockPos getBoundSource(){ return boundSource; }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if(boundSource!=null) {
            compound.setInteger("boundX", boundSource.getX());
            compound.setInteger("boundY", boundSource.getY());
            compound.setInteger("boundZ", boundSource.getZ());
        }
        return super.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if(compound.hasKey("boundX") && compound.hasKey("boundY") && compound.hasKey("boundZ"))
            boundSource = new BlockPos(compound.getInteger("boundX"), compound.getInteger("boundY"), compound.getInteger("boundZ"));
    }
}
