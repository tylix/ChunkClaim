package me.tylix.chunkclaim.commands.home;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import me.tylix.chunkclaim.ChunkClaim;
import me.tylix.chunkclaim.game.home.data.HomeData;
import me.tylix.chunkclaim.message.HelpPage;
import me.tylix.chunkclaim.message.Message;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class HomeCommand implements CommandExecutor, TabCompleter {

    private ChunkClaim instance = ChunkClaim.INSTANCE;

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        final Player player = (Player) commandSender;

        int maxHomes = 0;
        for (PermissionAttachmentInfo effectivePermission : player.getEffectivePermissions())
            if (effectivePermission.getPermission().contains("chunkclaim.homes.")) {
                String[] homes = effectivePermission.getPermission().split("\\.");
                if (homes[2].equals("*"))
                    maxHomes = -1;
                else
                    maxHomes = Integer.parseInt(homes[2]);
            } else
                maxHomes = -1;

        if (strings.length == 1) {
            if (strings[0].equalsIgnoreCase("list")) {
                if (instance.getChunkPlayer(player).getHomeObject().getHomes().isEmpty()) {
                    player.sendMessage(Message.PREFIX.getMessage() + "§cYou currently have no homes!");
                    player.sendMessage(Message.PREFIX.getMessage() + "§7You've §b" + String.valueOf(maxHomes).replace("-1", "infinity") + " Homes §7left§8.");
                    return false;
                }
                player.sendMessage(Message.PREFIX.getMessage() + "§7Your homes §8(§b" + new DecimalFormat().format(instance.getChunkPlayer(player).getHomeObject().getHomes().size()).replace(",", ".") + "§8):");
                for (HomeData homeData : instance.getChunkPlayer(player).getHomeObject().getHomes()) {
                    final TextComponent textComponent = new TextComponent("  §b" + homeData.getName());
                    textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§7Click, to get to home §b\"" + homeData.getName() + "\"").create()));
                    textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/home tp " + homeData.getName()));
                    player.spigot().sendMessage(textComponent);
                }
                player.sendMessage(Message.HOMES_LEFT.getMessage((maxHomes - instance.getChunkPlayer(player).getHomeObject().getHomes().size())));
                return false;
            }
            final String home = strings[0];
            if (!instance.getChunkPlayer(player).getHomeObject().existsHome(home)) {
                player.sendMessage(Message.HOME_DO_NOT_EXISTS.getMessage());
                return false;
            }
            player.teleport(instance.getChunkPlayer(player).getHomeObject().getHomeDataByName(home).getLocation().toBukkitLocation());
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 2);
        } else if (strings.length == 2) {
            final String home = strings[1];
            if (strings[0].equalsIgnoreCase("set")) {
                if (!instance.getChunkPlayer(player).getHomeObject().getHomes().isEmpty() && instance.getChunkPlayer(player).getHomeObject().getHomes().size() == maxHomes) {
                    player.sendMessage(Message.HOME_MAX.getMessage(maxHomes));
                    return false;
                }
                if (instance.getChunkPlayer(player).getHomeObject().existsHome(home)) {
                    player.sendMessage(Message.HOME_ALREADY_EXISTS.getMessage(home));
                    return false;
                }
                instance.getChunkPlayer(player).getHomeObject().addHome(home, player.getLocation());
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
                player.sendMessage(Message.HOME_SET_SUCCESSFULLY.getMessage(home));
            } else if (strings[0].equalsIgnoreCase("delete")) {
                if (!instance.getChunkPlayer(player).getHomeObject().existsHome(home)) {
                    player.sendMessage(Message.HOME_DO_NOT_EXISTS.getMessage());
                    return false;
                }
                instance.getChunkPlayer(player).getHomeObject().removeHome(home);
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
                player.sendMessage(Message.PREFIX.getMessage() + "§7You've §asuccessfully §7removed a home called §b\"" + home + "\"§8.");
            } else if (strings[0].equalsIgnoreCase("teleport") || strings[0].equalsIgnoreCase("tp")) {
                if (!instance.getChunkPlayer(player).getHomeObject().existsHome(home)) {
                    player.sendMessage(Message.HOME_DO_NOT_EXISTS.getMessage());
                    return false;
                }
                player.teleport(instance.getChunkPlayer(player).getHomeObject().getHomeDataByName(home).getLocation().toBukkitLocation());
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 2);
            } else {
                sendHelp(player, 1);
            }
        } else {
            sendHelp(player, 1);
        }

        return false;
    }

    private void sendHelp(final Player player, int page) {
        page = page - 1;

        final List<String> homePages = (List<String>) Message.HOME_HELP_PAGES.getMessageRaw();
        try {
            for (String s : new Gson().fromJson(homePages.get(page), HelpPage.class).getHelp()) {
                s = s.replace("$prefix$", (String) Message.PREFIX.getMessageRaw()).replace("$page$", String.valueOf((page + 1))).replace("$max_pages$", String.valueOf(homePages.size()));
                player.sendMessage(s);
            }
        } catch (IndexOutOfBoundsException e) {
            player.sendMessage(Message.PAGE_NOT_EXISTS.getMessage());
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length == 1) {
            if (strings[0].split("")[0].equalsIgnoreCase("s")) {
                return Lists.newArrayList("set");
            } else if (strings[0].split("")[0].equalsIgnoreCase("l")) {
                return Lists.newArrayList("list");
            }
        } else if (strings.length == 2) {
            if (strings[0].equalsIgnoreCase("teleport") || strings[0].equalsIgnoreCase("tp") || strings[0].equalsIgnoreCase("delete")) {
                final List<String> homes = new ArrayList<>();
                instance.getChunkPlayer(((Player) commandSender).getUniqueId()).getHomeObject().getHomes().forEach(chunkData -> homes.add(chunkData.getName()));
                return homes;
            }
        } else {
            return new ArrayList<>();
        }
        return Lists.newArrayList("set", "delete", "list", "teleport", "tp");
    }
}
