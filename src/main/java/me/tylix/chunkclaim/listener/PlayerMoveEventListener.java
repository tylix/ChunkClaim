package me.tylix.chunkclaim.listener;

import me.tylix.chunkclaim.ChunkClaim;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveEventListener implements Listener {

    @EventHandler
    public void handleMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        ChunkClaim.INSTANCE.getScoreboardManager().updateScoreboard(player);
    }

}
