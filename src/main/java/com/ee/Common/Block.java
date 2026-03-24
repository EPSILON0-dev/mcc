package com.ee.Common;

import com.ee.Client.BlockSide;

public class Block {
    public BlockType type;

    public Block(BlockType block) {
        this.type = block;
    }

    public static int getTextureIndex(BlockType block, BlockSide side) {
        switch (block) {
            case Air:
                return BlockTextures.Air.ordinal();
            case Stone:
                return BlockTextures.Stone.ordinal();
            case Cobblestone:
                return BlockTextures.Cobblestone.ordinal();
            case Bedrock:
                return BlockTextures.Bedrock.ordinal();
            case Dirt:
                return BlockTextures.Dirt.ordinal();
            case Grass:
                return side == BlockSide.Top ? BlockTextures.GrassTop.ordinal()
                        : side == BlockSide.Bottom ? BlockTextures.Dirt.ordinal() : BlockTextures.GrassSide.ordinal();
            case Sand:
                return BlockTextures.Sand.ordinal();
            case OakLog:
                return side == BlockSide.Top || side == BlockSide.Bottom ? BlockTextures.OakLogTop.ordinal()
                        : BlockTextures.OakLogSide.ordinal();
            case OakLeaves:
                return BlockTextures.OakLeaves.ordinal();
        }
        return BlockTextures.Air.ordinal();
    }
}
