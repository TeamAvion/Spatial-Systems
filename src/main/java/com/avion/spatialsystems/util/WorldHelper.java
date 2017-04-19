package com.avion.spatialsystems.util;

import com.avion.spatialsystems.tile.BoundTile;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Collection;

import static com.avion.spatialsystems.util.WorldHelper.Rotate.*;

//Created by Bread10 at 15:14 on 15/04/2017
@SuppressWarnings("unused")
public final class WorldHelper {

    public static final BlockPos NULL = new BlockPos(0, -1, 0);

    @SuppressWarnings("UnusedReturnValue")
    public static NBTTagCompound writeBlockPosToNBT(NBTTagCompound tag, String tagName, BlockPos pos) {
        NBTTagCompound blockPosTag = new NBTTagCompound();
        blockPosTag.setInteger("X", pos.getX());
        blockPosTag.setInteger("Y", pos.getY());
        blockPosTag.setInteger("Z", pos.getZ());

        tag.setTag(tagName, blockPosTag);

        return tag;
    }

    public static BlockPos readBlockPosFromNBT(NBTTagCompound tag, String tagName) {
        NBTTagCompound blockPosTag = tag.getCompoundTag(tagName);
        int x = blockPosTag.getInteger("X");
        int y = blockPosTag.getInteger("Y");
        int z = blockPosTag.getInteger("Z");
        return new BlockPos(x, y, z);
    }

    public static BlockPos copy(BlockPos pos) {
        return new BlockPos(pos.getX(), pos.getY(), pos.getZ());
    }

    public static EnumFacing[] getDirectionsFromAxis(EnumFacing.Axis axis) {
        return new EnumFacing[]{EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis), EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, axis)};
    }

    public static BlockPos rotatePos(BlockPos pos, BlockPos to, Rotate by){
        return new BlockPos(by==LEFT?to.getX()+pos.getZ()-to.getZ():to.getX()-pos.getZ()+to.getZ(), pos.getY(), by==LEFT?to.getZ()-pos.getX()+to.getX():to.getZ()+pos.getX()-to.getX());
    }

    public static BlockPos[] rotateRow(BlockPos[] pos, BlockPos around, Rotate direction){
        BlockPos[] pos1 = new BlockPos[pos.length];
        for(int i = 0; i<pos.length; ++i) pos1[i] = rotatePos(pos[i], around, direction);
        return pos1;
    }

    public static void bindAll(IBlockAccess w, BlockPos[] all, BlockPos source){
        TileEntity t;
        for(BlockPos p : all) if((t=w.getTileEntity(p))!=null && t instanceof BoundTile && !((BoundTile) t).isBound()) ((BoundTile) t).bind(source);
    }

    public static void unbindAll(IBlockAccess w, BlockPos[] all){
        TileEntity t;
        for(BlockPos p : all) if((t=w.getTileEntity(p))!=null && t instanceof BoundTile && ((BoundTile) t).isBound()) ((BoundTile) t).unbind();
    }

    public static void dropItem(World w, BlockPos pos, Collection<ItemStack> c){
        //InventoryHelper.dropInventoryItems(world, pos, this);
        for(ItemStack i : c) dropItem(w, pos, i);
    }

    public static void dropItem(World w, BlockPos pos, ItemStack... c){
        //InventoryHelper.dropInventoryItems(world, pos, this);
        for(ItemStack i : c) dropItem(w, pos, i);
    }

    public static void dropItem(World w, BlockPos pos, ItemStack i){
        if(!i.isEmpty()) {
            EntityItem e = new EntityItem(w, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, i.copy());
            e.rotationYaw = 0;
            e.motionX = 0;
            e.motionY = 0;
            e.motionZ = 0;
            w.spawnEntity(e);
            i.setCount(0);
        }
    }

    public enum Rotate{ LEFT, RIGHT }
}
