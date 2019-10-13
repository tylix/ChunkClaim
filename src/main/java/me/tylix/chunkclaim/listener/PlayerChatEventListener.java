package me.tylix.chunkclaim.listener;

import me.tylix.chunkclaim.ChunkClaim;
import me.tylix.chunkclaim.game.LocationManager;
import me.tylix.chunkclaim.game.setup.Setup;
import me.tylix.chunkclaim.message.Message;
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
            if (event.getMessage().equalsIgnoreCase("skip")) {
                setup.cancelAnimation();
                return;
            }
            switch (setup.getId()) {
                case 0:
                    setup.nextStep(1);
                    new LocationManager("ChunkClaim").createLocation(player, "Spawn").save();
                    break;
                case 1:
                case 2:
                    player.sendMessage((setup.getId() == 1 ? "First" : "Second") + " location set!");
                    new LocationManager("ChunkClaim").createLocation(player, "SpawnArea." + setup.getId()).save();
                    setup.nextStep(setup.getId() + 1);
                    break;
            }
        }
    }

}
