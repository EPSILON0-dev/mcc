package com.ee.Client;

import org.joml.*;

import java.lang.Math;

public class Camera {
    private Vector3f position;
    private Vector3f direction;
    private Vector3f up;
    private float fov;
    private float aspectRatio;

    public Camera(Vector3f position, Vector3f direction, float fovDegrees, float aspectRatio) {
        this.position = new Vector3f(position);
        this.direction = new Vector3f(direction).normalize();
        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.fov = (float)Math.toRadians(fovDegrees);
        this.aspectRatio = aspectRatio;
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f().lookAt(position, new Vector3f(position).add(direction), up);
    }

    public Matrix4f getProjectionMatrix() {
        return new Matrix4f().perspective(fov, aspectRatio, 0.01f, 100.0f);
    }

    public Matrix4f getViewProjectionMatrix() {
        return getProjectionMatrix().mul(getViewMatrix());
    }

    public Vector3f position() {
        return position;
    }

    public Vector3f direction() {
        return direction;
    }

    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public void setDirection(Vector3f direction) {
        this.direction.set(direction).normalize();
    }

    public float fov() {
        return fov;
    }

    public void setFov(float fov) {
        this.fov = (float)Math.toRadians(fov);
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

}
