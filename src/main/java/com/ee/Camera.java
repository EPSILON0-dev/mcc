package com.ee;

import org.joml.*;

import java.lang.Math;

public class Camera {
    private static final float MAX_PITCH = (float) Math.toRadians(89.0f);
    private static final Vector3f WORLD_UP = new Vector3f(0.0f, 1.0f, 0.0f);

    private Vector3f position;
    private Vector3f direction;
    private Vector3f up;
    private float fov;
    private float aspectRatio;
    private float yaw;
    private float pitch;

    public Camera(Vector3f position, Vector3f direction, float fovDegrees, float aspectRatio) {
        this.position = new Vector3f(position);
        this.direction = new Vector3f(direction).normalize();
        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.fov = (float)Math.toRadians(fovDegrees);
        this.aspectRatio = aspectRatio;
        syncAnglesFromDirection();
        updateUpVector();
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
        syncAnglesFromDirection();
        updateUpVector();
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

    public void moveForward(float distance) {
        position.fma(distance, new Vector3f(direction));
    }

    public void moveRight(float distance) {
        position.fma(distance, getRightVector());
    }

    public void moveUp(float distance) {
        position.fma(distance, getUpVector());
    }

    public void rotate(float yawDelta, float pitchDelta) {
        yaw += yawDelta;
        pitch = Math.clamp(pitch + pitchDelta, -MAX_PITCH, MAX_PITCH);
        updateDirectionFromAngles();
    }

    private Vector3f getRightVector() {
        return new Vector3f(direction).cross(WORLD_UP).normalize();
    }

    private Vector3f getUpVector() {
        return getRightVector().cross(direction).normalize();
    }

    private void syncAnglesFromDirection() {
        yaw = (float) Math.atan2(direction.z, direction.x);
        pitch = (float) Math.asin(direction.y);
    }

    private void updateDirectionFromAngles() {
        direction.set(
                (float) (Math.cos(yaw) * Math.cos(pitch)),
                (float) Math.sin(pitch),
                (float) (Math.sin(yaw) * Math.cos(pitch))
        ).normalize();
        updateUpVector();
    }

    private void updateUpVector() {
        Vector3f right = getRightVector();
        right.cross(direction, up).normalize();
    }
}
