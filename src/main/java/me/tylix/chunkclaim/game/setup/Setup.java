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
    private boolean animation = true;

    public Setup(Player player) {
        this.player = player;
        this.start(2);
        message = "Welcome " + player.getName() + "!\nThanks for downloading the plugin!\nEnter \"skip\" to skip the animation\nLets begin the Setup!\n \nFist, you have to set the spawn. Stand in the position and enter \"set\" in the chat!";
    }

    private void start(int time) {
        if (animation)
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
        else {
            for (int i = 0; i < 200; i++)
                player.sendMessage(" ");
            player.sendMessage(message);
        }
    }

    public void nextStep(int id) {
        this.id = id;
        switch (id) {
            case 1:
                message = "Now you have to set the spawn area. Stand in the two positions and enter \"set\" in the chat!";
                start(2);
                break;
            case 3:
                message = "Finished";
                start(2);
                ChunkClaim.INSTANCE.getSetupMap().remove(player.getUniqueId());
                ChunkClaim.INSTANCE.loadSpawnArea();
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
        animation = false;
    }
}
