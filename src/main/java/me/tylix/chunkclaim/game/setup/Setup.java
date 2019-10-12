package me.tylix.chunkclaim.game.setup;

import me.tylix.chunkclaim.ChunkClaim;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Setup {

    private final String message;
    private int task;
    private int count = 0;
    private String finalMessage = "";

    public Setup(Player player) {
        this.start(player);
        message = "Welcome " + player.getName()  +  "!\nLets begin the Setup!";
    }

    private void start(Player player) {
        task = Bukkit.getScheduler().scheduleAsyncRepeatingTask(ChunkClaim.INSTANCE, () -> {
            for (int i = 0; i < 200; i++)
                player.sendMessage(" ");
            finalMessage = finalMessage + message.charAt(count);
            player.sendMessage(finalMessage);
            count++;
            if (count == message.length())
                Bukkit.getScheduler().cancelTask(task);
        }, 0, 2);
    }

}
