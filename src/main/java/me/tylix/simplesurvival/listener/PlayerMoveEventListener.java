package me.tylix.simplesurvival.listener;

import me.tylix.simplesurvival.SimpleSurvival;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveEventListener implements Listener {

    @EventHandler
    public void handleMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        SimpleSurvival.INSTANCE.getScoreboardManager().updateScoreboard(player);
    }

}
