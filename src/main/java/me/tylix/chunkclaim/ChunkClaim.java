package me.tylix.chunkclaim;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import me.tylix.chunkclaim.actionbar.ActionbarManager;
import me.tylix.chunkclaim.commands.BackCommand;
import me.tylix.chunkclaim.commands.ChunkClaimAdminCommand;
import me.tylix.chunkclaim.commands.ChunkClaimCommand;
import me.tylix.chunkclaim.config.ConfigManager;
import me.tylix.chunkclaim.cuboid.Cuboid;
import me.tylix.chunkclaim.game.LocationManager;
import me.tylix.chunkclaim.game.chunk.ChunkManager;
import me.tylix.chunkclaim.game.chunk.biome.BiomeLoader;
import me.tylix.chunkclaim.game.deathcause.DeathCauseLoader;
import me.tylix.chunkclaim.game.item.ItemBuilder;
import me.tylix.chunkclaim.game.player.ChunkPlayer;
import me.tylix.chunkclaim.game.recipes.RecipeLoader;
import me.tylix.chunkclaim.game.scoreboard.ScoreboardManager;
import me.tylix.chunkclaim.game.setup.Setup;
import me.tylix.chunkclaim.listener.*;
import me.tylix.chunkclaim.message.Message;
import me.tylix.chunkclaim.message.manager.MessageManager;
import me.tylix.chunkclaim.module.manager.ModuleManager;
import me.tylix.chunkclaim.update.UpdateChecker;
import net.minecraft.server.v1_14_R1.Blocks;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class ChunkClaim extends JavaPlugin {

    public static ChunkClaim INSTANCE;

    private final List<ChunkPlayer> players = new ArrayList<>();
    private final List<UUID> registeredPlayers = new ArrayList<>();
    private final Map<UUID, Setup> setupMap = new HashMap<>();


    private final Gson gson = new Gson(),
            prettyGson = new GsonBuilder().setPrettyPrinting().create();
    private final JsonParser parser = new JsonParser();

    private ConfigManager configManager;
    private MessageManager messageManager;
    private ChunkManager chunkManager;
    private ScoreboardManager scoreboardManager;
    private ModuleManager moduleManager;
    private RecipeLoader recipeLoader;
    private DeathCauseLoader deathCauseLoader;

    private Cuboid spawnCuboid;

    @Override
    public void onEnable() {
        INSTANCE = this;

        this.init();
    }

    private void init() {
        System.out.println(" ");
        Bukkit.getConsoleSender().sendMessage("§8----------------------");
        System.out.println(" ");
        this.loadPlayers();

        configManager = new ConfigManager();
        messageManager = new MessageManager();
        chunkManager = new ChunkManager();
        scoreboardManager = new ScoreboardManager();
        moduleManager = new ModuleManager();
        recipeLoader = new RecipeLoader();
        deathCauseLoader = new DeathCauseLoader();

        configManager.createFileIfNotExists();
        configManager.loadValues();
        System.out.println(" ");

        messageManager.createFileIfNotExists();
        messageManager.loadValues();
        System.out.println(" ");

        moduleManager.loadModules();
        System.out.println(" ");

        chunkManager.loadChunks();

        recipeLoader.loadRecipes();
        deathCauseLoader.loadDeathCauses();

        System.out.println(" ");

        UpdateChecker.of(this).resourceId(72110).handleResponse((versionResponse, s) -> {
            switch (versionResponse) {
                case FOUND_NEW:
                    Bukkit.broadcastMessage("New version of the plugin was found: " + s);
                    break;
                case LATEST:
                    Bukkit.broadcastMessage("You are on the latest version of the plugin.");
                    break;
                case UNAVAILABLE:
                    System.out.println("Unable to perform an update check.");
                    break;
            }
        }).check();

        System.out.println(" ");

        new BiomeLoader();

        Bukkit.getConsoleSender().sendMessage("§8----------------------");
        System.out.println(" ");

        this.registerListener(Bukkit.getPluginManager());
        this.registerCommands();

        new Items();

        // startChecker();

        loadSpawnArea();
    }

    private void startChecker() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            /*for (Entity world : Bukkit.getWorld("world").getEntities()) {
                if (world instanceof Arrow) {
                    if (world.getFireTicks() != 0) {
                        int random = new Random().nextInt(4);
                        int percentage = new Random().nextInt(100);
                        if (percentage <= 30) {
                            world.getLocation().add(random, random, random).getBlock().setType(Material.FIRE);
                            world.remove();
                        }
                    }
                }
            }*/
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                boolean inside = false;
                new ActionbarManager(onlinePlayer.getUniqueId()).sendActionbar(inside ? "inside" : "outside");
            }
        }, 5, 5);
    }

    public void loadSpawnArea() {
        try {
            this.spawnCuboid = new Cuboid(new LocationManager("ChunkClaim").getLocation("SpawnArea.1"), new LocationManager("ChunkClaim").getLocation("SpawnArea.2"));
        } catch (Exception ignore) {
        }
    }

    private void loadPlayers() {
        System.out.println("Loading players...");
        if (new File("plugins/ChunkClaim/data/users").listFiles() != null)
            for (File file : new File("plugins/ChunkClaim/data/users").listFiles())
                registeredPlayers.add(UUID.fromString(file.getName().replace(".yml", "")));
        System.out.println("All players (" + registeredPlayers.size() + ") loaded.");
    }

    private void registerCommands() {
        this.getCommand("back").setExecutor(new BackCommand());
        this.getCommand("chunkclaim").setExecutor(new ChunkClaimCommand());
        this.getCommand("chunkclaim").setTabCompleter(new ChunkClaimCommand());
        this.getCommand("chunkclaimadmin").setExecutor(new ChunkClaimAdminCommand());
    }

    private void registerListener(PluginManager pluginManager) {
        pluginManager.registerEvents(new PlayerDeathEventListener(), this);
        pluginManager.registerEvents(new PlayerChatEventListener(), this);
        pluginManager.registerEvents(new PlayerJoinEventListener(), this);
        pluginManager.registerEvents(new PlayerQuitEventListener(), this);
        pluginManager.registerEvents(new PlayerMoveEventListener(), this);
        pluginManager.registerEvents(new ProtectionListener(), this);
    }

    public void reloadPlugin(Player player) {
        if (player != null)
            player.sendMessage(Message.PREFIX.getMessage() + " §7Reloading players...");
        reloadPlayers();

        if (player != null)
            player.sendMessage(Message.PREFIX.getMessage() + " §7Reloading configs...");
        messageManager.reload();
        configManager.reload();
        deathCauseLoader.reload();
        recipeLoader.reload();
    }

    @Override
    public void onDisable() {

    }

    public ItemStack getPlaceholder() {
        return new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
    }

    public Inventory fillInventory(Inventory inventory, ItemStack placeHolder) {
        for (int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, placeHolder);
        return inventory;
    }

    public Inventory fillCycle(Inventory inventory, ItemStack placeHolder) {
        for (int i = 0; i < 9; ++i)
            if (inventory.getItem(i) == null)
                inventory.setItem(i, placeHolder);
        for (int i = inventory.getSize() - 9; i < inventory.getSize(); ++i)
            if (inventory.getItem(i) == null)
                inventory.setItem(i, placeHolder);
        int j = 0;
        for (int i = 0; i < inventory.getSize() / 9; ++i) {
            if (i != 0 && i != inventory.getSize() - 9)
                inventory.setItem(j, placeHolder);
            j += 9;
        }
        int k = 8;
        for (int i = 0; i < inventory.getSize() / 9; ++i) {
            if (i != 0 && i != inventory.getSize() - 9)
                inventory.setItem(k, placeHolder);
            k += 9;
        }
        return inventory;
    }

    public Inventory fillInventory(Inventory inventory, ItemStack placeHolder, int... slots) {
        for (int slot : slots)
            inventory.setItem(slot, placeHolder);
        return inventory;
    }

    private void reloadPlayers() {
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

    public Map<UUID, Setup> getSetupMap() {
        return setupMap;
    }

    public Cuboid getSpawnCuboid() {
        return spawnCuboid;
    }

    public RecipeLoader getRecipeLoader() {
        return recipeLoader;
    }

    public DeathCauseLoader getDeathCauseLoader() {
        return deathCauseLoader;
    }


    public static class Items {
        public static final ItemStack MENU = new ItemBuilder(Material.BLAZE_POWDER).setDisplayName(Message.MENU_ITEM_NAME.getMessage()).build();
    }
}
