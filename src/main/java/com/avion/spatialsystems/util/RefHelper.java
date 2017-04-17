package com.avion.spatialsystems.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@SuppressWarnings("unused")
public final class RefHelper {
    public static Object getValue(String name, Object on, Class<?> from){
        try{
            Field f = from.getDeclaredField(name);
            f.setAccessible(true);
            return f.get(on);
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

    public static boolean isNestedClass(Object o){ return getEnclosingReference(o, false)!=null; }
    public static Object getEnclosingReference(Object o){ return getEnclosingReference(o, true); }

    private static Object getEnclosingReference(Object nestedClass, boolean error){
        try{
            Field f = nestedClass.getClass().getDeclaredField("this$0");
            f.setAccessible(true);
            return f.get(nestedClass);
        }catch(Exception e){ if(error) e.printStackTrace(); return null; }
    }

    @SuppressWarnings("ThrowableNotThrown")
    public static Class getCallerClass(boolean ignoreSpecial, int depth){
        StackTraceElement[] trace = new Exception().getStackTrace();
        boolean foundFirst = false, isReflective;
        for(int i = 0; i<trace.length; ++i) {
            if (i < depth || ((isReflective=(trace[i].isNativeMethod() || trace[i].getClassName().startsWith("java.lang.reflect") ||
                    trace[i].getClassName().startsWith("sun.reflect"))) && !foundFirst) || (isReflective && ignoreSpecial)) continue;
            if(!foundFirst){
                foundFirst = true;
                continue;
            }
            try{ return Class.forName(trace[i].getClassName()); }catch(Throwable e){ e.printStackTrace(); return null; }
        }
        return null;
    }

    public static Class getCallerClass(boolean ignoreSpecial){ return getCallerClass(ignoreSpecial, 2); }
    public static Class getCallerClass(){ return getCallerClass(true, 2); }
}
