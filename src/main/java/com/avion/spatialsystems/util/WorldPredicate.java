package com.avion.spatialsystems.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class WorldPredicate {
    public abstract boolean apply(World w, BlockPos p);
}
