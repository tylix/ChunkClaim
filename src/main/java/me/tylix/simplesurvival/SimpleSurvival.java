package me.tylix.simplesurvival;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import me.tylix.simplesurvival.actionbar.ActionbarManager;
import me.tylix.simplesurvival.commands.admin.ChunkClaimAdminCommand;
import me.tylix.simplesurvival.commands.chunk.ChunkClaimCommand;
import me.tylix.simplesurvival.commands.death.BackCommand;
import me.tylix.simplesurvival.commands.home.HomeCommand;
import me.tylix.simplesurvival.commands.warp.WarpCommand;
import me.tylix.simplesurvival.config.Config;
import me.tylix.simplesurvival.config.ConfigManager;
import me.tylix.simplesurvival.cuboid.Cuboid;
import me.tylix.simplesurvival.game.LocationManager;
import me.tylix.simplesurvival.game.achievements.manager.AchievementLoader;
import me.tylix.simplesurvival.game.chunk.ChunkManager;
import me.tylix.simplesurvival.game.chunk.biome.BiomeLoader;
import me.tylix.simplesurvival.game.deathcause.DeathCauseLoader;
import me.tylix.simplesurvival.game.item.ItemBuilder;
import me.tylix.simplesurvival.game.item.SkullBuilder;
import me.tylix.simplesurvival.game.player.ChunkPlayer;
import me.tylix.simplesurvival.game.recipes.RecipeLoader;
import me.tylix.simplesurvival.game.scoreboard.ScoreboardManager;
import me.tylix.simplesurvival.game.setup.Setup;
import me.tylix.simplesurvival.game.warp.WarpManager;
import me.tylix.simplesurvival.listener.*;
import me.tylix.simplesurvival.message.Message;
import me.tylix.simplesurvival.message.manager.MessageManager;
import me.tylix.simplesurvival.module.manager.ModuleManager;
import me.tylix.simplesurvival.update.Downloader;
import me.tylix.simplesurvival.update.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class SimpleSurvival extends JavaPlugin {

    public static SimpleSurvival INSTANCE;

    private final List<ChunkPlayer> players = new ArrayList<>();
    private final List<UUID> registeredPlayers = new ArrayList<>();
    private final Map<UUID, Setup> setupMap = new HashMap<>();
    private final Map<UUID, Integer> warpPage = new HashMap<>();

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
    private WarpManager warpManager;

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
        warpManager = new WarpManager();

        configManager.createFileIfNotExists();
        configManager.loadValues();
        System.out.println(" ");

        messageManager.createFileIfNotExists();
        messageManager.loadValues();
        System.out.println(" ");

        moduleManager.loadModules();
        System.out.println(" ");

        chunkManager.loadChunks();

        new AchievementLoader().loadAchievements();

        recipeLoader.loadRecipes();
        deathCauseLoader.loadDeathCauses();

        System.out.println(" ");

        UpdateChecker.of(this).resourceId(72110).handleResponse((versionResponse, s) -> {
            switch (versionResponse) {
                case FOUND_NEW:
                    double newVersion = Double.parseDouble(s.split(" ")[0]);
                    double currentVersion = Double.parseDouble(getDescription().getVersion().split(" ")[0]);
                    if (newVersion > currentVersion) {
                        Bukkit.broadcastMessage("[SimpleSurvival] New version of the plugin was found: " + s + " (Current version: " + getDescription().getVersion() + ")");

                        if ((Boolean) Config.AUTO_UPDATE.getData())
                            new Downloader("SimpleSurvival", "https://www.spigotmc.org/resources/simplesurvival-easy-chunk-claiming-and-more-free.72110/download?version=298761", new File("plugins/SimpleSurvival.jar"), false).run();
                    } else
                        Bukkit.broadcastMessage("[SimpleSurvival] Lower version of the plugin was found: " + s + " (Current version: " + getDescription().getVersion() + ")");

                    break;
                case LATEST:
                    Bukkit.broadcastMessage("[SimpleSurvival] You are on the latest version of the plugin.");
                    break;
                case UNAVAILABLE:
                    System.out.println("[SimpleSurvival] Unable to perform an update check.");
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
            this.spawnCuboid = new Cuboid(new LocationManager("SimpleSurvival").getLocation("SpawnArea.1"), new LocationManager("SimpleSurvival").getLocation("SpawnArea.2"));
        } catch (Exception ignore) {
        }
    }

    private void loadPlayers() {
        System.out.println("Loading players...");
        if (new File("plugins/SimpleSurvival/data/users").listFiles() != null)
            for (File file : new File("plugins/SimpleSurvival/data/users").listFiles())
                registeredPlayers.add(UUID.fromString(file.getName().replace(".yml", "")));
        System.out.println("All players (" + registeredPlayers.size() + ") loaded.");
    }

    private void registerCommands() {
        this.getCommand("home").setExecutor(new HomeCommand());
        this.getCommand("home").setTabCompleter(new HomeCommand());
        this.getCommand("warp").setExecutor(new WarpCommand());
        this.getCommand("warp").setTabCompleter(new WarpCommand());
        this.getCommand("back").setExecutor(new BackCommand());
        this.getCommand("chunkclaim").setExecutor(new ChunkClaimCommand());
        this.getCommand("chunkclaim").setTabCompleter(new ChunkClaimCommand());
        this.getCommand("chunkclaimadmin").setExecutor(new ChunkClaimAdminCommand());
    }

    private void registerListener(PluginManager pluginManager) {
        pluginManager.registerEvents(new PlayerDeathEventListener(), this);
        pluginManager.registerEvents(new PlayerClickEventListener(), this);
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

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public Map<UUID, Integer> getWarpPage() {
        return warpPage;
    }


    public static class Items {
        public static final ItemStack MENU = new ItemBuilder(Material.BLAZE_POWDER).setDisplayName(Message.MENU_ITEM_NAME.getMessage()).build();
        public static final SkullBuilder MONEY_SKULL = new SkullBuilder("{display:{Name:\\\"Gold Block\\\"},SkullOwner:{Id:\\\"fdea850d-ae8b-4e10-8b03-6883494ae266\\\",Properties:{textures:[{Value:\\\"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTRiZjg5M2ZjNmRlZmFkMjE4Zjc4MzZlZmVmYmU2MzZmMWMyY2MxYmI2NTBjODJmY2NkOTlmMmMxZWU2In19fQ==\\\"}]}}}", "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNTRiZjg5M2ZjNmRlZmFkMjE4Zjc4MzZlZmVmYmU2MzZmMWMyY2MxYmI2NTBjODJmY2NkOTlmMmMxZWU2In19fQ");
    }
}
