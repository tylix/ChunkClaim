package me.tylix.simplesurvival.message;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import me.tylix.simplesurvival.SimpleSurvival;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public enum Message {

    PREFIX("§aSimple§2Survival §8›", ""),
    NO_PERMISSIONS("$prefix$ §cYou don't have enough permissions!", ""),
    PLAYER_JOIN("$prefix$ §e$player$ §7has joined the server. §8[§b$online$§7/§b$max$§8]", ""),
    PLAYER_QUIT("$prefix$ §e$player$ §7has left the server. §8[§b$online$§7/§b$max$§8]", ""),
    PLAYER_DEATH("$prefix$ §e{0} §7{1}", ""),
    PLAYER_KILLED("$prefix$ §e{0} was killed by §e{1}§7.", ""),
    TELEPORT_TO_DEATH_POINT("$prefix$ §b§oteleport to death location (/back)", ""),
    TELEPORT_TO_DEATH_HOVER("§7Click, to teleport to death location", ""),
    NOT_ENOUGH_MONEY("$prefix$ §cYou are missing {0} Money!", ""),
    SUCCESSFULLY_CLAIMED_CHUNK("$prefix$ §7You §asuccessfully §7claimed a chunk for §e{0} §7Money!", ""),
    CHUNK_ALREADY_ClAIMED("$prefix$ §cThis chunk is already claimed!", ""),
    CHUNK_ALREADY_YOURS("$prefix$ §cThis chunk is already your chunk!", ""),
    CHUNK_NOT_FOUND_ID("$prefix$ §cYou don't have a chunk with this id!", ""),
    CHUNK_IN_SPAWN("$prefix$ §cYou can't claim a chunk here!", ""),
    CHUNK_NOT_IN_WORLD("$prefix$ §cYou can't claim a chunk in this word!", ""),
    CHUNK_INFO_CLAIMED("", ""),
    CHUNK_INFO_FREE("", ""),
    HOME_SET_SUCCESSFULLY("$prefix$ §7You §asuccessfully §7set a home called §b{0}§7!", ""),
    HOME_REMOVE_SUCCESSFULLY("§7You §asuccessfully §7removed a home called §b{0}§8.", ""),
    HOME_MAX("$prefix$ §cYou can only set {0} homes!", ""),
    HOME_ALREADY_EXISTS("$prefix$ §cYou already own a home with this name!", ""),
    HOME_DO_NOT_EXISTS("$prefix$ §cYou don't have a home with this name!", ""),
    HOMES_LEFT("$prefix$ §7You have §a{0} §7home(s) left.", ""),
    HOME_LIST_FORMAT("   §8- §e{0}", ""),
    HOME_CLICK_TO_TELEPORT("§7Click to teleport to home §a{0}", ""),
    NO_HOMES("$prefix$ §cYou currently have no homes!", ""),
    YOUR_HOMES("$prefix$ §7Your homes §8(§a{0}§8):", ""),
    WARP_DO_NOT_EXISTS("$prefix$ §cThere is no warp with this name!", ""),
    WARP_INVENTORY_NAME("§7All Warps §8(§3$warp_size$§8): §8[§b$page$§8]", ""),
    WARP_ITEM_NAME("§b$warp_name$", ""),
    WARP_ITEM_LORE(Lists.newArrayList(" ", "§7Click to teleport"), new ArrayList<>()),
    MENU_ITEM_NAME("§2SimpleSurvival Menu", ""),
    MENU_INVENTORY_NAME("§2SimpleSurvival Menu", ""),
    SCOREBOARD_ENABLED(true, ""),
    DISPLAY_NAME("SimpleSurvival", ""),
    SCOREBOARD(Lists.newArrayList(" ", "§7Money§8:", "§6$money$", " ", "§7Chunks§8:", "§a$chunk_size$", " ", "§7Chunk by§8:", "§b$chunk_owner$", " "), ""),
    UNKNOWN_CHUNK_OWNER("§bNone", ""),
    PAGE_NOT_EXISTS("$prefix$ §cThis page doesn't exists!", ""),
    NOT_A_NUMBER("$prefix$ §cPlease enter a number!", ""),
    HELP_PAGES(Lists.newArrayList(SimpleSurvival.INSTANCE.getPrettyGson().toJson(new Gson().toJsonTree(new HelpPage(Lists.newArrayList(
            " "
            , "$prefix$ §7Simple Survival Commands §8[§e$page$§7/§e$max_pages$§8]",
            " ",
            "§8» §b/cc help [page] §8- §7Show the help pages",
            "§8» §b/cc info §8- §7Get information about the chunk you're standing in",
            "§8» §b/cc claim §8- §7Claim the Chunk",
            "§8» §b/cc unclaim §8- §7Unclaim the Chunk",
            " "))))
            , SimpleSurvival.INSTANCE.getPrettyGson().toJson(new Gson().toJsonTree(new HelpPage(Lists.newArrayList(
                    " ",
                    "$prefix$ §7Simple Survival Commands §8[§e$page$§7/§e$max_pages$§8]",
                    " ",
                    "§8» §b/cc random §8- §7Teleport you to a random free chunk",
                    "§8» §b/cc list §8- §7List your claimed Chunks",
                    "§8» §b/cc teleport [chunkId] §8- §7Teleport you to a chunk from you",
                    " "
            ))))), ""),
    HOME_HELP_PAGES(Lists.newArrayList(SimpleSurvival.INSTANCE.getPrettyGson().toJson(new Gson().toJsonTree(new HelpPage(Lists.newArrayList(
            " "
            , "$prefix$ §7Home Commands §8[§e$page$§7/§e$max_pages$§8]",
            "§8» §b/home set [name] §8- §7Set a home",
            "§8» §b/home delete [name] §8- §7Delete a home",
            "§8» §b/home list §8- §7List your homes",
            "§8» §b/home (teleport) [name] §8- §7Teleport you to a home",
            " "
    ))))), ""),
    WARP_HELP_PAGES(Lists.newArrayList(SimpleSurvival.INSTANCE.getPrettyGson().toJson(new Gson().toJsonTree(new HelpPage(Lists.newArrayList(
            " "
            , "$prefix$ §7Warp Commands §8[§e$page$§7/§e$max_pages$§8]",
            "§8» §b/warps §8- §7Open warp inventory",
            "§8» §b/warp list §8- §7List all warps",
            "§8» §b/warp (teleport) [name] §8- §7Teleport you to a warp",
            " "
    ))))), "");

    private Object defaultMessage;
    private Object message;

    Message(Object defaultMessage, Object message) {
        this.defaultMessage = defaultMessage;
        this.message = message;
    }

    public Object getMessageRaw() {
        return message;
    }

    public Object getDefaultMessage() {
        return defaultMessage;
    }

    public String getMessage() {
        return ((String) message).replace("&", "§").replace("$prefix$", (String) PREFIX.getMessageRaw()).replace("$online$", String.valueOf(SimpleSurvival.INSTANCE.getPlayers().size())).replace("$max$", String.valueOf(Bukkit.getMaxPlayers()));
    }

    public String getMessage(final Player player) {
        return ((String) message).replace("&", "§").replace("$prefix$", PREFIX.getMessage()).replace("$player$", player.getName()).replace("$online$", String.valueOf(SimpleSurvival.INSTANCE.getPlayers().size())).replace("$max$", String.valueOf(Bukkit.getMaxPlayers()));
    }

    public String getMessage(Object... arguments) {
        return MessageFormat.format((String) message, arguments).replace("$prefix$", (String) PREFIX.getMessageRaw());
    }

    public void setMessage(final Object message) {
        this.message = message;
    }

}
