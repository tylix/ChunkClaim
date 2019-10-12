package me.tylix.chunkclaim.game.chunk.data;

import me.tylix.chunkclaim.game.chunk.rank.ChunkRank;

import java.util.UUID;

public class ChunkUserData {

    private final UUID uuid;
    private final ChunkRank chunkRank;

    public ChunkUserData(UUID uuid, ChunkRank chunkRank) {
        this.uuid = uuid;
        this.chunkRank = chunkRank;
    }

    public UUID getUuid() {
        return uuid;
    }

    public ChunkRank getChunkRank() {
        return chunkRank;
    }
}
