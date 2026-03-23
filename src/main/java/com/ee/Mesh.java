package com.ee;

public interface Mesh {
    public default void bind() {}
    public default void unbind() {}
    public default void draw() {}
}
