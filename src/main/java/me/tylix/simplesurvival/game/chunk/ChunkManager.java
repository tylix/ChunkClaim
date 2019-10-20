package me.tylix.simplesurvival.game.chunk;

import me.tylix.simplesurvival.SimpleSurvival;
import me.tylix.simplesurvival.config.Config;
import me.tylix.simplesurvival.cuboid.Cuboid;
import me.tylix.simplesurvival.game.chunk.data.ChunkData;
import me.tylix.simplesurvival.game.chunk.location.ChunkLocation;
import me.tylix.simplesurvival.game.player.ChunkPlayer;
import me.tylix.simplesurvival.game.player.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ChunkManager {

    private final List<ChunkData> chunks;

    public ChunkManager() {
        this.chunks = new ArrayList<>();
    }

    public void loadChunks() {
        System.out.println("Loading Chunks...");
        for (UUID registeredPlayer : SimpleSurvival.INSTANCE.getRegisteredPlayers()) {
            final PlayerData playerData = new ChunkPlayer(registeredPlayer).getPlayerData();
            chunks.addAll(playerData.getChunks());
        }
        System.out.println("All Chunks (" + chunks.size() + ") loaded.");
    }

    public void setBiome(final Chunk chunk, final Biome biome) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                final Block block = chunk.getBlock(x, 0, z);
                block.setBiome(biome);
            }
        }
    }

    public boolean isInSpawnArea(Chunk chunk) {
        for (int x = 0; x < 16; x++)
            for (int y = 0; y < 256; y++)
                for (int z = 0; z < 16; z++) {
                    final Block b = chunk.getBlock(x, y, z);
                    if (SimpleSurvival.INSTANCE.getSpawnCuboid().contains(b))
                        return true;
                }

        return false;
    }


    public boolean isFree(final Chunk chunk) {
        final ChunkLocation maxLocation = new ChunkLocation(this.getMaxLocation(chunk));
        final ChunkLocation minLocation = new ChunkLocation(this.getMinLocation(chunk));
        for (ChunkData chunkData : this.chunks)
            if (chunkData.getMaxLocation().getX() == maxLocation.getX() && chunkData.getMaxLocation().getZ() == maxLocation.getZ() && chunkData.getMinLocation().getX() == minLocation.getX() && chunkData.getMinLocation().getZ() == minLocation.getZ())
                return false;
        return true;
    }

    public UUID getOwner(final Chunk chunk) throws NullPointerException {
        final ChunkLocation maxLocation = new ChunkLocation(this.getMaxLocation(chunk));
        final ChunkLocation minLocation = new ChunkLocation(this.getMinLocation(chunk));
        for (ChunkData chunkData : this.chunks)
            if (chunkData.getMaxLocation().getX() == maxLocation.getX() && chunkData.getMaxLocation().getZ() == maxLocation.getZ() && chunkData.getMinLocation().getX() == minLocation.getX() && chunkData.getMinLocation().getZ() == minLocation.getZ())
                return chunkData.getOwner();
        return null;
    }

    public Location getCenter(final ChunkData chunkData) {
        final Cuboid cuboid = new Cuboid(chunkData.getMinLocation().toBukkitLocation(), chunkData.getMaxLocation().toBukkitLocation());
        final Location center = cuboid.getCenter();
        center.setY(center.getWorld().getHighestBlockYAt(center) + 1);
        return center;
    }

    public UUID getChunkOwnerById(final String id) {
        for (ChunkData chunkData : this.chunks)
            if (chunkData.getId().equals(id))
                return chunkData.getOwner();
        return null;
    }

    public ChunkData getChunkDataById(final String id) {
        for (ChunkData chunk : this.chunks)
            if (chunk.getId().equals(id))
                return chunk;
        return null;
    }

    public List<ChunkData> getChunkDataByUuid(final UUID uuid) {
        final List<ChunkData> chunkData = new ArrayList<>();
        for (ChunkData chunk : this.chunks)
            if (chunk.getOwner().equals(uuid))
                chunkData.add(chunk);
        return chunkData;
    }

    public String getIdByUUID(final UUID uuid) {
        for (ChunkData chunk : this.chunks)
            if (chunk.getOwner().equals(uuid))
                return chunk.getId();
        return null;
    }

    public Location getMaxLocation(final Chunk chunk) {
        final int maxX = chunk.getX() * 16;
        final int maxY = 256;
        final int maxZ = chunk.getZ() * 16;
        return new Location(chunk.getWorld(), maxX, maxY, maxZ);
    }

    public Location getMinLocation(final Chunk chunk) {
        final int maxX = (chunk.getX() * 16) + 15;
        final int maxY = 0;
        final int maxZ = (chunk.getZ() * 16) + 15;
        return new Location(chunk.getWorld(), maxX, maxY, maxZ);
    }

    public List<ChunkData> getChunks() {
        return chunks;
    }

    public String getChunkMap(Location location) {
        final int size = 10;
        final int halfSize = size / 2;
        final StringBuilder stringBuilder = new StringBuilder();
        final List<String> rows = new ArrayList<>();

        for (int i = 1; i < halfSize + 1; i++) {
            int chunkAt = (i + 16);
            final Chunk chunk = Bukkit.getWorld((String) Config.CHUNK_WORLD.getData()).getChunkAt(location.add(chunkAt, 0, 0));
            boolean free = SimpleSurvival.INSTANCE.getChunkManager().isFree(chunk);
            rows.add(free ? "§a█" : "§c█");
        }


        for (int i = 1; i < halfSize + 1; i++) {
            int chunkAt = (i + 16);
            final Chunk chunk = Bukkit.getWorld((String) Config.CHUNK_WORLD.getData()).getChunkAt(location.add(chunkAt, 0, chunkAt));
            boolean free = SimpleSurvival.INSTANCE.getChunkManager().isFree(chunk);
            rows.add(free ? "§a█" : "§c█");
        }

        for (int i = 0; i < rows.size(); i++)
            stringBuilder.append(rows.get((rows.size() - i) - 1)).append("\n");

        return stringBuilder.toString();
    }
}
