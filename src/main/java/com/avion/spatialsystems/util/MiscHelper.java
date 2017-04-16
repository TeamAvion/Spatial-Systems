package com.avion.spatialsystems.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public final class MiscHelper {

    public static <T> List<T> exceptArray(T[] values, T... except) {
        List<T> list = new ArrayList<T>();
        Collections.addAll(list, values);
        list.removeAll(Arrays.asList(except));
        return list;
    }

}
