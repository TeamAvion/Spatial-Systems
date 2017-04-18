package com.avion.spatialsystems.util;

public class MutableReference<T> implements ObjectReference<T> {
    protected T value;
    public MutableReference(T value){ this.value = value; }
    @Override public T get() { return value; }
    public void update(T value){ this.value = value; }
}
