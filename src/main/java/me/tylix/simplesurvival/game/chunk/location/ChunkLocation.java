package me.tylix.simplesurvival.game.chunk.location;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ChunkLocation {

    private double x, y, z;
    private float yaw;
    private float pitch;
    private String world;

    public ChunkLocation(int x, int y, int z, float yaw, float pitch, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

    public ChunkLocation(Location location) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        this.world = location.getWorld().getName();
    }

    public ChunkLocation(Location location, float yaw, float pitch) {
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = location.getWorld().getName();
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public String getWorld() {
        return world;
    }

    public Location toBukkitLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z, yaw, pitch);
    }

    public String toString() {
        return world + "~" + x + "~" + y + "~" + z + "~" + yaw + "~" + pitch;
    }
}
