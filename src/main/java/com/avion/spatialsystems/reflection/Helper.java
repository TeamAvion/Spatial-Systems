package com.avion.spatialsystems.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class Helper {
    public static Object getValue(String name, Object on, Class<?> from){
        try{
            Field f = from.getDeclaredField(name);
            f.setAccessible(true);
            return f.get(null);
        }catch(Exception e){ e.printStackTrace(); }
        return null;
    }

    public static void setValue(String name, Object on, Object value, Class<?> from){
        try{
            Field f = from.getDeclaredField(name);
            f.setAccessible(true);
            if(f.getType().isPrimitive()){
                String s;
                Method m = Field.class.getDeclaredMethod("set"+(s=f.getType().getSimpleName()).substring(0, 1)+f.getType().getSimpleName().substring(1, s.length()),
                        Object.class, f.getType());
                m.invoke(on, value); // Unboxing is done automatically
            }
            else f.set(on, value);
        }catch(Exception e){ e.printStackTrace(); }
    }
}
