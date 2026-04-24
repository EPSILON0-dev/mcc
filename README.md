# MCC

MCC - MineCraftClone - Small Java voxel sandbox prototype built with LWJGL, OpenGL, JOML, and ImGui.

![Game Screenshot Placeholder](assets/textures/ss.png)

## Current Features

- Chunk-based voxel world generation
- Client/server architecture over UDP
- On-demand chunk streaming from the server
- Block breaking and placement synchronized over the network
- Heartbeat packets to keep client connections alive
- Client-side chunk request TTL to avoid spamming duplicate requests
- Automatic unloading of chunks that stay outside render distance for too long
- ImGui debug overlay with runtime information
- CLI arguments for server IP, port, render distance, and server world file

### Planned Features

- Player models
- More advanced rendering pipeline (greedy meshing, shadows, point light torches)

## Network Communication

All traffic between client and server uses UDP. There are four packet types:

| Packet | Direction | Size | Description |
|---|---|---|---|
| `HEARTBEAT` | client → server | 1 byte | Sent periodically to keep the client registered on the server |
| `CHUNK_REQUEST` | client → server | 9 bytes | Requests chunk data at a given (chunkX, chunkZ) |
| `CHUNK_RESPONSE` | server → client | 13 + N bytes | Returns the compressed block data for the requested chunk |
| `BLOCK_UPDATE` | client → server → all clients | 14 bytes | Reports a block break or placement; server rebroadcasts to every active client |

The server tracks active clients by the source address of the most recently received packet. Clients that have not sent any packet within the configured TTL are pruned and will no longer receive broadcasts.

### Heartbeat

```
Client                                    Server
  |                                          |
  |--- HEARTBEAT (periodic) ---------------->|  registers / refreshes client TTL
  |                                          |
```

### Chunk retrieval

```
Client                                    Server
  |                                          |
  |--- CHUNK_REQUEST (chunkX, chunkZ) ------>|
  |<-- CHUNK_RESPONSE (compressed data) -----|  server generates chunk if missing
  |                                          |
```

### Block update

```
Client                                    Server
  |                                          |
  |--- BLOCK_UPDATE (x, y, z, type) -------->|  player breaks / places a block
  |                                          |
  |                                          |--- BLOCK_UPDATE (broadcast) --> all active clients
  |                                          |
  |<-- BLOCK_UPDATE (x, y, z, type) ---------|  including the originating client
```

## Controls

- `W A S D`: move
- `Mouse`: look around
- `Space`: jump
- `Left Shift`: sprint
- `Left Click`: break block
- `Right Click`: place block
- `Middle Click`: pick block
- `Mouse Wheel`: cycle selected block
- `Esc`: close the game

## Requirements

- Java 21
- Maven
- A platform that supports LWJGL/OpenGL

## How To Run

### 1. Compile

```bash
mvn -q -DskipTests compile
```

If your shell still defaults to an older JDK, set `JAVA_HOME` to Java 21 first.

Example on Linux:

```bash
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk
export PATH="$JAVA_HOME/bin:$PATH"
mvn -q -DskipTests compile
```

## CLI Arguments

### Server

- `--port=<number>`
- `--server-port=<number>`
- `--world-file=<path>` defaults to `world.json`

### Client

- `--server-ip=<host>`
- `--server-host=<host>`
- `--server-port=<number>`
- `--port=<number>`
- `--render-distance=<number>`

## Debug Overlay

The in-game debug menu currently shows:

- FPS
- player position, direction, and velocity
- current chunk and chunk hash
- compressed chunk size
- selected block
- loaded chunk count
- connected server IP and port
- sent packet count
- received packet count

## Notes

This is a simple Minecraft clone I made to learn Java. Since we all know that the minecraft is the only non-enterprise use case for Java this seemed like the only viable project idea.

## License

MIT — see [LICENSE](LICENSE).
