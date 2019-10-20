package me.tylix.simplesurvival.game.warp.data;

import me.tylix.simplesurvival.game.chunk.location.ChunkLocation;
import org.bukkit.inventory.ItemStack;

public class WarpData {

    private final String name;
    private final ItemStack itemStack;
    private final ChunkLocation location;

    public WarpData(String name, ItemStack itemStack, ChunkLocation location) {
        this.name = name;
        this.itemStack = itemStack;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public ChunkLocation getLocation() {
        return location;
    }
}
