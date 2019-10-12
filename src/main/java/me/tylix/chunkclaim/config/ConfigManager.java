package me.tylix.chunkclaim.config;

import me.tylix.chunkclaim.message.Message;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ConfigManager {

    private final File file = new File("plugins//ChunkClaim//config.yml");
    private YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

    public void loadValues() {
        for (Config value : Config.values())
            value.setData(this.cfg.get(value.name().toLowerCase()));
    }

    public void reload() {
        this.cfg = YamlConfiguration.loadConfiguration(file);
        this.loadValues();
    }

    public void createFileIfNotExists() {
        if (!this.file.exists()) {
            for (Config value : Config.values()) {
                this.cfg.set(value.name().toLowerCase(), value.getData());
            }
            try {
                this.cfg.save(this.file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for (Config value : Config.values()) {
            if (this.cfg.get(value.name().toLowerCase()) == null) {
                this.cfg.set(value.name().toLowerCase(), value.getData());
            }
            try {
                this.cfg.save(this.file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getCfg() {
        return cfg;
    }
}
