package com.ee.Client;

public class ShaderException extends RuntimeException {
    public ShaderException(String msg) {
        super(msg);
    }

    public ShaderException(ShaderException e) {
        super(e);
    }
}
