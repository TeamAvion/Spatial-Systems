package com.avion.spatialsystems.util;

import net.minecraft.util.EnumFacing;

import net.minecraft.util.EnumFacing.Axis;

//Created by Bread10 at 09:20 on 16/04/2017
public final class FacingHelper {

    public static final EnumFacing[] VALUES = {EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.SOUTH, EnumFacing.WEST, EnumFacing.UP, EnumFacing.DOWN};
    public static final Axis[] AXES = {Axis.X, Axis.Y, Axis.Z};

    public static EnumFacing[] getDirectionsFromAxis(Axis axis) {
        switch (axis) {
            case X:
                return new EnumFacing[] {EnumFacing.EAST, EnumFacing.WEST};
            case Y:
                return new EnumFacing[] {EnumFacing.UP, EnumFacing.DOWN};
            default:
                return new EnumFacing[] {EnumFacing.NORTH, EnumFacing.SOUTH};
        }
    }

}
