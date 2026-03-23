package com.ee;

import org.joml.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Cube {
    public static ArrayList<ChunkMeshVertex> frontSide(Vector3f offset, float textureIndex) {
        return new ArrayList<>(Arrays.asList(
                new ChunkMeshVertex(new Vector3f(0.0f, 0.0f, 1.0f).add(offset),
                        new Vector3f(0.0f, 0.0f, 1.0f),
                        new Vector2f(0.0f, 1.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(1.0f, 0.0f, 1.0f).add(offset),
                        new Vector3f(0.0f, 0.0f, 1.0f),
                        new Vector2f(1.0f, 1.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(1.0f, 1.0f, 1.0f).add(offset),
                        new Vector3f(0.0f, 0.0f, 1.0f),
                        new Vector2f(1.0f, 0.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(0.0f, 1.0f, 1.0f).add(offset),
                        new Vector3f(0.0f, 0.0f, 1.0f),
                        new Vector2f(0.0f, 0.0f), textureIndex)));
    }

    public static ArrayList<ChunkMeshVertex> backSide(Vector3f offset, float textureIndex) {
        return new ArrayList<>(Arrays.asList(
                new ChunkMeshVertex(new Vector3f(1.0f, 0.0f, 0.0f).add(offset),
                        new Vector3f(0.0f, 0.0f, -1.0f),
                        new Vector2f(0.0f, 1.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(0.0f, 0.0f, 0.0f).add(offset),
                        new Vector3f(0.0f, 0.0f, -1.0f),
                        new Vector2f(1.0f, 1.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(0.0f, 1.0f, 0.0f).add(offset),
                        new Vector3f(0.0f, 0.0f, -1.0f),
                        new Vector2f(1.0f, 0.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(1.0f, 1.0f, 0.0f).add(offset),
                        new Vector3f(0.0f, 0.0f, -1.0f),
                        new Vector2f(0.0f, 0.0f), textureIndex)));
    }

    public static ArrayList<ChunkMeshVertex> leftSide(Vector3f offset, float textureIndex) {
        return new ArrayList<>(Arrays.asList(
                new ChunkMeshVertex(new Vector3f(0.0f, 0.0f, 0.0f).add(offset),
                        new Vector3f(-1.0f, 0.0f, 0.0f),
                        new Vector2f(0.0f, 1.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(0.0f, 0.0f, 1.0f).add(offset),
                        new Vector3f(-1.0f, 0.0f, 0.0f),
                        new Vector2f(1.0f, 1.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(0.0f, 1.0f, 1.0f).add(offset),
                        new Vector3f(-1.0f, 0.0f, 0.0f),
                        new Vector2f(1.0f, 0.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(0.0f, 1.0f, 0.0f).add(offset),
                        new Vector3f(-1.0f, 0.0f, 0.0f),
                        new Vector2f(0.0f, 0.0f), textureIndex)));
    }

    public static ArrayList<ChunkMeshVertex> rightSide(Vector3f offset, float textureIndex) {
        return new ArrayList<>(Arrays.asList(
                new ChunkMeshVertex(new Vector3f(1.0f, 0.0f, 1.0f).add(offset),
                        new Vector3f(1.0f, 0.0f, 0.0f),
                        new Vector2f(0.0f, 1.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(1.0f, 0.0f, 0.0f).add(offset),
                        new Vector3f(1.0f, 0.0f, 0.0f),
                        new Vector2f(1.0f, 1.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(1.0f, 1.0f, 0.0f).add(offset),
                        new Vector3f(1.0f, 0.0f, 0.0f),
                        new Vector2f(1.0f, 0.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(1.0f, 1.0f, 1.0f).add(offset),
                        new Vector3f(1.0f, 0.0f, 0.0f),
                        new Vector2f(0.0f, 0.0f), textureIndex)));
    }

    public static ArrayList<ChunkMeshVertex> topSide(Vector3f offset, float textureIndex) {
        return new ArrayList<>(Arrays.asList(
                new ChunkMeshVertex(new Vector3f(0.0f, 1.0f, 1.0f).add(offset),
                        new Vector3f(0.0f, 1.0f, 0.0f),
                        new Vector2f(0.0f, 1.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(1.0f, 1.0f, 1.0f).add(offset),
                        new Vector3f(0.0f, 1.0f, 0.0f),
                        new Vector2f(1.0f, 1.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(1.0f, 1.0f, 0.0f).add(offset),
                        new Vector3f(0.0f, 1.0f, 0.0f),
                        new Vector2f(1.0f, 0.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(0.0f, 1.0f, 0.0f).add(offset),
                        new Vector3f(0.0f, 1.0f, 0.0f),
                        new Vector2f(0.0f, 0.0f), textureIndex)));
    }

    public static ArrayList<ChunkMeshVertex> bottomSide(Vector3f offset, float textureIndex) {
        return new ArrayList<>(Arrays.asList(
                new ChunkMeshVertex(new Vector3f(0.0f, 0.0f, 0.0f).add(offset),
                        new Vector3f(0.0f, -1.0f, 0.0f),
                        new Vector2f(0.0f, 0.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(1.0f, 0.0f, 0.0f).add(offset),
                        new Vector3f(0.0f, -1.0f, 0.0f),
                        new Vector2f(1.0f, 0.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(1.0f, 0.0f, 1.0f).add(offset),
                        new Vector3f(0.0f, -1.0f, 0.0f),
                        new Vector2f(1.0f, 1.0f), textureIndex),
                new ChunkMeshVertex(new Vector3f(0.0f, 0.0f, 1.0f).add(offset),
                        new Vector3f(0.0f, -1.0f, 0.0f),
                        new Vector2f(0.0f, 1.0f), textureIndex)));
    }

    public static ArrayList<PosOnlyMeshVertex> frontSidePosOnly(Vector3f offset) {
        return new ArrayList<>(Arrays.asList(
                new PosOnlyMeshVertex(new Vector3f(0.0f, 0.0f, 1.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(1.0f, 0.0f, 1.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(1.0f, 1.0f, 1.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(0.0f, 1.0f, 1.0f).add(offset))));
    }

    public static ArrayList<PosOnlyMeshVertex> backSidePosOnly(Vector3f offset) {
        return new ArrayList<>(Arrays.asList(
                new PosOnlyMeshVertex(new Vector3f(1.0f, 0.0f, 0.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(0.0f, 0.0f, 0.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(0.0f, 1.0f, 0.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(1.0f, 1.0f, 0.0f).add(offset))));
    }

    public static ArrayList<PosOnlyMeshVertex> leftSidePosOnly(Vector3f offset) {
        return new ArrayList<>(Arrays.asList(
                new PosOnlyMeshVertex(new Vector3f(0.0f, 0.0f, 0.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(0.0f, 0.0f, 1.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(0.0f, 1.0f, 1.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(0.0f, 1.0f, 0.0f).add(offset))));
    }

    public static ArrayList<PosOnlyMeshVertex> rightSidePosOnly(Vector3f offset) {
        return new ArrayList<>(Arrays.asList(
                new PosOnlyMeshVertex(new Vector3f(1.0f, 0.0f, 1.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(1.0f, 0.0f, 0.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(1.0f, 1.0f, 0.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(1.0f, 1.0f, 1.0f).add(offset))));
    }

    public static ArrayList<PosOnlyMeshVertex> topSidePosOnly(Vector3f offset) {
        return new ArrayList<>(Arrays.asList(
                new PosOnlyMeshVertex(new Vector3f(0.0f, 1.0f, 1.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(1.0f, 1.0f, 1.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(1.0f, 1.0f, 0.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(0.0f, 1.0f, 0.0f).add(offset))));
    }

    public static ArrayList<PosOnlyMeshVertex> bottomSidePosOnly(Vector3f offset) {
        return new ArrayList<>(Arrays.asList(
                new PosOnlyMeshVertex(new Vector3f(0.0f, 0.0f, 0.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(1.0f, 0.0f, 0.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(1.0f, 0.0f, 1.0f).add(offset)),
                new PosOnlyMeshVertex(new Vector3f(0.0f, 0.0f, 1.0f).add(offset))));
    }

    public static ArrayList<Integer> indices(int offset) {
        return new ArrayList<>(
                Arrays.asList(offset + 0, offset + 1, offset + 2, offset + 2, offset + 3, offset + 0));
    }

    public static PosOnlyMesh cubeMesh() {
        ArrayList<PosOnlyMeshVertex> verticesList = new ArrayList<>(24);
        verticesList.addAll(frontSidePosOnly(new Vector3f(0)));
        verticesList.addAll(backSidePosOnly(new Vector3f(0)));
        verticesList.addAll(leftSidePosOnly(new Vector3f(0)));
        verticesList.addAll(rightSidePosOnly(new Vector3f(0)));
        verticesList.addAll(topSidePosOnly(new Vector3f(0)));
        verticesList.addAll(bottomSidePosOnly(new Vector3f(0)));

        ArrayList<Integer> indicesList = new ArrayList<>(36);
        indicesList.addAll(indices(0));
        indicesList.addAll(indices(4));
        indicesList.addAll(indices(8));
        indicesList.addAll(indices(12));
        indicesList.addAll(indices(16));
        indicesList.addAll(indices(20));

        return new PosOnlyMesh(verticesList, indicesList);
    }
}
