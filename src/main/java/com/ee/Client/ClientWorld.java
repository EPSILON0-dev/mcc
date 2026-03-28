package com.ee.Client;

import java.util.HashMap;
import java.util.Optional;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Map;
import java.util.function.LongSupplier;
import java.lang.Math;
import org.joml.*;

import com.ee.Common.Block;
import com.ee.Common.BlockType;
import com.ee.Common.Chunk;
import com.ee.Common.Config;
import com.ee.Common.Util;

public class ClientWorld {
    private HashMap<Vector2i, ChunkRenderer> chunks;
    private LinkedList<ChunkRenderer> chunksToGenerateMesh;
    private HashMap<Vector2i, Long> requestedChunks;
    private HashMap<Vector2i, Long> outOfRangeChunks;
    private LongSupplier currentTimeSupplier;
    private int renderDistance;

    public ClientWorld() {
        this(Config.WORLD_CHUNK_DISTANCE);
    }

    public ClientWorld(int renderDistance) {
        this(System::currentTimeMillis, renderDistance);
    }

    public ClientWorld(LongSupplier currentTimeSupplier) {
        this(currentTimeSupplier, Config.WORLD_CHUNK_DISTANCE);
    }

    public ClientWorld(LongSupplier currentTimeSupplier, int renderDistance) {
        chunks = new HashMap<>();
        chunksToGenerateMesh = new LinkedList<>();
        requestedChunks = new HashMap<>();
        outOfRangeChunks = new HashMap<>();
        this.currentTimeSupplier = currentTimeSupplier;
        this.renderDistance = renderDistance;
    }

    public synchronized void addChunk(Vector2i chunkPos, Chunk chunk) {
        var renderer = new ChunkRenderer(chunk);
        if (requestedChunks.containsKey(chunkPos)) {
            requestedChunks.remove(chunkPos);
        }
        outOfRangeChunks.remove(chunkPos);
        chunks.put(chunkPos, renderer);
        chunksToGenerateMesh.add(renderer);
    }

    public synchronized void setBlock(Vector3i worldPos, Block block) throws IndexOutOfBoundsException {
        var chunkPos = getChunkInWorld(worldPos);
        if (!chunks.containsKey(chunkPos)) {
            throw new IndexOutOfBoundsException("Chunk not found");
        }
        var blockPos = getBlockInChunk(worldPos);
        chunks.get(chunkPos).setBlock(blockPos, block);
    }

    public synchronized void applyBlockUpdate(Vector3i worldPos, Block block) throws IndexOutOfBoundsException {
        var chunkPos = getChunkInWorld(worldPos);
        if (!chunks.containsKey(chunkPos)) {
            throw new IndexOutOfBoundsException("Chunk not found");
        }

        var blockPos = getBlockInChunk(worldPos);
        chunks.get(chunkPos).setBlock(blockPos, block);
        queueMeshGenerationForAffectedChunks(chunkPos, blockPos);
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

    public synchronized boolean containsChunk(Vector2i chunkPos) {
        return chunks.containsKey(chunkPos);
    }

    public synchronized int loadedChunkCount() {
        return chunks.size();
    }

    public synchronized boolean isSolid(int x, int y, int z) {
        return getBlockNoThrow(new Vector3i(x, y, z)).type != BlockType.Air;
    }

    public synchronized void requestMeshGeneration(Vector2i chunkPos) throws IndexOutOfBoundsException {
        if (!chunks.containsKey(chunkPos)) {
            throw new IndexOutOfBoundsException("Chunk not found");
        }
        var chunk = chunks.get(chunkPos);
        if (!chunksToGenerateMesh.contains(chunk))
            chunksToGenerateMesh.add(chunk);
    }

    private void queueMeshGenerationForAffectedChunks(Vector2i chunkPos, Vector3i blockPos) {
        ChunkRenderer chunk = chunks.get(chunkPos);
        if (chunk != null && !chunksToGenerateMesh.contains(chunk)) {
            chunksToGenerateMesh.add(chunk);
        }
    }

    public synchronized void generateQueuedMeshes() {
        // TODO move mesh generation onto another thread
        for (ChunkRenderer chunk : chunksToGenerateMesh) {
            chunk.generateMeshData();
            chunk.generateMesh();
        }
        chunksToGenerateMesh.clear();
    }

    private synchronized ChunkRenderer[] getLoadedChunks() {
        ChunkRenderer renderers[] = new ChunkRenderer[chunks.values().size()];
        chunks.values().toArray(renderers);
        return renderers;
    }

    public void renderChunks(Shader shader, String modelMatrixUniform) {
        ChunkRenderer[] renderers = getLoadedChunks();
        if (renderers == null)
            return;

        for (ChunkRenderer chunk : renderers) {
            if (!chunk.isReadyToRender())
                continue;
            shader.setUniformMatrix4f(modelMatrixUniform, chunk.modelMatrix());
            chunk.mesh().bind();
            chunk.mesh().draw();
            chunk.mesh().unbind();
        }
    }

    public void unloadDistantChunks(Player player) {
        long now = currentTimeSupplier.getAsLong();
        Vector2i playerChunk = getChunkInWorld(Util.vec3fToVec3i(player.position()));

        Iterator<Map.Entry<Vector2i, ChunkRenderer>> iterator = chunks.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Vector2i, ChunkRenderer> entry = iterator.next();
            Vector2i chunkPos = entry.getKey();

            if (isChunkWithinRenderDistance(chunkPos, playerChunk)) {
                outOfRangeChunks.remove(chunkPos);
                continue;
            }

            Long firstOutOfRangeAt = outOfRangeChunks.get(chunkPos);
            if (firstOutOfRangeAt == null) {
                outOfRangeChunks.put(new Vector2i(chunkPos), now);
                continue;
            }

            if (now - firstOutOfRangeAt >= Config.WORLD_CHUNK_UNLOAD_TTL_MS) {
                chunksToGenerateMesh.remove(entry.getValue());
                outOfRangeChunks.remove(chunkPos);
                iterator.remove();
            }
        }
    }

    public Optional<Vector2i> getNearestMissingChunk(Player player) {
        pruneExpiredRequestedChunks();
        Vector2i bestCandidate = new Vector2i(0, 0);
        float bestDistance = Float.MAX_VALUE;
        boolean found = false;
        var playerChunk = getChunkInWorld(Util.vec3fToVec3i(player.position()));
        for (int y = playerChunk.y - renderDistance; y < playerChunk.y + renderDistance; y++) {
            for (int x = playerChunk.x - renderDistance; x < playerChunk.x + renderDistance; x++) {
                var chunk = new Vector2i(x, y);
                if (!containsChunk(chunk) && chunk.distance(playerChunk) < bestDistance
                        && !requestedChunks.containsKey(chunk)) {
                    bestCandidate = chunk;
                    bestDistance = (float) chunk.distance(playerChunk);
                    found = true;
                }
            }
        }
        if (!found) {
            return Optional.empty();
        }

        Vector2i requestedChunk = new Vector2i(bestCandidate);
        requestedChunks.put(requestedChunk, currentTimeSupplier.getAsLong());
        return Optional.of(requestedChunk);
    }

    private void pruneExpiredRequestedChunks() {
        long cutoff = currentTimeSupplier.getAsLong() - Config.NETWORK_CHUNK_REQUEST_TTL_MS;
        Iterator<HashMap.Entry<Vector2i, Long>> iterator = requestedChunks.entrySet().iterator();
        while (iterator.hasNext()) {
            HashMap.Entry<Vector2i, Long> entry = iterator.next();
            if (entry.getValue() < cutoff) {
                iterator.remove();
            }
        }
    }

    private boolean isChunkWithinRenderDistance(Vector2i chunkPos, Vector2i playerChunk) {
        return Math.abs(chunkPos.x - playerChunk.x) < renderDistance
                && Math.abs(chunkPos.y - playerChunk.y) < renderDistance;
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
}
