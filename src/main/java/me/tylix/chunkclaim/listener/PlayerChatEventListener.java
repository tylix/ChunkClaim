package me.tylix.chunkclaim.listener;

import me.tylix.chunkclaim.ChunkClaim;
import me.tylix.chunkclaim.game.setup.Setup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatEventListener implements Listener {

    @EventHandler
    public void handleChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (ChunkClaim.INSTANCE.getSetupMap().containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            Setup setup = ChunkClaim.INSTANCE.getSetupMap().get(player.getUniqueId());
            if(event.getMessage().equalsIgnoreCase("skip")) {
                setup.cancelAnimation();
                return;
            }
            switch (setup.getId()) {
                case 0:
                    setup.nextStep(1);
                    break;
                case 1:
                    setup.setId(2);
                    break;
                case 2:
                    setup.nextStep(3);
                    break;
            }
        }
    }

}
