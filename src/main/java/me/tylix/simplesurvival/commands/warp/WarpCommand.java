package me.tylix.simplesurvival.commands.warp;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import me.tylix.simplesurvival.SimpleSurvival;
import me.tylix.simplesurvival.game.chunk.location.ChunkLocation;
import me.tylix.simplesurvival.game.warp.data.WarpData;
import me.tylix.simplesurvival.game.warp.inventory.WarpInventory;
import me.tylix.simplesurvival.message.HelpPage;
import me.tylix.simplesurvival.message.Message;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand implements CommandExecutor, TabCompleter {

    private SimpleSurvival instance = SimpleSurvival.INSTANCE;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        final Player player = (Player) commandSender;

        if (strings.length == 0) {
            new WarpInventory(player.getUniqueId()).setItems(instance.getWarpPage().get(player.getUniqueId())).load();
        } else if (strings.length == 1) {
            if (strings[0].equalsIgnoreCase("list")) {
                new WarpInventory(player.getUniqueId()).setItems(instance.getWarpPage().get(player.getUniqueId())).load();
            } else {
                if (instance.getWarpManager().existsWarp(strings[0])) {
                    player.teleport(instance.getWarpManager().getWarpByName(strings[0]).getLocation().toBukkitLocation());
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                } else {
                    sendHelp(player, 1);
                }
            }
        } else if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("teleport") || strings[0].equalsIgnoreCase("tp")) {
                if (!instance.getWarpManager().existsWarp(strings[1])) {
                    player.sendMessage(Message.WARP_DO_NOT_EXISTS.getMessage());
                    return false;
                }
                player.teleport(instance.getWarpManager().getWarpByName(strings[1]).getLocation().toBukkitLocation());
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            } else if (strings[0].equalsIgnoreCase("set")) {
                if (!player.hasPermission("simplesurvival.admin")) {
                    player.sendMessage(Message.NO_PERMISSIONS.getMessage());
                    return false;
                }
                if (instance.getWarpManager().existsWarp(strings[1])) {
                    player.sendMessage(Message.PREFIX.getMessageRaw() + " §cThere is already a warp with this name!");
                    return false;
                }
                instance.getWarpManager().addWarp(new WarpData(strings[1], new ItemStack(Material.GRASS_BLOCK), new ChunkLocation(player.getLocation())));
                player.sendMessage(Message.PREFIX.getMessageRaw() + " §7You §asuccessfully added §7a Warp called §b\"" + strings[1] + "\"§8!");
            } else if (strings[0].equalsIgnoreCase("delete")) {
                if (!player.hasPermission("simplesurvival.admin")) {
                    player.sendMessage(Message.NO_PERMISSIONS.getMessage());
                    return false;
                }
                if (!instance.getWarpManager().existsWarp(strings[1])) {
                    player.sendMessage(Message.PREFIX.getMessageRaw() + " §cThere is no warp with this name!");
                    return false;
                }
                instance.getWarpManager().removeWarp(instance.getWarpManager().getWarpByName(strings[1]));
                player.sendMessage(Message.PREFIX.getMessageRaw() + " §7You §asuccessfully §cremoved §7a Warp called §b\"" + strings[1] + "\"§8!");
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
        } else {
            sendHelp(player, 1);
        }
        return false;
    }

    private void sendHelp(Player player, int page) {
        if (player.hasPermission("simplesurvival.admin")) {
            player.sendMessage(" ");
            player.sendMessage(Message.PREFIX.getMessageRaw() + " §7Warp Commands");
            player.sendMessage(" ");
            player.sendMessage(" §8» §b/warp set <name> §8× §7Set a Warp§8.");
            player.sendMessage(" §8» §b/warp delete <name> §8× §7Delete a Warp§8.");
            player.sendMessage(" §8» §b/warp list §8× §7List your Warp§8.");
            player.sendMessage(" §8» §b/warp (teleport) <name> §8× §7Teleport you to your Warp§8.");
            player.sendMessage(" ");
        } else {
            page = page - 1;

            final List<String> warpPages = (List<String>) Message.WARP_HELP_PAGES.getMessageRaw();
            try {
                for (String s : new Gson().fromJson(warpPages.get(page), HelpPage.class).getHelp()) {
                    s = s.replace("$prefix$", (String) Message.PREFIX.getMessageRaw()).replace("$page$", String.valueOf((page + 1))).replace("$max_pages$", String.valueOf(warpPages.size()));
                    player.sendMessage(s);
                }
            } catch (IndexOutOfBoundsException e) {
                player.sendMessage(Message.PAGE_NOT_EXISTS.getMessage());
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender.hasPermission("de.verany.admin")) {
            if (strings.length == 1) {
                if (strings[0].split("")[0].equalsIgnoreCase("l")) {
                    return Lists.newArrayList("list");
                } else if (strings[0].split("")[0].equalsIgnoreCase("t")) {
                    return Lists.newArrayList("teleport", "tp");
                } else if (strings[0].split("")[0].equalsIgnoreCase("s")) {
                    return Lists.newArrayList("set");
                } else if (strings[0].split("")[0].equalsIgnoreCase("d")) {
                    return Lists.newArrayList("delete");
                } else {
                    return Lists.newArrayList("list", "teleport", "tp", "set", "delete");
                }
            } else if (strings.length == 2) {
                if (strings[0].equalsIgnoreCase("teleport") || strings[0].equalsIgnoreCase("tp")) {
                    final List<String> warps = new ArrayList<>();
                    instance.getWarpManager().getWarps().forEach(warpData -> warps.add(warpData.getName()));
                    return warps;
                } else {
                    return new ArrayList<>();
                }
            } else {
                return new ArrayList<>();
            }
        } else {
            if (strings.length == 1) {
                if (strings[0].split("")[0].equalsIgnoreCase("l")) {
                    return Lists.newArrayList("list");
                } else if (strings[0].split("")[0].equalsIgnoreCase("t")) {
                    return Lists.newArrayList("teleport", "tp");
                } else {
                    return Lists.newArrayList("list", "teleport", "tp");
                }
            } else if (strings.length == 2) {
                if (strings[0].equalsIgnoreCase("teleport") || strings[0].equalsIgnoreCase("tp")) {
                    final List<String> warps = new ArrayList<>();
                    instance.getWarpManager().getWarps().forEach(warpData -> warps.add(warpData.getName()));
                    return warps;
                } else {
                    return new ArrayList<>();
                }
            } else {
                return new ArrayList<>();
            }
        }
    }
}
