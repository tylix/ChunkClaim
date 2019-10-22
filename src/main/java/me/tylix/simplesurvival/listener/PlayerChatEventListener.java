package me.tylix.simplesurvival.listener;

import me.tylix.simplesurvival.SimpleSurvival;
import me.tylix.simplesurvival.game.LocationManager;
import me.tylix.simplesurvival.game.setup.Setup;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatEventListener implements Listener {

    @EventHandler
    public void handleChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (SimpleSurvival.INSTANCE.getSetupMap().containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            Setup setup = SimpleSurvival.INSTANCE.getSetupMap().get(player.getUniqueId());
            if (event.getMessage().equalsIgnoreCase("skip")) {
                setup.cancelAnimation();
                return;
            }
            switch (setup.getId()) {
                case 0:
                    setup.nextStep(1);
                    new LocationManager("SimpleSurvival").createLocation(player, "Spawn").save();
                    break;
                case 1:
                case 2:
                    player.sendMessage((setup.getId() == 1 ? "First" : "Second") + " location set!");
                    new LocationManager("SimpleSurvival").createLocation(player, "SpawnArea." + setup.getId()).save();
                    setup.nextStep(setup.getId() + 1);
                    break;
            }
        }
    }

}
