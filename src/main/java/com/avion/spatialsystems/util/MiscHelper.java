package com.avion.spatialsystems.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//Created by Bread10 at 13:30 on 16/04/2017
public class MiscHelper {

    public static <T> List<T> exceptArray(T[] values, T... except) {
        List<T> list = new ArrayList<T>();
        Collections.addAll(list, values);
        for (T t : except) {
            list.remove(t);
        }
        return list;
    }

}
