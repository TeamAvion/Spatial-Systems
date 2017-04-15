package com.avion.spatialsystems.misc;

import net.minecraft.util.IStringSerializable;

public enum EnumLevel implements IStringSerializable {
    BASIC(0),
    ADVANCED(1),
    ELITE(2);

    private final int value;
    EnumLevel(int value) {
        this.value = value;
    }

    @Override
    public String getName() {
        return this.name().toLowerCase();
    }

    public int getValue() {
        return value;
    }

    public static EnumLevel getLevelFromValue(int value) {
        for (EnumLevel level : values()) {
            if (level.getValue() == value) {
                return level;
            }
        }
        return BASIC;
    }

}
