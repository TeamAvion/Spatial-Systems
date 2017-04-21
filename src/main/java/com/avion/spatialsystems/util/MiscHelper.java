package com.avion.spatialsystems.util;

import java.util.Arrays;
import java.util.List;

public final class MiscHelper {

    public static <T> List<T> exceptArray(T[] values, T... except) {
        List<T> list = Arrays.asList(values);
        list.removeAll(Arrays.asList(except));
        return list;
    }

}
