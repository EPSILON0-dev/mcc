package com.ee.Common;

import org.joml.*;

public class Chunk {
    protected Vector2i worldPosition;
    protected Block[] blocks;

    public Chunk(Vector2i worldPosition) {
        this.worldPosition = worldPosition;
        this.blocks = new Block[Config.CHUNK_SIZE.x * Config.CHUNK_SIZE.y * Config.CHUNK_SIZE.z];
    }

    public void generateBlocks() {
        try {
            for (int x = 0; x < Config.CHUNK_SIZE.x; x++) {
                for (int y = 0; y < Config.CHUNK_SIZE.y; y++) {
                    for (int z = 0; z < Config.CHUNK_SIZE.z; z++) {
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

    private int positionToIndex(Vector3i position) throws IndexOutOfBoundsException {
        if (position.x >= Config.CHUNK_SIZE.x || position.x < 0 || position.y >= Config.CHUNK_SIZE.y || position.y < 0
                || position.z >= Config.CHUNK_SIZE.z || position.z < 0) {
            throw new IndexOutOfBoundsException("Position out of bounds");
        }

        return position.x + position.z * Config.CHUNK_SIZE.x + position.y * Config.CHUNK_SIZE.x * Config.CHUNK_SIZE.z;
    }
}
