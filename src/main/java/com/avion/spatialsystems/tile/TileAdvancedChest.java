package com.avion.spatialsystems.tile;

import com.avion.spatialsystems.blocks.ModBlocks;
import com.avion.spatialsystems.misc.BlockPosHelper;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

//Created by Bread10 at 10:20 on 15/04/2017
public class TileAdvancedChest extends TileEntity {

    private boolean isFormed = false;
    private BlockPos bottomNorthWestCorner = BlockPosHelper.NULL;
    private int dimension = 0;

    public void findMultiBlockStructure() {
        BlockPos thisPos = this.getPos();
        EnumFacing airDirection = EnumFacing.SOUTH;
        int airBlocks = 0;
        for (EnumFacing facing : EnumFacing.values()) {
            BlockPos pos = thisPos.offset(facing);
            if (this.getWorld().getBlockState(pos).getBlock() == ModBlocks.advancedChestBlock) {
                airBlocks++;
                airDirection = facing;
            }
        }
        if (airBlocks > 1) {
            isFormed = false;
            bottomNorthWestCorner = BlockPosHelper.NULL;
            dimension = 0;
            return;
        }

        EnumFacing back = airDirection.getOpposite();

        int dim = 0;
        for (int i = 1; i <= 16; i++) {
            BlockPos pos = thisPos.offset(back, i);
            if (this.getWorld().getBlockState(pos).getBlock() != ModBlocks.advancedChestBlock) {
                dim = i;
                break;
            }
        }
        System.out.println(dim);
    }

}
