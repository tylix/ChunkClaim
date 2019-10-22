package me.tylix.simplesurvival.game.home.data;

import me.tylix.simplesurvival.game.chunk.location.ChunkLocation;

public class HomeData {

    private final String name;
    private final ChunkLocation location;

    public HomeData(String name, ChunkLocation location) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public ChunkLocation getLocation() {
        return location;
    }
}
