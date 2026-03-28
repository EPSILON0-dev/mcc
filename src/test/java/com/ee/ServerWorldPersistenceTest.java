package com.ee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.joml.Vector2i;
import org.joml.Vector3i;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import com.ee.Common.Block;
import com.ee.Common.BlockType;
import com.ee.Server.ServerWorld;

public class ServerWorldPersistenceTest {

    @TempDir
    Path tempDir;

    @Test
    public void saveAndLoadRoundTripsChunkData() throws IOException {
        Path worldFile = tempDir.resolve("save/world.json");
        try (ServerWorld world = new ServerWorld()) {
            world.getOrGenerateChunk(new Vector2i(0, 0));
            world.getOrGenerateChunk(new Vector2i(-2, 3));
            world.setBlock(new Vector3i(1, 65, 1), new Block(BlockType.Sand));
            world.setBlock(new Vector3i(-31, 12, 49), new Block(BlockType.OakLog));
            world.save(worldFile);

            try (ServerWorld loadedWorld = ServerWorld.load(worldFile)) {
                assertEquals(world.chunkCount(), loadedWorld.chunkCount());
                assertEquals(BlockType.Sand, loadedWorld.getBlock(new Vector3i(1, 65, 1)).type);
                assertEquals(BlockType.OakLog, loadedWorld.getBlock(new Vector3i(-31, 12, 49)).type);
            }
        }
    }

    @Test
    public void loadReturnsEmptyWorldWhenFileDoesNotExist() throws IOException {
        try (ServerWorld world = ServerWorld.load(tempDir.resolve("missing-world.json"))) {
            assertTrue(world.isEmpty());
        }
    }

    @Test
    public void saveWritesChunkArrayJson() throws IOException {
        Path worldFile = tempDir.resolve("world.json");
        try (ServerWorld world = new ServerWorld()) {
            world.getOrGenerateChunk(new Vector2i(1, -1));
            world.save(worldFile);
        }

        String json = Files.readString(worldFile);
        assertTrue(json.startsWith("["));
        assertTrue(json.contains("\"pos\""));
        assertTrue(json.contains("\"hash\""));
        assertTrue(json.contains("\"data\""));
        assertFalse(json.contains("\n\n"));
    }
}
