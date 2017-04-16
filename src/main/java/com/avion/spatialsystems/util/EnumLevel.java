package com.avion.spatialsystems.util;

import net.minecraft.util.IStringSerializable;

public enum EnumLevel implements IStringSerializable {
    BASIC(0),
    ADVANCED(1),
    ELITE(2);

    /**
     * <em>Deprecated:</em> Use {@link EnumLevel#ordinal()}
     */
    @Deprecated
    private final int VALUE;
    /**
     * <em>Deprecated:</em> Use {@link EnumLevel#ordinal()}
     */
    @Deprecated
    EnumLevel(int value) {
        this.VALUE = value;
    }

    @Override
    public String getName() {
        return this.name().toLowerCase();
    }

    /**
     * <em>Deprecated:</em> Use {@link EnumLevel#ordinal()}
     */
    @Deprecated
    public int getValue() {
        return VALUE;
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
