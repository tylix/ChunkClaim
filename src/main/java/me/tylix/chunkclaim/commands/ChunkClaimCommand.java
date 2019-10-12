package me.tylix.chunkclaim.commands;

import me.tylix.chunkclaim.ChunkClaim;
import me.tylix.chunkclaim.game.chunk.ChunkObject;
import me.tylix.chunkclaim.game.setup.Setup;
import me.tylix.chunkclaim.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.Plugin;

import javax.script.ScriptException;
import java.io.File;

public class ChunkClaimCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        final Player player = (Player) commandSender;

        final Chunk chunk = player.getLocation().getChunk();

        switch (strings.length) {
            case 1:
                if (strings[0].equalsIgnoreCase("info")) {
                    player.sendMessage(String.valueOf(ChunkClaim.INSTANCE.getChunkManager().isFree(chunk)));
                } else if (strings[0].equalsIgnoreCase("claim")) {
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
                }
                break;
            default:
                sendHelp(player, 2);
                break;
        }

        if (!player.hasPermission("verany.admin")) {
            sendHelp(player, 2);
            return false;
        }

        switch (strings.length) {
            case 1:
                if (strings[0].equalsIgnoreCase("setup")) {
                    player.sendMessage(Message.PREFIX.getMessage() + " §7Starting Setup..");
                    new Setup(player);
                } else if (strings[0].equalsIgnoreCase("admin")) {
                    sendHelp(player, 1);
                } else if (strings[0].equalsIgnoreCase("user")) {
                    sendHelp(player, 2);
                } else if (strings[0].equalsIgnoreCase("reload") || strings[0].equalsIgnoreCase("rl")) {
                    boolean success = true;
                    try {
                        player.sendMessage(Message.PREFIX.getMessage() + " §7Reloading players...");
                        ChunkClaim.INSTANCE.reloadPlayers();

                        player.sendMessage(Message.PREFIX.getMessage() + " §7Reloading configs...");
                        ChunkClaim.INSTANCE.getConfigManager().reload();
                        ChunkClaim.INSTANCE.getMessageManager().reload();
                    } catch (Exception e) {
                        e.printStackTrace();
                        player.sendMessage(Message.PREFIX.getMessageRaw() + " §cAn error occurred while reloading (check console/logs for more info)! [" + e.toString() + "]");
                        success = false;
                    } finally {
                        if (success)
                            player.sendMessage(Message.PREFIX.getMessage() + " §2Reloading successfully");
                        else
                            player.sendMessage(Message.PREFIX.getMessage() + " §4Reloading failed!");
                    }

                }
                break;
            case 2:

                break;
            default:
                sendHelp(player, 0);
                break;
        }

        return false;
    }

    private void sendHelp(final Player player, final int admin) {
        String message = "usage";
        if (admin == 0) {
            message = " \n" +
                    Message.PREFIX.getMessage() + " §7Useful Commands§8:\n" +
                    " \n" +
                    "§8» §e/cc admin §8- §7Admin Commands\n" +
                    "§8» §e/cc user §8- §7User Commands\n" +
                    " ";
        } else if (admin == 1) {
            message = " \n" +
                    Message.PREFIX.getMessage() + " §7Useful Admin-Commands§8:\n" +
                    " \n" +
                    "§8» §e/cc reload§8- §7Reloading config\n" +
                    "§8» §e/cc setup §8- §7Start the Setup\n" +
                    " ";
        } else if (admin == 2) {

        }
        player.sendMessage(message);
    }
}
