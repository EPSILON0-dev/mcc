package com.ee;

import org.joml.*;
import java.lang.Math;

public class Player extends PhysicsObject {
    private static final float MAX_PITCH = (float) Math.toRadians(89.0f);
    private static final float COLLIDER_RADIUS = 0.3f;
    private static final float COLLIDER_HEIGHT = 1.8f;
    private static final float MOVEMENT_SPEED = 2.0f;
    private static final float CAMERA_HEIGHT_OFFSET = 1.6f;
    private static final float PLAYER_FRICTION = 20.0f;
    private static final float PLAYER_GRAVITY = 9.81f * 2.0f; // For better feel :p
    private static final float JUMP_IMPULSE = 7.0f;

    private float yaw;
    private float pitch;

    public Player(Vector3f position, Vector3f direction) {
        super(position, direction, COLLIDER_RADIUS, COLLIDER_HEIGHT, PLAYER_FRICTION, PLAYER_GRAVITY);
        syncAnglesFromDirection();
    }

    public void setupCamera(Camera camera) {
        Vector3f cameraPosition = new Vector3f(position).add(0, CAMERA_HEIGHT_OFFSET, 0);
        camera.setPosition(cameraPosition);
        camera.setDirection(direction);
    }

    public void rotate(float yawDelta, float pitchDelta) {
        yaw += yawDelta;
        pitch = Math.clamp(pitch + pitchDelta, -MAX_PITCH, MAX_PITCH);
        updateDirectionFromAngles();
    }

    public void moveForward(World world) {
        addMovementForce(getForwardVector());
    }

    public void moveRight(World world) {
        addMovementForce(getRightVector());
    }

    public void moveBackward(World world) {
        addMovementForce(getForwardVector().negate());
    }

    public void moveLeft(World world) {
        addMovementForce(getRightVector().negate());
    }

    public void jump(World world) {
        if (Physics.isOnGround(world, position, colliderRadius, colliderHeight)) {
            velocity = velocity.add(0, JUMP_IMPULSE, 0);
            System.out.println("Jumped");
        }
    }

    public void addMovementForce(Vector3f movementDirection) {
        movementDirection.y = 0.0f;
        addForce(new Vector3f(movementDirection).normalize().mul(MOVEMENT_SPEED));
    }

    public void dumpDebugInfo(World world) {
        System.out.print("Position: " + position);
        System.out.print(", Direction: " + direction);
        System.out.print(", Velocity: " + velocity);
        System.out.println(isOnGround(world) ? ", On Ground" : ", In Air");
    }

    private void syncAnglesFromDirection() {
        yaw = (float) Math.atan2(direction.z, direction.x);
        pitch = (float) Math.asin(direction.y);
    }

    private void updateDirectionFromAngles() {
        direction.set(
                (float) (Math.cos(yaw) * Math.cos(pitch)),
                (float) Math.sin(pitch),
                (float) (Math.sin(yaw) * Math.cos(pitch))).normalize();
    }
}
