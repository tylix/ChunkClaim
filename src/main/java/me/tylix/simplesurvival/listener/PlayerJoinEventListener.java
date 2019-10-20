package me.tylix.simplesurvival.listener;

import me.tylix.simplesurvival.SimpleSurvival;
import me.tylix.simplesurvival.game.LocationManager;
import me.tylix.simplesurvival.game.player.ChunkPlayer;
import me.tylix.simplesurvival.message.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventListener implements Listener {

    @EventHandler
    public void handleJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        SimpleSurvival.INSTANCE.getPlayers().add(new ChunkPlayer(player.getUniqueId()));

        try {
            boolean exists = SimpleSurvival.INSTANCE.getChunkPlayer(player).createIfNotExists();
            if (!exists) {
                final Location location = new LocationManager("SimpleSurvival").getLocation("Spawn");
                player.teleport(location);
            }
        } catch (Exception e) {
            player.sendMessage(" ");
            player.sendMessage(Message.PREFIX.getMessage() + " §cThe Spawn doesn't set yet!");
            player.sendMessage(Message.PREFIX.getMessage() + " §cStart the setup with §c§o/cca setup§c!");
            player.sendMessage(" ");
        }

        SimpleSurvival.INSTANCE.getWarpPage().put(player.getUniqueId(), 1);

        // SimpleSurvival.INSTANCE.getChunkPlayer(player).setItem();

        event.setJoinMessage(Message.PLAYER_JOIN.getMessage(player));

        SimpleSurvival.INSTANCE.getScoreboardManager().setScoreboard(player);
    }

}
