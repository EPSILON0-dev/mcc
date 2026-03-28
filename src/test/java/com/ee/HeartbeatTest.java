package com.ee;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.jupiter.api.Test;

import com.ee.Common.Network.Heartbeat;
import com.ee.Common.Network.PacketType;

public class HeartbeatTest {

    @Test
    public void serializeProducesHeartbeatPacketType() {
        Heartbeat heartbeat = new Heartbeat();

        assertArrayEquals(new byte[] { (byte) PacketType.HEARTBEAT.ordinal() }, heartbeat.serialize());
    }

    @Test
    public void deserializeAcceptsSerializedHeartbeat() {
        Heartbeat heartbeat = new Heartbeat();

        assertDoesNotThrow(() -> Heartbeat.deserialize(heartbeat.serialize(), 1));
    }
}
