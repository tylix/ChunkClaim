package me.tylix.simplesurvival.listener;

import me.tylix.simplesurvival.SimpleSurvival;
import me.tylix.simplesurvival.message.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitEventListener implements Listener {

    @EventHandler
    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        SimpleSurvival.INSTANCE.getPlayers().remove(SimpleSurvival.INSTANCE.getChunkPlayer(player));
        PlayerDeathEventListener.LOCATION_MAP.remove(player.getUniqueId());

        event.setQuitMessage(Message.PLAYER_QUIT.getMessage(player));
    }

}
