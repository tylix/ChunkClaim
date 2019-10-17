package me.tylix.chunkclaim.listener;

import me.tylix.chunkclaim.ChunkClaim;
import me.tylix.chunkclaim.game.deathcause.DeathCauseData;
import me.tylix.chunkclaim.message.Message;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.Random;

public class PlayerDeathEventListener implements Listener {

    @EventHandler
    public void handleDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (killer == null) {
            final DeathCauseData data = ChunkClaim.INSTANCE.getDeathCauseLoader().getData(player.getLastDamageCause().getCause());
            if (data != null) {
                String message = data.getMessage()[new Random().nextInt(data.getMessage().length)];
                event.setDeathMessage(Message.PLAYER_DEATH.getMessage(player.getName(), message));
            } else
                event.setDeathMessage(Message.PLAYER_DEATH.getMessage(player.getName(), "died"));

            player.sendMessage(Message.TELEPORT_TO_DEATH_POINT.getMessage());
        } else {
            event.setDeathMessage(Message.PLAYER_KILLED.getMessage(player.getName(), killer.getName()));
        }
    }

}
