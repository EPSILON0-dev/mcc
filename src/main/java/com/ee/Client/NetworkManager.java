package com.ee.Client;

import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.joml.Vector3i;

import com.ee.Common.BlockType;
import com.ee.Common.Config;
import com.ee.Common.Network.*;

public class NetworkManager implements AutoCloseable {
    private DatagramSocket socket;
    private NetworkListener listener;
    private ClientWorld world;
    private ScheduledExecutorService heartbeatScheduler;

    public NetworkManager(ClientWorld world) {
        this.world = world;
        try {
            this.socket = new DatagramSocket();
            this.listener = new NetworkListener(this.socket, this.world);
            new Thread(this.listener).start();
            this.heartbeatScheduler = Executors.newSingleThreadScheduledExecutor(runnable -> {
                Thread thread = new Thread(runnable, "network-heartbeat");
                thread.setDaemon(true);
                return thread;
            });
            this.heartbeatScheduler.scheduleAtFixedRate(
                    this::sendHeartbeat,
                    0,
                    Config.NETWORK_HEARTBEAT_INTERVAL_MS,
                    TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            if (this.heartbeatScheduler != null) {
                this.heartbeatScheduler.shutdownNow();
            }
            this.socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendHeartbeat() {
        sendPacket(new Heartbeat().serialize());
    }

    public void sendBlockUpdate(Vector3i blockPos, BlockType blockType) {
        BlockUpdate request = new BlockUpdate(blockPos.x, blockPos.y, blockPos.z, blockType);
        sendPacket(request.serialize());
        System.out.println("[NET] Send Block Update " + blockPos + ", " + blockType);
    }

    public void requestChunk(int chunkX, int chunkZ) {
        ChunkRequest request = new ChunkRequest(chunkX, chunkZ);
        sendPacket(request.serialize());
        System.out.println("[NET] Send Chunk Request " + chunkX + ", " + chunkZ);
    }

    protected void sendPacket(byte[] data) {
        DatagramPacket udpPacket = new DatagramPacket(data, data.length);
        try {
            // TODO hardcoded for now
            udpPacket.setAddress(java.net.InetAddress.getByName("localhost"));
            udpPacket.setPort(6767);
            socket.send(udpPacket);
        } catch (Exception e) {
            if (!socket.isClosed()) {
                e.printStackTrace();
            }
        }
    }
}
