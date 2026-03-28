package com.ee.Common.Network;

public class Heartbeat implements NetworkPacket {
    @Override
    public byte[] serialize() {
        return new byte[] { (byte) PacketType.HEARTBEAT.ordinal() };
    }

    public static Heartbeat deserialize(byte[] data, int length) {
        if (data.length < length || length != 1) {
            throw new IllegalArgumentException("Data length is not valid for Heartbeat");
        }

        PacketType type = PacketType.values()[data[0]];
        if (type != PacketType.HEARTBEAT) {
            throw new IllegalArgumentException("Invalid packet type for Heartbeat");
        }

        return new Heartbeat();
    }
}
