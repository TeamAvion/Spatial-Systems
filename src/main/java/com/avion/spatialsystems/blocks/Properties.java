package com.avion.spatialsystems.blocks;

import com.avion.spatialsystems.util.EnumLevel;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;

public final class Properties {

    public static final PropertyEnum LEVEL = PropertyEnum.create("level", EnumLevel.class);
    public static final PropertyDirection FACING = PropertyDirection.create("facing");

}
