package me.tylix.chunkclaim.message;

import com.google.common.collect.Lists;
import me.tylix.chunkclaim.ChunkClaim;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.MessageFormat;
import java.util.ArrayList;

public enum Message {

    PREFIX("§aChunkClaim §8›"),
    NO_PERMISSIONS("$prefix$ §cYou don't have enough permissions!"),
    PLAYER_JOIN("$prefix$ §e$player$ §7has joined the server. §8[§b$online$§7/§b$max$§8]"),
    PLAYER_QUIT("$prefix$ §e$player$ §7has left the server. §8[§b$online$§7/§b$max$§8]"),
    NOT_ENOUGH_MONEY("$prefix$ §cYou are missing {0} Money!"),
    SUCCESSFULLY_CLAIMED_CHUNK("$prefix$ §7You §asuccessfully §7claimed a chunk for §e{0} §7Money!"),
    CHUNK_ALREADY_ClAIMED("$prefix$ §cThis chunk is already claimed!"),
    CHUNK_ALREADY_YOURS("$prefix$ §cThis chunk is already your chunk!"),
    CHUNK_NOT_FOUND_ID("$prefix$ §cYou don't have a chunk with this id!"),
    SCOREBOARD_ENABLED(true),
    DISPLAY_NAME("ChunkClaim"),
    SCOREBOARD(Lists.newArrayList(" ", "§7Money§8:", "§6$money$", " ", "§7Chunks§8:", "§a$chunk_size$", " ", "§7Chunk by§8:", "§b$chunk_owner$", " ")),
    UNKNOWN_CHUNK_OWNER("§bNone");

    private Object message;

    Message(Object message) {
        this.message = message;
    }

    public Object getMessageRaw() {
        return message;
    }

    public String getMessage() {
        return ((String) message).replace("&", "§").replace("$prefix$", (String) PREFIX.getMessageRaw()).replace("$online$", String.valueOf(ChunkClaim.INSTANCE.getPlayers().size())).replace("$max$", String.valueOf(Bukkit.getMaxPlayers()));
    }

    public String getMessage(final Player player) {
        return ((String) message).replace("&", "§").replace("$prefix$", PREFIX.getMessage()).replace("$player$", player.getName()).replace("$online$", String.valueOf(ChunkClaim.INSTANCE.getPlayers().size())).replace("$max$", String.valueOf(Bukkit.getMaxPlayers()));
    }

    public String getMessage(Object... arguments) {
        return MessageFormat.format((String) message, arguments).replace("$prefix$", (String) PREFIX.getMessageRaw());
    }

    public void setMessage(final Object message) {
        this.message = message;
    }
}
