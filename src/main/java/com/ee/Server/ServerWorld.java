package com.ee.Server;

import java.lang.Math;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.HashMap;
import org.joml.*;

import com.ee.Common.Block;
import com.ee.Common.BlockType;
import com.ee.Common.Chunk;
import com.ee.Common.CompressedChunk;
import com.ee.Common.Config;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;

public class ServerWorld implements AutoCloseable {
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().create();

    private final HashMap<Vector2i, Chunk> chunks;

    public ServerWorld() {
        chunks = new HashMap<>();
    }

    private record PersistedChunk(int[] pos, int hash, String data) {
    }

    @Override
    public void close() {
    }

    public synchronized Chunk getOrGenerateChunk(Vector2i chunkPos) {
        if (!chunks.containsKey(chunkPos)) {
            var chunk = new Chunk(chunkPos);
            chunk.generateBlocks();
            chunks.put(chunkPos, chunk);
        }
        return chunks.get(chunkPos);
    }

    public synchronized void setBlock(Vector3i worldPos, Block block) throws IndexOutOfBoundsException {
        var chunkPos = getChunkInWorld(worldPos);
        if (!chunks.containsKey(chunkPos)) {
            throw new IndexOutOfBoundsException("Chunk not found");
        }
        var blockPos = getBlockInChunk(worldPos);
        chunks.get(chunkPos).setBlock(blockPos, block);
    }

    public synchronized Block getBlock(Vector3i worldPos) throws IndexOutOfBoundsException {
        var chunkPos = getChunkInWorld(worldPos);
        if (!chunks.containsKey(chunkPos)) {
            throw new IndexOutOfBoundsException("Chunk not found");
        }
        var blockPos = getBlockInChunk(worldPos);
        return chunks.get(chunkPos).getBlock(blockPos);
    }

    public Block getBlockNoThrow(Vector3i worldPos) {
        try {
            return getBlock(worldPos);
        } catch (Exception e) {
            return new Block(BlockType.Air);
        }
    }

    public synchronized Chunk getChunk(Vector2i chunkPos) {
        return chunks.getOrDefault(chunkPos, null);
    }

    public synchronized Chunk getChunkAtPos(Vector3i chunkPos) {
        return chunks.getOrDefault(getChunkInWorld(chunkPos), null);
    }

    public static Vector3i getBlockInChunk(Vector3i position) {
        int x = (position.x >= 0) ? position.x % Config.CHUNK_SIZE.x
                : (Config.CHUNK_SIZE.x - 1 - (-position.x - 1) % Config.CHUNK_SIZE.x);
        int z = (position.z >= 0) ? position.z % Config.CHUNK_SIZE.z
                : (Config.CHUNK_SIZE.z - 1 - (-position.z - 1) % Config.CHUNK_SIZE.z);
        return new Vector3i(x, position.y, z);
    }

    public static Vector2i getChunkInWorld(Vector3i position) {
        int x = Math.floorDiv(position.x, Config.CHUNK_SIZE.x);
        int z = Math.floorDiv(position.z, Config.CHUNK_SIZE.z);
        return new Vector2i(x, z);
    }

    public synchronized boolean hasChunk(Vector2i chunkPos) {
        return chunks.containsKey(chunkPos);
    }

    public synchronized boolean isEmpty() {
        return chunks.isEmpty();
    }

    public synchronized int chunkCount() {
        return chunks.size();
    }

    public boolean isSolid(int x, int y, int z) {
        return getBlockNoThrow(new Vector3i(x, y, z)).type != BlockType.Air;
    }

    public synchronized void save(Path worldFile) throws IOException {
        Path parent = worldFile.toAbsolutePath().getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        ArrayList<PersistedChunk> persistedChunks = chunks.values().stream()
                .sorted(Comparator
                        .comparingInt((Chunk chunk) -> chunk.worldPosition().x)
                        .thenComparingInt(chunk -> chunk.worldPosition().y))
                .map(CompressedChunk::new)
                .map(chunk -> new PersistedChunk(
                        new int[] { chunk.worldPosition().x, chunk.worldPosition().y },
                        chunk.hash(),
                        Base64.getEncoder().encodeToString(chunk.compressedData())))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll);

        try (Writer writer = Files.newBufferedWriter(worldFile)) {
            GSON.toJson(persistedChunks, writer);
        } catch (JsonIOException e) {
            throw new IOException("Failed to write world file: " + worldFile, e);
        }
    }

    public static ServerWorld load(Path worldFile) throws IOException {
        ServerWorld world = new ServerWorld();
        if (!Files.exists(worldFile)) {
            return world;
        }

        try (Reader reader = Files.newBufferedReader(worldFile)) {
            PersistedChunk[] persistedChunks = GSON.fromJson(reader, PersistedChunk[].class);
            if (persistedChunks == null) {
                return world;
            }

            for (PersistedChunk persistedChunk : persistedChunks) {
                if (persistedChunk.pos() == null || persistedChunk.pos().length != 2) {
                    throw new IOException("Invalid chunk position in world file: " + worldFile);
                }
                if (persistedChunk.data() == null) {
                    throw new IOException("Missing chunk data in world file: " + worldFile);
                }

                byte[] compressedData = Base64.getDecoder().decode(persistedChunk.data());
                Vector2i position = new Vector2i(persistedChunk.pos()[0], persistedChunk.pos()[1]);
                Chunk chunk = new CompressedChunk(position, persistedChunk.hash(), compressedData).decompress();
                world.chunks.put(chunk.worldPosition(), chunk);
            }
        } catch (JsonParseException | IllegalArgumentException | IllegalStateException e) {
            throw new IOException("Failed to load world file: " + worldFile, e);
        }

        return world;
    }
}
