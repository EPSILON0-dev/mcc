package com.ee;

import org.joml.Vector3f;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CameraTest {

    @Test
    public void moveMethodsTranslateAlongCameraAxes() {
        Camera camera = new Camera(new Vector3f(0.0f, 0.0f, 1.0f), new Vector3f(0.0f, 0.0f, -1.0f),
                90.0f, 1.0f);

        camera.moveForward(1.0f);
        camera.moveRight(2.0f);
        camera.moveUp(3.0f);

        assertEquals(2.0f, camera.position().x, 0.0001f);
        assertEquals(3.0f, camera.position().y, 0.0001f);
        assertEquals(0.0f, camera.position().z, 0.0001f);
    }

    @Test
    public void rotateUpdatesViewDirection() {
        Camera camera = new Camera(new Vector3f(0.0f, 0.0f, 1.0f), new Vector3f(0.0f, 0.0f, -1.0f),
                90.0f, 1.0f);

        camera.rotate((float) Math.toRadians(90.0f), 0.0f);

        assertEquals(1.0f, camera.direction().x, 0.0001f);
        assertEquals(0.0f, camera.direction().y, 0.0001f);
        assertEquals(0.0f, camera.direction().z, 0.0001f);
    }
}
