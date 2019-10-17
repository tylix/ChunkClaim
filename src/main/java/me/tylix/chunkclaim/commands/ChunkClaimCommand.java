package me.tylix.chunkclaim.commands;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import me.tylix.chunkclaim.ChunkClaim;
import me.tylix.chunkclaim.config.Config;
import me.tylix.chunkclaim.game.LocationManager;
import me.tylix.chunkclaim.game.chunk.ChunkObject;
import me.tylix.chunkclaim.game.chunk.biome.BiomeLoader;
import me.tylix.chunkclaim.game.chunk.data.ChunkData;
import me.tylix.chunkclaim.game.chunk.display.ChunkDisplayTask;
import me.tylix.chunkclaim.game.setup.Setup;
import me.tylix.chunkclaim.message.HelpPage;
import me.tylix.chunkclaim.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import javax.script.ScriptException;
import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class ChunkClaimCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        final Player player = (Player) commandSender;

        final Chunk chunk = player.getLocation().getChunk();

        switch (strings.length) {
            case 1:
                if (strings[0].equalsIgnoreCase("info")) {
                    if (!player.getWorld().getName().equals(Config.CHUNK_WORLD.getData())) {
                        player.sendMessage(Message.CHUNK_NOT_IN_WORLD.getMessage());
                        return false;
                    }
                    if (ChunkClaim.INSTANCE.getChunkManager().isInSpawnArea(player.getLocation().getChunk()) || player.getLocation().distance(new LocationManager("ChunkClaim").getLocation("Spawn")) < (int) Config.SPAWN_PROTECTION_RADIUS.getData()) {
                        player.sendMessage(Message.CHUNK_IN_SPAWN.getMessage());
                        return false;
                    }

                    new ChunkDisplayTask().display(player);

                    final Location location = player.getLocation();
                    location.setPitch(-45);
                    player.teleport(location);

                    if (ChunkClaim.INSTANCE.getChunkManager().getOwner(player.getLocation().getChunk()) == null) {
                        int must = 0;
                        try {
                            must = ChunkClaim.INSTANCE.getChunkPlayer(player).getNextChunkPrice();
                        } catch (ScriptException e) {
                            e.printStackTrace();
                        }

                        player.sendMessage("§7This Chunk is §afree§8.");
                        player.sendMessage("§7Enter §b/chunk claim §7to claim this Chunk for §b" + new DecimalFormat().format(must).replace(",", ".") + " Vens§8.");
                        if (ChunkClaim.INSTANCE.getChunkPlayer(player).getPlayerData().getMoney() < must)
                            player.sendMessage(Message.NOT_ENOUGH_MONEY.getMessage(must - ChunkClaim.INSTANCE.getChunkPlayer(player).getPlayerData().getMoney()));
                        return false;
                    }
                    return false;
                } else if (strings[0].equalsIgnoreCase("claim")) {
                    if (!player.getWorld().getName().equals(Config.CHUNK_WORLD.getData())) {
                        player.sendMessage(Message.CHUNK_NOT_IN_WORLD.getMessage());
                        return false;
                    }
                    if (ChunkClaim.INSTANCE.getChunkManager().isInSpawnArea(player.getLocation().getChunk()) || player.getLocation().distance(new LocationManager("ChunkClaim").getLocation("Spawn")) < (int) Config.SPAWN_PROTECTION_RADIUS.getData()) {
                        player.sendMessage(Message.CHUNK_IN_SPAWN.getMessage());
                        return false;
                    }
                    if (ChunkClaim.INSTANCE.getChunkManager().getOwner(chunk) != null && ChunkClaim.INSTANCE.getChunkManager().getOwner(chunk).equals(player.getUniqueId())) {
                        player.sendMessage(Message.CHUNK_ALREADY_YOURS.getMessage());
                        return false;
                    }
                    if (!ChunkClaim.INSTANCE.getChunkManager().isFree(chunk)) {
                        player.sendMessage(Message.CHUNK_ALREADY_ClAIMED.getMessage());
                        return false;
                    }
                    try {
                        new ChunkObject(player.getUniqueId()).claimChunk(player.getLocation().getChunk());
                    } catch (ScriptException e) {
                        e.printStackTrace();
                    }
                    return false;
                } else if (strings[0].equalsIgnoreCase("map")) {
                    player.sendMessage(" ");
                    player.sendMessage(ChunkClaim.INSTANCE.getChunkManager().getChunkMap(player.getLocation()));
                    player.sendMessage(" ");

                    player.sendMessage(new Gson().toJson(BiomeLoader.BIOMES));
                    return false;
                } else {
                    sendHelp(player, 1);
                    return false;
                }
            case 2:
                if (strings[0].equalsIgnoreCase("teleport") || strings[0].equalsIgnoreCase("tp")) {
                    final String chunkId = strings[1];
                    final List<String> chunkIds = new ArrayList<>();
                    ChunkClaim.INSTANCE.getChunkPlayer(player).getPlayerData().getChunks().forEach(chunkData -> chunkIds.add(chunkData.getId()));
                    if (!chunkIds.contains(strings[1])) {
                        player.sendMessage(Message.CHUNK_NOT_FOUND_ID.getMessage());
                        return false;
                    }
                    final ChunkData chunkData = ChunkClaim.INSTANCE.getChunkManager().getChunkDataById(chunkId);
                    final Location location = ChunkClaim.INSTANCE.getChunkManager().getCenter(chunkData);
                    location.setYaw(player.getLocation().getYaw());
                    location.setPitch(player.getLocation().getPitch());
                    player.teleport(location);
                    return false;
                } else if (strings[0].equalsIgnoreCase("help")) {
                    try {
                        final int page = Integer.parseInt(strings[1]);
                        sendHelp(player, page);
                    } catch (NumberFormatException e) {
                        player.sendMessage(Message.NOT_A_NUMBER.getMessage());
                    }
                } else {
                    sendHelp(player, 1);
                }
                break;
            default:
                sendHelp(player, 1);
                return false;
        }

        return false;
    }

    public static void sendHelp(final Player player, int page) {
        page = page - 1;

        final List<String> helpPages = (List<String>) Message.HELP_PAGES.getMessageRaw();
        try {
            for (String s : new Gson().fromJson(helpPages.get(page), HelpPage.class).getHelp())
                player.sendMessage(s);
        } catch (IndexOutOfBoundsException e) {
            player.sendMessage(Message.PAGE_NOT_EXISTS.getMessage());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        return null;
    }
}
