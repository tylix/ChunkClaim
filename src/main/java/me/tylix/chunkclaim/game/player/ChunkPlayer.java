package me.tylix.chunkclaim.game.player;

import me.tylix.chunkclaim.ChunkClaim;
import me.tylix.chunkclaim.config.Config;
import me.tylix.chunkclaim.config.JsonConfig;
import me.tylix.chunkclaim.game.player.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class ChunkPlayer {

    private final UUID uuid;
    private final File file;
    private final YamlConfiguration cfg;

    private int chunkSize = 0;

    private PlayerData playerData;

    public ChunkPlayer(UUID uuid) {
        this.uuid = uuid;
        this.file = new File("plugins//ChunkClaim//users//" + uuid.toString() + ".yml");
        this.cfg = YamlConfiguration.loadConfiguration(file);

        if (!exists())
            create();

        final JsonConfig jsonConfig = new JsonConfig(this.file);
        playerData = jsonConfig.get("userData", PlayerData.class);

        setChunkSize(playerData.getChunks().size());
    }

    private boolean exists() {
        return file.exists();
    }

    private void create() {
        new File("plugins//ChunkClaim").mkdirs();
        new File("plugins//ChunkClaim//users").mkdirs();
        final PlayerData playerData = new PlayerData(0, 0, 100, 100, new ArrayList<>());

        final JsonConfig jsonConfig = new JsonConfig(this.file);
        jsonConfig.set("userData", playerData);
        jsonConfig.saveConfig();

        ChunkClaim.INSTANCE.getRegisteredPlayers().add(uuid);
    }

    public PlayerData getPlayerData() {
        return playerData;
    }

    public void updatePlayerData(PlayerData playerData) {
        final JsonConfig jsonConfig = new JsonConfig(this.file);
        jsonConfig.set("userData", playerData);
        jsonConfig.saveConfig();
        this.playerData = playerData;

        if (Bukkit.getPlayer(uuid) != null)
            ChunkClaim.INSTANCE.getScoreboardManager().updateScoreboard(Bukkit.getPlayer(uuid));
    }

    public void removeMoney(int value) {
        PlayerData playerData = getPlayerData();
        playerData.setMoney((playerData.getMoney() - value));
        updatePlayerData(playerData);
    }

    public void addMoney(int value) {
        PlayerData playerData = getPlayerData();
        playerData.setMoney(playerData.getMoney() + value);
        updatePlayerData(playerData);
    }

    public int getNextChunkPrice() throws ScriptException {
        int price = (int) Config.CHUNK_PRICE.getData();
        if (getPlayerData().getChunks().size() > 0) {
            ScriptEngineManager engineManager = new ScriptEngineManager();
            ScriptEngine engine = engineManager.getEngineByName("JavaScript");
            String calculation = (String) Config.CHUNK_CALCULATION.getData();
            calculation = calculation.replace("$chunk_price$", String.valueOf(price)).replace("$chunk_size$", String.valueOf(getPlayerData().getChunks().size()));
            price = (int) engine.eval(calculation);
        }
        return price;
    }

    public File getFile() {
        return file;
    }

    public YamlConfiguration getCfg() {
        return cfg;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getChunkSize() {
        return chunkSize;
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }
}
