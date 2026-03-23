package com.ee;

public class ShaderException extends RuntimeException {
    public ShaderException(String msg) {
        super(msg);
    }

    public ShaderException(ShaderException e) {
        super(e);
    }
}
