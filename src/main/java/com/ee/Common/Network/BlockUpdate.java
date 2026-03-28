package com.ee.Common.Network;

import org.joml.*;
import com.ee.Common.BlockType;

public class BlockUpdate implements NetworkPacket {
    private final int blockX;
    private final int blockY;
    private final int blockZ;
    private final BlockType type;

    public BlockUpdate(int blockX, int blockY, int blockZ, BlockType type) {
        this.blockX = blockX;
        this.blockY = blockY;
        this.blockZ = blockZ;
        this.type = type;
    }

    public int blockX() {
        return this.blockX;
    }

    public int blockY() {
        return this.blockY;
    }

    public int blockZ() {
        return this.blockZ;
    }

    public Vector3i blockPos() {
        return new Vector3i(blockX, blockY, blockZ);
    }

    public BlockType blockType() {
        return this.type;
    }

    @Override
    public byte[] serialize() {
        byte[] data = new byte[14];
        data[0] = (byte) PacketType.BLOCK_UPDATE.ordinal();
        data[1] = (byte) (this.blockX >> 24);
        data[2] = (byte) (this.blockX >> 16);
        data[3] = (byte) (this.blockX >> 8);
        data[4] = (byte) this.blockX;
        data[5] = (byte) (this.blockY >> 24);
        data[6] = (byte) (this.blockY >> 16);
        data[7] = (byte) (this.blockY >> 8);
        data[8] = (byte) this.blockY;
        data[9] = (byte) (this.blockZ >> 24);
        data[10] = (byte) (this.blockZ >> 16);
        data[11] = (byte) (this.blockZ >> 8);
        data[12] = (byte) this.blockZ;
        data[13] = (byte) this.type.ordinal();
        return data;
    }

    public static BlockUpdate deserialize(byte[] data, int length) {
        if (data.length < length || length != 14) {
            throw new IllegalArgumentException("Data length is not valid for ChunkRequest");
        }
        PacketType type = PacketType.values()[data[0]];
        if (type != PacketType.BLOCK_UPDATE) {
            throw new IllegalArgumentException("Invalid packet type for ChunkRequest");
        }
        int blockX = ((data[1] & 0xFF) << 24) | ((data[2] & 0xFF) << 16) | ((data[3] & 0xFF) << 8) | (data[4] & 0xFF);
        int blockY = ((data[5] & 0xFF) << 24) | ((data[6] & 0xFF) << 16) | ((data[7] & 0xFF) << 8) | (data[8] & 0xFF);
        int blockZ = ((data[9] & 0xFF) << 24) | ((data[10] & 0xFF) << 16) | ((data[11] & 0xFF) << 8)
                | (data[12] & 0xFF);
        BlockType blockType = BlockType.values()[data[13]];
        return new BlockUpdate(blockX, blockY, blockZ, blockType);
    }
}
