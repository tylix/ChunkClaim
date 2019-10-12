package me.tylix.chunkclaim;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import me.tylix.chunkclaim.commands.ChunkClaimCommand;
import me.tylix.chunkclaim.config.ConfigManager;
import me.tylix.chunkclaim.game.chunk.ChunkManager;
import me.tylix.chunkclaim.game.player.ChunkPlayer;
import me.tylix.chunkclaim.game.scoreboard.ScoreboardManager;
import me.tylix.chunkclaim.listener.PlayerJoinEventListener;
import me.tylix.chunkclaim.listener.PlayerQuitEventListener;
import me.tylix.chunkclaim.listener.ProtectionListener;
import me.tylix.chunkclaim.message.manager.MessageManager;
import me.tylix.chunkclaim.module.manager.ModuleManager;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class ChunkClaim extends JavaPlugin {

    public static ChunkClaim INSTANCE;

    private final List<ChunkPlayer> players = new ArrayList<>();
    private final List<UUID> registeredPlayers = new ArrayList<>();

    private final Gson gson = new Gson(),
            prettyGson = new GsonBuilder().setPrettyPrinting().create();
    private final JsonParser parser = new JsonParser();

    private ConfigManager configManager;
    private MessageManager messageManager;
    private ChunkManager chunkManager;
    private ScoreboardManager scoreboardManager;
    private ModuleManager moduleManager;

    @Override
    public void onEnable() {
        INSTANCE = this;

        this.init();
    }

    private void init() {
        System.out.println(" ");
        this.loadPlayers();

        configManager = new ConfigManager();
        messageManager = new MessageManager();
        chunkManager = new ChunkManager();
        scoreboardManager = new ScoreboardManager();
        moduleManager = new ModuleManager();

        configManager.createFileIfNotExists();
        configManager.loadValues();
        System.out.println(" ");

        messageManager.createFileIfNotExists();
        messageManager.loadValues();
        System.out.println(" ");

        moduleManager.loadModules();
        System.out.println(" ");

        chunkManager.loadChunks();
        System.out.println(" ");

        this.registerListener(Bukkit.getPluginManager());
        this.registerCommands();
    }

    private void loadPlayers() {
        System.out.println("Loading Players...");
        if (new File("plugins//ChunkClaim//users").listFiles() != null)
            for (File file : new File("plugins//ChunkClaim//users").listFiles())
                registeredPlayers.add(UUID.fromString(file.getName().replace(".yml", "")));
        System.out.println("All Players (" + registeredPlayers.size() + ") loaded.");
    }

    private void registerCommands() {
        this.getCommand("chunkclaim").setExecutor(new ChunkClaimCommand());
    }

    private void registerListener(PluginManager pluginManager) {
        pluginManager.registerEvents(new PlayerJoinEventListener(), this);
        pluginManager.registerEvents(new PlayerQuitEventListener(), this);
        pluginManager.registerEvents(new ProtectionListener(), this);
    }


    @Override
    public void onDisable() {

    }

    public void reloadPlayers() {
        registeredPlayers.clear();
        loadPlayers();

        players.clear();
        for (Player onlinePlayer : Bukkit.getOnlinePlayers())
            players.add(new ChunkPlayer(onlinePlayer.getUniqueId()));
    }

    public ChunkPlayer getChunkPlayer(final UUID uuid) {
        for (ChunkPlayer player : players)
            if (player.getUuid().equals(uuid))
                return player;
        return null;
    }

    public ChunkPlayer getChunkPlayer(final Player player) {
        for (ChunkPlayer chunkPlayer : players)
            if (chunkPlayer.getUuid().equals(player.getUniqueId()))
                return chunkPlayer;
        return null;
    }

    public String generateKey(int letters) {
        final StringBuilder builder = new StringBuilder();
        final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        for (int i = 0; i < letters; i++)
            builder.append(alphabet.charAt(new Random().nextInt(alphabet.length())));
        return builder.toString();
    }

    public List<ChunkPlayer> getPlayers() {
        return players;
    }

    public Gson getGson() {
        return gson;
    }

    public Gson getPrettyGson() {
        return prettyGson;
    }

    public JsonParser getParser() {
        return parser;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public List<UUID> getRegisteredPlayers() {
        return registeredPlayers;
    }

    public ScoreboardManager getScoreboardManager() {
        return scoreboardManager;
    }

    public ChunkManager getChunkManager() {
        return chunkManager;
    }

    public ModuleManager getModuleManager() {
        return moduleManager;
    }
}
