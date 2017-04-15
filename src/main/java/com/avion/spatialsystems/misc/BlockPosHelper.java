package com.avion.spatialsystems.misc;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

//Created by Bread10 at 15:14 on 15/04/2017
public class BlockPosHelper {

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

}
