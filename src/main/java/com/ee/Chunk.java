package com.ee;

import java.util.ArrayList;

import org.joml.*;

public class Chunk {
    public static final Vector3i chunkSize = new Vector3i(16, 128, 16);
    private Vector2i worldPosition;
    private Block[] blocks;
    private ChunkMesh mesh;

    public Chunk(Vector2i worldPosition) {
        this.worldPosition = worldPosition;
        this.blocks = new Block[chunkSize.x * chunkSize.y * chunkSize.z];
    }

    public void generateBlocks() {
        try {
            for (int x = 0; x < chunkSize.x; x++) {
                for (int y = 0; y < chunkSize.y; y++) {
                    for (int z = 0; z < chunkSize.z; z++) {
                        generateBlock(new Vector3i(x, y, z));
                    }
                }
            }
        } catch (Exception e) {
            // Pass
        }
    }

    private void generateBlock(Vector3i position) {
        var block = (position.y > 64) ? new Block(BlockType.Air)
                : (position.y == 64) ? new Block(BlockType.Grass)
                        : (position.y > 60) ? new Block(BlockType.Dirt) : new Block(BlockType.Cobblestone);
        setBlock(position, block);
    }

    public void setBlock(Vector3i position, Block block) throws IndexOutOfBoundsException {
        blocks[positionToIndex(position)] = block;
    }

    public Block getBlock(Vector3i position) throws IndexOutOfBoundsException {
        return blocks[positionToIndex(position)];
    }

    public void generateMesh() {
        ArrayList<ChunkMeshVertex> vertices = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<>();
        GenerateBlockMeshes(vertices, indices);
        mesh = new ChunkMesh(vertices, indices);
    }

    public ChunkMesh mesh() {
        return mesh;
    }

    public Matrix4f modelMatrix() {
        return new Matrix4f().translate(worldPosition.x * chunkSize.x, 0, worldPosition.y * chunkSize.z);
    }

    private void GenerateBlockMeshes(ArrayList<ChunkMeshVertex> vertices, ArrayList<Integer> indices) {
        for (int x = 0; x < chunkSize.x; x++) {
            for (int y = 0; y < chunkSize.y; y++) {
                for (int z = 0; z < chunkSize.z; z++) {
                    GenerateBlockMesh(new Vector3i(x, y, z), vertices, indices);
                }
            }
        }
        System.out.println("Generated mesh for chunk at " + worldPosition + " with " + vertices.size() + " vertices and "
                + indices.size() / 3 + " triangles.");
    }

    private void GenerateBlockMesh(Vector3i position, ArrayList<ChunkMeshVertex> vertices, ArrayList<Integer> indices) {
        BlockType block = getBlock(position).type;

        if (block == BlockType.Air) {
            return;
        }

        Vector3f offset = new Vector3f(position);

        if (position.x <= 0 || getBlock(new Vector3i(position).sub(new Vector3i(1, 0, 0))).type == BlockType.Air) {
            int indexOffset = vertices.size();
            vertices.addAll(Cube.leftSide(offset, Block.getTextureIndex(block, BlockSide.Left)));
            indices.addAll(Cube.indices(indexOffset));
        }

        if (position.x >= chunkSize.x - 1 || getBlock(new Vector3i(position).add(new Vector3i(1, 0, 0))).type == BlockType.Air) {
            int indexOffset = vertices.size();
            vertices.addAll(Cube.rightSide(offset, Block.getTextureIndex(block, BlockSide.Right)));
            indices.addAll(Cube.indices(indexOffset));
        }

        if (position.y <= 0 || getBlock(new Vector3i(position).sub(new Vector3i(0, 1, 0))).type == BlockType.Air) {
            int indexOffset = vertices.size();
            vertices.addAll(Cube.bottomSide(offset, Block.getTextureIndex(block, BlockSide.Bottom)));
            indices.addAll(Cube.indices(indexOffset));
        }

        if (position.y >= chunkSize.y - 1 || getBlock(new Vector3i(position).add(new Vector3i(0, 1, 0))).type == BlockType.Air) {
            int indexOffset = vertices.size();
            vertices.addAll(Cube.topSide(offset, Block.getTextureIndex(block, BlockSide.Top)));
            indices.addAll(Cube.indices(indexOffset));
        }

        if (position.z <= 0 || getBlock(new Vector3i(position).sub(new Vector3i(0, 0, 1))).type == BlockType.Air) {
            int indexOffset = vertices.size();
            vertices.addAll(Cube.backSide(offset, Block.getTextureIndex(block, BlockSide.Back)));
            indices.addAll(Cube.indices(indexOffset));
        }

        if (position.z >= chunkSize.z - 1 || getBlock(new Vector3i(position).add(new Vector3i(0, 0, 1))).type == BlockType.Air) {
            int indexOffset = vertices.size();
            vertices.addAll(Cube.frontSide(offset, Block.getTextureIndex(block, BlockSide.Front)));
            indices.addAll(Cube.indices(indexOffset));
        }
    }

    private int positionToIndex(Vector3i position) throws IndexOutOfBoundsException {
        if (position.x >= chunkSize.x || position.x < 0 || position.y >= chunkSize.y || position.y < 0
                || position.z >= chunkSize.z || position.z < 0) {
            throw new IndexOutOfBoundsException("Position out of bounds");
        }

        return position.x + position.z * chunkSize.x + position.y * chunkSize.x * chunkSize.z;
    }
}
