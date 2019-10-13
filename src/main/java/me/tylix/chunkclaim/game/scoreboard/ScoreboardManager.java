package me.tylix.chunkclaim.game.scoreboard;

import me.tylix.chunkclaim.ChunkClaim;
import me.tylix.chunkclaim.game.player.data.PlayerData;
import me.tylix.chunkclaim.message.Message;
import net.md_5.bungee.chat.SelectorComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardManager {

    private final ConcurrentHashMap<Scoreboard, Player> scoreboardUpdater = new ConcurrentHashMap<>();

    public void setScoreboard(Player player) {
        if (!(Boolean) Message.SCOREBOARD_ENABLED.getMessageRaw()) return;
        final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        final Objective objective = scoreboard.registerNewObjective("aaa", "bbb");

        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName(Message.DISPLAY_NAME.getMessage());

        final List<String> scores = ((ArrayList<String>) Message.SCOREBOARD.getMessageRaw());

        final PlayerData playerData = ChunkClaim.INSTANCE.getChunkPlayer(player).getPlayerData();

        int score = scores.size();
        for (int i = 0; i < scores.size(); i++) {
            final Team team = scoreboard.registerNewTeam("x" + score);
            String prefix = scores.get(i);

            String owner;
            if (ChunkClaim.INSTANCE.getChunkManager().getOwner(player.getLocation().getChunk()) == null)
                owner = Message.UNKNOWN_CHUNK_OWNER.getMessage();
            else {
                owner = Bukkit.getOfflinePlayer(ChunkClaim.INSTANCE.getChunkManager().getOwner(player.getLocation().getChunk())).getName();
                /*if (prefix.contains("$chunk_owner$"))
                    if ((prefix.length() + owner.length()) >= 16)
                        owner = owner.substring(0, 16);*/
            }

            prefix = prefix.replace("$money$", new DecimalFormat().format(playerData.getMoney()).replace(",", ".")).replace("$chunk_size$", String.valueOf(playerData.getChunks().size())).replace("$chunk_owner$", owner);
            team.setPrefix(prefix);
            team.addEntry(ChatColor.values()[i].toString());
            objective.getScore(ChatColor.values()[i].toString()).setScore(score);
            score--;
        }

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard(Player target) {
        if (target == null)
            for (Scoreboard scoreboard : scoreboardUpdater.keySet()) {
                final Player player = scoreboardUpdater.get(scoreboard);

                scoreboard.getObjective(DisplaySlot.SIDEBAR).setDisplayName(Message.DISPLAY_NAME.getMessage());

                final List<String> scores = ((ArrayList<String>) Message.SCOREBOARD.getMessageRaw());

                final PlayerData playerData = ChunkClaim.INSTANCE.getChunkPlayer(player).getPlayerData();

                int score = scores.size();
                for (String s : scores) {
                    final Team team = scoreboard.getTeam("x" + score);
                    String prefix = s;
                    String owner;
                    if (ChunkClaim.INSTANCE.getChunkManager().getOwner(player.getLocation().getChunk()) == null)
                        owner = Message.UNKNOWN_CHUNK_OWNER.getMessage();
                    else {
                        owner = Bukkit.getOfflinePlayer(ChunkClaim.INSTANCE.getChunkManager().getOwner(player.getLocation().getChunk())).getName();
                      /*if (prefix.contains("$chunk_owner$"))
                          if ((prefix.length() + owner.length()) >= 16)
                           owner = owner.substring(0, 16);*/
                    }
                    prefix = prefix.replace("$money$", new DecimalFormat().format(playerData.getMoney()).replace(",", ".")).replace("$chunk_size$", String.valueOf(playerData.getChunks().size())).replace("$chunk_owner$", owner);
                    team.setPrefix(prefix);
                    score--;
                }
            }
        else {
            final Scoreboard scoreboard = target.getScoreboard();
            scoreboard.getObjective(DisplaySlot.SIDEBAR).setDisplayName(Message.DISPLAY_NAME.getMessage());

            final List<String> scores = ((ArrayList<String>) Message.SCOREBOARD.getMessageRaw());

            final PlayerData playerData = ChunkClaim.INSTANCE.getChunkPlayer(target).getPlayerData();

            int score = scores.size();
            for (String s : scores) {
                final Team team = scoreboard.getTeam("x" + score);
                String prefix = s;

                String owner;
                if (ChunkClaim.INSTANCE.getChunkManager().getOwner(target.getLocation().getChunk()) == null)
                    owner = Message.UNKNOWN_CHUNK_OWNER.getMessage();
                else {
                    owner = Bukkit.getOfflinePlayer(ChunkClaim.INSTANCE.getChunkManager().getOwner(target.getLocation().getChunk())).getName();
                /*if (prefix.contains("$chunk_owner$"))
                    if ((prefix.length() + owner.length()) >= 16)
                        owner = owner.substring(0, 16);*/
                }

                prefix = prefix.replace("$money$", new DecimalFormat().format(playerData.getMoney()).replace(",", ".")).replace("$chunk_size$", String.valueOf(playerData.getChunks().size())).replace("$chunk_owner$", owner);
                team.setPrefix(prefix);
                score--;
            }
        }
    }

}
