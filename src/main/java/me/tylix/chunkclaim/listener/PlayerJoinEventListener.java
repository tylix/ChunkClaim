package me.tylix.chunkclaim.listener;

import me.tylix.chunkclaim.ChunkClaim;
import me.tylix.chunkclaim.game.LocationManager;
import me.tylix.chunkclaim.game.player.ChunkPlayer;
import me.tylix.chunkclaim.message.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventListener implements Listener {

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        ChunkClaim.INSTANCE.getPlayers().add(new ChunkPlayer(player.getUniqueId()));

        try {
            boolean exists = ChunkClaim.INSTANCE.getChunkPlayer(player).createIfNotExists();
            if (!exists) {
                final Location location = new LocationManager("ChunkClaim").getLocation("Spawn");
                player.teleport(location);
            }
        } catch (Exception e) {
            player.sendMessage(" ");
            player.sendMessage(Message.PREFIX.getMessage() + " §cThe Spawn doesn't set yet!");
            player.sendMessage(Message.PREFIX.getMessage() + " §cStart the setup with §c§o/cc setup§c!");
            player.sendMessage(" ");
        }

        ChunkClaim.INSTANCE.getChunkPlayer(player).setItem();

        event.setJoinMessage(Message.PLAYER_JOIN.getMessage(player));

        ChunkClaim.INSTANCE.getScoreboardManager().setScoreboard(player);
    }

}
