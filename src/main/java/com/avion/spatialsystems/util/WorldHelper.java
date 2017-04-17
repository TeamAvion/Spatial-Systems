package com.avion.spatialsystems.util;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

import static com.avion.spatialsystems.util.WorldHelper.Rotate.*;

//Created by Bread10 at 15:14 on 15/04/2017
public final class WorldHelper {

    public static final BlockPos NULL = new BlockPos(0, -1, 0);

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


    public static final EnumFacing[] VALUES = {EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.UP, EnumFacing.DOWN};
    public static final EnumFacing.Axis[] AXES = {EnumFacing.Axis.X, EnumFacing.Axis.Y, EnumFacing.Axis.Z};

    public static EnumFacing[] getDirectionsFromAxis(EnumFacing.Axis axis) {
        return new EnumFacing[]{EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis), EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, axis)};
    }

    public static boolean findMultiblock(MBStruct m){
        //TODO: Implement
        return true;
    }

    public static BlockPos rotatePos(BlockPos pos, BlockPos to, Rotate by){
        return new BlockPos(by==LEFT?to.getX()-pos.getZ()+to.getZ():to.getX()+pos.getZ()-to.getZ(), pos.getY(), by==LEFT?to.getZ()+pos.getX()-to.getX():to.getZ()-pos.getX()+to.getZ());
    }

    public static BlockPos[] rotateRow(BlockPos[] pos, BlockPos around, Rotate direction){
        BlockPos[] pos1 = new BlockPos[pos.length];
        for(int i = 0; i<pos.length; ++i) pos1[i] = rotatePos(pos[i], around, direction);
        return pos1;
    }

    public enum Rotate{ LEFT, RIGHT; }
}
