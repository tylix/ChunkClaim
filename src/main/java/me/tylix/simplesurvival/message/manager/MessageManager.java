package me.tylix.simplesurvival.message.manager;

import me.tylix.simplesurvival.SimpleSurvival;
import me.tylix.simplesurvival.config.Config;
import me.tylix.simplesurvival.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;

import java.io.File;
import java.io.IOException;

public class MessageManager {

    private final File file = new File("plugins/SimpleSurvival/data/messages/" + Config.MESSAGES.getData() + ".yml");
    private YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    public void loadValues() {
        System.out.println("Load messages...");
        for (Message value : Message.values())
            value.setMessage(this.cfg.get(value.name().toLowerCase()));
        System.out.println("All messages (" + Message.values().length + ") loaded.");
    }

    public void reload() {
        this.cfg = YamlConfiguration.loadConfiguration(file);
        this.loadValues();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.getScoreboard().clearSlot(DisplaySlot.SIDEBAR);
            SimpleSurvival.INSTANCE.getScoreboardManager().setScoreboard(onlinePlayer);
        }
    }

    public void createFileIfNotExists() {
        if (!this.file.exists()) {
            for (Message value : Message.values())
                this.cfg.set(value.name().toLowerCase(), value.getDefaultMessage());
            try {
                this.cfg.save(this.file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Message value : Message.values()) {
            if (this.cfg.get(value.name().toLowerCase()) == null) {
                this.cfg.set(value.name().toLowerCase(), value.getDefaultMessage());
                try {
                    this.cfg.save(this.file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void reset() {
        for (Message value : Message.values()) {
            this.cfg.set(value.name().toLowerCase(), value.getDefaultMessage());
            value.setMessage(value.getDefaultMessage());
        }
        try {
            this.cfg.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getCfg() {
        return cfg;
    }


}
