package com.ee.Server;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicBoolean;

import org.joml.Vector2i;

import com.ee.Common.CliArgs;
import com.ee.Common.Config;

public class Main {
    private static int port = Config.NETWORK_SERVER_PORT;
    private static Path worldFile = Path.of(Config.WORLD_FILE);
    private static Listener listener;
    private static Thread listenerThread;
    private static ServerWorld world;
    private static final AtomicBoolean shutdownStarted = new AtomicBoolean(false);

    public static void main(String[] args) {
        try {
            CliArgs.ServerOptions options = CliArgs.parseServer(args);
            port = options.port();
            worldFile = Path.of(options.worldFile());
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.err.println("Server usage: --port=<port> --world-file=<path>");
            return;
        }

        try {
            world = ServerWorld.load(worldFile);
        } catch (IOException e) {
            System.err.println("Failed to load world file: " + worldFile);
            e.printStackTrace();
            return;
        }

        if (world.isEmpty()) {
            seedSpawnChunks();
        }

        Runtime.getRuntime().addShutdownHook(new Thread(Main::shutdown, "server-shutdown"));

        listener = new Listener(port, world);

        listenerThread = new Thread(listener, "server-listener");
        listenerThread.start();

        try {
            listenerThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private static void seedSpawnChunks() {
        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                world.getOrGenerateChunk(new Vector2i(x, z));
            }
        }
    }

    private static void shutdown() {
        if (!shutdownStarted.compareAndSet(false, true)) {
            return;
        }

        if (listener != null) {
            listener.close();
        }

        if (listenerThread != null && listenerThread != Thread.currentThread()) {
            try {
                listenerThread.join(2_000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (world != null) {
            try {
                world.save(worldFile);
                System.out.println("[WORLD] Saved to " + worldFile.toAbsolutePath());
            } catch (IOException e) {
                System.err.println("Failed to save world file: " + worldFile);
                e.printStackTrace();
            }
        }
    }
}
