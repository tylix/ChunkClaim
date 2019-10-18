package me.tylix.chunkclaim.commands;

import me.tylix.chunkclaim.listener.PlayerDeathEventListener;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class BackCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if(!PlayerDeathEventListener.LOCATION_MAP.containsKey(player.getUniqueId())) {
            player.sendMessage(" ");
            return false;
        }

        player.teleport(PlayerDeathEventListener.LOCATION_MAP.get(player.getUniqueId()));
        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1.4F);

        return false;
    }
}
