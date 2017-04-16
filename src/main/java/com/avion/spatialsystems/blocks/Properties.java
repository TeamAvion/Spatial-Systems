package com.avion.spatialsystems.blocks;

import com.avion.spatialsystems.util.EnumLevel;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;

//Created by Bread10 at 08:33 on 15/04/2017
public class Properties {

    public static final PropertyEnum LEVEL = PropertyEnum.create("level", EnumLevel.class);
    //public static final PropertyEnum TYPE = PropertyEnum.create("type", EnumType.class);
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

}
