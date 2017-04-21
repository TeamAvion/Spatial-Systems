package com.avion.spatialsystems.util;

import net.minecraft.util.IStringSerializable;

//Created by Bread10 at 04:59 on 19/04/2017
public enum EnumType implements IStringSerializable {
    CORNER, ;

    static {
        int i = 0;
        for (EnumType t : values()) {
            t.value = i++;
        }
    }

    private int value;
    public int getValue() {
        return value;
    }

    public static EnumType getTypeFromValue(int value) {
        for (EnumType t : values()) {
            if (t.getValue() == value) {
                return t;
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return name().toLowerCase();
    }
}
