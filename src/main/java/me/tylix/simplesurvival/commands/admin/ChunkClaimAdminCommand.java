package me.tylix.simplesurvival.commands.admin;

import me.tylix.simplesurvival.SimpleSurvival;
import me.tylix.simplesurvival.commands.chunk.ChunkClaimCommand;
import me.tylix.simplesurvival.game.setup.Setup;
import me.tylix.simplesurvival.message.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChunkClaimAdminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (!player.hasPermission("simplesurvival.admin")) {
            ChunkClaimCommand.sendHelp(player, 1);
            return false;
        }

        switch (strings.length) {
            case 1:
                if (strings[0].equalsIgnoreCase("setup")) {
                    player.sendMessage(Message.PREFIX.getMessage() + " §7Starting Setup..");
                    Setup setup = new Setup(player);
                    SimpleSurvival.INSTANCE.getSetupMap().put(player.getUniqueId(), setup);
                } else if (strings[0].equalsIgnoreCase("admin")) {
                    sendHelp(player, 1);
                } else if (strings[0].equalsIgnoreCase("user")) {
                    ChunkClaimCommand.sendHelp(player, 1);
                } else if (strings[0].equalsIgnoreCase("reload") || strings[0].equalsIgnoreCase("rl")) {
                    boolean success = true;
                    try {
                        SimpleSurvival.INSTANCE.reloadPlugin(player);
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

                } else if (strings[0].equalsIgnoreCase("reset")) {
                    player.sendMessage(Message.PREFIX.getMessageRaw() + " §7Are you sure to reset all messages?");
                    player.sendMessage(Message.PREFIX.getMessageRaw() + " §a/cca reset confirm");
                }
                break;
            case 2:
                if (strings[0].equalsIgnoreCase("reset") && strings[1].equalsIgnoreCase("confirm")) {
                    boolean success = true;
                    try {
                        player.sendMessage(Message.PREFIX.getMessageRaw() + " §7Reset messages...");
                        SimpleSurvival.INSTANCE.getMessageManager().reset();
                    } catch (Exception e) {
                        e.printStackTrace();
                        player.sendMessage(Message.PREFIX.getMessageRaw() + " §cAn error occurred while reset messages (check console/logs for more info)! [" + e.toString() + "]");
                        success = false;
                    } finally {
                        if (success)
                            player.sendMessage(Message.PREFIX.getMessage() + " §2Reset successfully");
                        else
                            player.sendMessage(Message.PREFIX.getMessage() + " §4Reset failed!");
                    }
                }
                break;
            default:
                sendHelp(player, 1);
                break;
        }
        return false;
    }

    private void sendHelp(Player player, int admin) {
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
                    "§8» §e/cca reload§8- §7Reloading config\n" +
                    "§8» §e/cca reset§8- §7Reset Config to default (en_US)\n" +
                    "§8» §e/cca setup §8- §7Start the Setup\n" +
                    " ";
        }
        player.sendMessage(message);
    }
}
