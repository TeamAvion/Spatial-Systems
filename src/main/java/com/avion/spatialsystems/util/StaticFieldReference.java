package com.avion.spatialsystems.util;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class StaticFieldReference<T> implements ObjectReference<T> {
    protected final String fieldName;
    protected final String clazzName;
    protected Class resolved;
    protected Field resolvedField;

    public StaticFieldReference(String fieldName, String clazzName){ this.fieldName = fieldName; this.clazzName = clazzName; }
    public StaticFieldReference(Field field){
        resolvedField = field;
        try{ resolvedField.setAccessible(true); }catch(Exception ignored){}
        resolved = resolvedField.getDeclaringClass();
        fieldName = null;
        clazzName = null;
    }
    public StaticFieldReference(String fieldName, Class clazz){ this.fieldName = fieldName; clazzName = null; resolved = clazz; }

    @Override
    public T get() {
        if(resolved == null) try{ resolved = Class.forName(clazzName); }catch(Throwable e){ e.printStackTrace(); }
        if(resolvedField == null) try{ resolvedField = resolved.getDeclaredField(fieldName); resolvedField.setAccessible(true); }catch(Throwable e){ e.printStackTrace(); }
        try{ //noinspection unchecked
            return (T) resolvedField.get(null); }catch(Throwable e){ e.printStackTrace(); }
        return null;
    }

    public static <T> StaticFieldReference<T> fromHere(String fieldName){
        Class c = RefHelper.getCallerClass();
        try{ return new StaticFieldReference<T>(c.getDeclaredField(fieldName)); }catch(Exception ignored){}
        return new StaticFieldReference<T>(fieldName, c); // Probably wrong... This essentially means there is a class but not field
    }
}