package me.tylix.simplesurvival.game.chunk.display;

import me.tylix.simplesurvival.SimpleSurvival;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ChunkDisplayTask {

    public void display(final Player player) {
        final World world = player.getWorld();
        final List<Location> blocksToDo = new ArrayList<>();
        final int showTimeInSeconds = 5;

        final int xStart = world.getChunkAt(player.getLocation()).getX() * 16;
        final int zStart = world.getChunkAt(player.getLocation()).getZ() * 16;
        final int yStart = (int) player.getLocation().getY() + 10;
        for (int ys = 0; ys < 3; ys++) {
            final  int y = yStart + ys;
            for (int i = 1; i < 16; i++) {
                blocksToDo.add(new Location(world, xStart + i, y, zStart));
                blocksToDo.add(new Location(world, xStart + i, y, zStart + 16));
            }
            for (int i = 0; i < 17; i++) {
                blocksToDo.add(new Location(world, xStart, y, zStart + i));
                blocksToDo.add(new Location(world, xStart + 16, y, zStart + i));
            }
        }

        for (Location loc : blocksToDo)
            for (int i = 0; i < showTimeInSeconds * 2 + 1; i++)
                Bukkit.getScheduler().scheduleSyncDelayedTask(SimpleSurvival.INSTANCE, () -> {
                    if (player.isOnline())
                        player.spawnParticle(Particle.TOTEM, loc, 0);
                }, i * 10);

    }

}
