package me.tylix.simplesurvival.game.warp.data;

import me.tylix.simplesurvival.game.chunk.location.ChunkLocation;
import org.bukkit.Material;

public class WarpData {

    private final String name;
    private final Material material;
    private final ChunkLocation location;

    public WarpData(String name, Material material, ChunkLocation location) {
        this.name = name;
        this.material = material;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public ChunkLocation getLocation() {
        return location;
    }
}
