package me.tylix.chunkclaim.listener;

import me.tylix.chunkclaim.ChunkClaim;
import me.tylix.chunkclaim.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitEventListener implements Listener {

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        ChunkClaim.INSTANCE.getPlayers().remove(ChunkClaim.INSTANCE.getChunkPlayer(player));

        event.setQuitMessage(Message.PLAYER_QUIT.getMessage(player));
    }

}
