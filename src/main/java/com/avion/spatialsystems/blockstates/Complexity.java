package com.avion.spatialsystems.blockstates;

import com.google.common.base.Optional;
import net.minecraft.block.properties.IProperty;
import java.util.Arrays;
import java.util.Collection;

public enum Complexity implements IProperty{
    Basic, Advanced, Elite;

    @Override public String getName() { return this.name(); }
    @Override public Collection getAllowedValues() { return Arrays.asList(values()); }
    @Override public Class getValueClass() { return Complexity.class; } // Constant can be used since Enums can't be extended
    @Override
    public Optional parseValue(String value) {
        for(Complexity c : values())
            if(c.name().equals(value))
                return Optional.of(c);
        return Optional.absent();
    }
    @Override public String getName(Comparable value) { return value instanceof Enum?((Enum<?>) value).name():value instanceof IProperty?((IProperty) value).getName():value.toString(); } // I have no fkn clue
}
