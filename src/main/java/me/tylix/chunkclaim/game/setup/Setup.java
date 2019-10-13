package me.tylix.chunkclaim.game.setup;

import me.tylix.chunkclaim.ChunkClaim;
import me.tylix.chunkclaim.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Setup {

    private final Player player;
    private String message;
    private int task;
    private int count = 0;
    private int id = 0;
    private String finalMessage = "";

    public Setup(Player player) {
        this.player = player;
        this.start(2);
        message = "Welcome " + player.getName() + "!\nThanks for downloading the plugin!\n \nLets begin the Setup!\nEnter \"skip\" to skip the animations\nFist, you have to set the spawn. Stand in the position and enter \"set\" in the chat!";
    }

    private void start(int time) {
        task = Bukkit.getScheduler().scheduleAsyncRepeatingTask(ChunkClaim.INSTANCE, () -> {
            for (int i = 0; i < 200; i++)
                player.sendMessage(" ");
            finalMessage = finalMessage + message.charAt(count);
            player.sendMessage(finalMessage);
            count++;
            if (count == message.length()) {
                Bukkit.getScheduler().cancelTask(task);
                count = 0;
                finalMessage = "";
            }
        }, 0, time);
    }

    public void nextStep(int id) {
        this.id = id;
        switch (id) {
            case 1:
                start(2);
                player.sendMessage(Message.PREFIX.getMessage() + "§aSpawn successfully set!");
                message = "Now set spawn are";
                break;
            case 3:
                start(2);
                message = "lol";
                break;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void cancelAnimation() {
        for (int i = 0; i < 200; i++)
            player.sendMessage(" ");
        Bukkit.getScheduler().cancelTask(task);
        count = 0;
        finalMessage = "";
        player.sendMessage(message);
    }
}
