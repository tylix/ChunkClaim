package me.tylix.chunkclaim.game.chunk.data;

import me.tylix.chunkclaim.game.chunk.location.ChunkLocation;

import java.util.List;
import java.util.UUID;

public class ChunkData {

    private final String id;
    private final UUID owner;
    private final List<ChunkUserData> member;
    private final ChunkLocation maxLocation;
    private final ChunkLocation minLocation;

    public ChunkData(String id, UUID owner, List<ChunkUserData> member, ChunkLocation maxLocation, ChunkLocation minLocation) {
        this.id = id;
        this.owner = owner;
        this.member = member;
        this.maxLocation = maxLocation;
        this.minLocation = minLocation;
    }

    public String getId() {
        return id;
    }

    public UUID getOwner() {
        return owner;
    }

    public List<ChunkUserData> getMember() {
        return member;
    }

    public ChunkLocation getMaxLocation() {
        return maxLocation;
    }

    public ChunkLocation getMinLocation() {
        return minLocation;
    }
}
