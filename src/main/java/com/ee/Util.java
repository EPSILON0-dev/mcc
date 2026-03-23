package com.ee;

public class Util {
    public static float lerp(float start, float end, float t) {
        return start + t * (end - start);
    }
}
