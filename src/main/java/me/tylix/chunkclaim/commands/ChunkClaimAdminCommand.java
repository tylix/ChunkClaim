package me.tylix.chunkclaim.commands;

import me.tylix.chunkclaim.ChunkClaim;
import me.tylix.chunkclaim.game.setup.Setup;
import me.tylix.chunkclaim.message.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChunkClaimAdminCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Player player = (Player) commandSender;

        if (!player.hasPermission("verany.admin")) {
            ChunkClaimCommand.sendHelp(player, 1);
            return false;
        }

        switch (strings.length) {
            case 1:
                if (strings[0].equalsIgnoreCase("setup")) {
                    player.sendMessage(Message.PREFIX.getMessage() + " §7Starting Setup..");
                    Setup setup = new Setup(player);
                    ChunkClaim.INSTANCE.getSetupMap().put(player.getUniqueId(), setup);
                } else if (strings[0].equalsIgnoreCase("admin")) {
                    sendHelp(player,  1);
                } else if (strings[0].equalsIgnoreCase("user")) {
                    ChunkClaimCommand.sendHelp(player, 1);
                } else if (strings[0].equalsIgnoreCase("reload") || strings[0].equalsIgnoreCase("rl")) {
                    boolean success = true;
                    try {
                        ChunkClaim.INSTANCE.reloadPlugin(player);
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
                sendHelp(player,  1);
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
                    "§8» §e/cca setup §8- §7Start the Setup\n" +
                    " ";
        }
        player.sendMessage(message);
    }
}
