package me.tylix.chunkclaim.listener;

import me.tylix.chunkclaim.ChunkClaim;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class ProtectionListener implements Listener {

    private ChunkClaim instance = ChunkClaim.INSTANCE;

    @EventHandler
    public void handleBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Chunk chunk = event.getBlock().getChunk();

        if (instance.getChunkManager().isFree(chunk)) {
            event.setCancelled(true);
            return;
        }

        if (!instance.getChunkManager().getOwner(chunk).equals(player.getUniqueId())) {
            event.setCancelled(true);
            return;
        }



    }

}
