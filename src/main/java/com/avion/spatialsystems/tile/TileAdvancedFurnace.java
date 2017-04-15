package com.avion.spatialsystems.tile;

import net.minecraft.tileentity.TileEntity;

public class TileAdvancedFurnace extends TileEntity{

    // Values defined for getting info from this class
    public static final int FIELD_TIME = 0x00;
    public static final int FIELD_MAXTIME = 0x01;
    public static final int FIELD_BURN = 0x02;
    public static final int FIELD_MAXBURN = 0x03;


    protected final int complexity;

    public TileAdvancedFurnace(int complexity){ this.complexity = complexity; }



}
