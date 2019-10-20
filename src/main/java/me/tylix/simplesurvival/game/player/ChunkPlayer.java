package me.tylix.simplesurvival.game.player;

import me.tylix.simplesurvival.SimpleSurvival;
import me.tylix.simplesurvival.config.Config;
import me.tylix.simplesurvival.config.JsonConfig;
import me.tylix.simplesurvival.game.home.HomeObject;
import me.tylix.simplesurvival.game.player.data.PlayerData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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

    private HomeObject homeObject;

    private int chunkSize = 0;

    private PlayerData playerData;

    public ChunkPlayer(UUID uuid) {
        this.uuid = uuid;
        this.file = new File("plugins/SimpleSurvival/data/users/" + uuid.toString() + ".yml");
        this.cfg = YamlConfiguration.loadConfiguration(file);

        if (exists()) {
            final JsonConfig jsonConfig = new JsonConfig(this.file);
            playerData = jsonConfig.get("userData", PlayerData.class);
            homeObject = new HomeObject(uuid);

            if (playerData.getHomes() == null) {
                playerData.setHomes(new ArrayList<>());
                updatePlayerData(playerData);
            }

            setChunkSize(playerData.getChunks().size());
        }
    }

    private boolean exists() {
        return file.exists();
    }

    public boolean createIfNotExists() {
        if (exists()) return true;
        new File("plugins/SimpleSurvival/data").mkdirs();
        new File("plugins/SimpleSurvival/data/users").mkdirs();
        final PlayerData playerData = new PlayerData(0, 0, 100, 500, 8, Config.MESSAGES.getData().toString(), new ArrayList<>(), new ArrayList<>());

        final JsonConfig jsonConfig = new JsonConfig(this.file);
        jsonConfig.set("userData", playerData);
        jsonConfig.saveConfig();

        SimpleSurvival.INSTANCE.getRegisteredPlayers().add(uuid);

        this.playerData = jsonConfig.get("userData", PlayerData.class);
        this.homeObject = new HomeObject(uuid);

        setChunkSize(playerData.getChunks().size());

        return false;
    }

    public void setItem() {
        Player player = Bukkit.getPlayer(uuid);

        if (player.getInventory().getItem(playerData.getMenuSlot()) != null && !player.getInventory().getItem(playerData.getMenuSlot()).getType().equals(SimpleSurvival.Items.MENU.getType())) {
            ItemStack itemStack = player.getInventory().getItem(playerData.getMenuSlot());
            player.getInventory().setItem(playerData.getMenuSlot(), null);
            player.getWorld().dropItemNaturally(player.getLocation(), itemStack);
        }

        player.getInventory().setItem(playerData.getMenuSlot(), SimpleSurvival.Items.MENU);
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
            SimpleSurvival.INSTANCE.getScoreboardManager().updateScoreboard(Bukkit.getPlayer(uuid));
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

    public HomeObject getHomeObject() {
        return this.homeObject;
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
