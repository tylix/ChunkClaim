package me.tylix.chunkclaim.listener;

import me.tylix.chunkclaim.ChunkClaim;
import me.tylix.chunkclaim.game.LocationManager;
import me.tylix.chunkclaim.game.deathcause.DeathCauseData;
import me.tylix.chunkclaim.game.item.ItemBuilder;
import me.tylix.chunkclaim.message.Message;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class PlayerDeathEventListener implements Listener {

    public static final Map<UUID, Location> LOCATION_MAP = new HashMap<>();

    @EventHandler
    public void handleDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        Player killer = player.getKiller();

        if (killer == null) {
            final DeathCauseData data = ChunkClaim.INSTANCE.getDeathCauseLoader().getData(player.getLastDamageCause().getCause());
            if (data != null) {
                String message = data.getMessage()[new Random().nextInt(data.getMessage().length)];
                event.setDeathMessage(Message.PLAYER_DEATH.getMessage(player.getName(), message));
            } else
                event.setDeathMessage(Message.PLAYER_DEATH.getMessage(player.getName(), "died"));

            LOCATION_MAP.put(player.getUniqueId(), player.getLocation());

            TextComponent textComponent = new TextComponent();
            textComponent.setText(Message.TELEPORT_TO_DEATH_POINT.getMessage());
            textComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(Message.TELEPORT_TO_DEATH_HOVER.getMessage()).create()));
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/back"));
            Bukkit.getScheduler().runTaskLater(ChunkClaim.INSTANCE, () -> player.spigot().sendMessage(textComponent), 5);
        } else {
            event.setDeathMessage(Message.PLAYER_KILLED.getMessage(player.getName(), killer.getName()));
        }

        final ItemStack[] contents = player.getInventory().getContents();
        final ItemStack[] armorStandContents = player.getInventory().getArmorContents();
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        event.getDrops().clear();
        for (ItemStack content : contents)
            if (content != null && !content.getType().equals(Material.AIR))
                if (!content.equals(ChunkClaim.Items.MENU))
                    player.getLocation().getWorld().dropItem(player.getLocation(), new ItemBuilder(content).setDisplayName(content.getItemMeta().getDisplayName()).build());
                else
                    content.setType(Material.AIR);
        for (ItemStack content : armorStandContents)
            if (content != null && !content.getType().equals(Material.AIR))
                player.getLocation().getWorld().dropItem(player.getLocation(), new ItemBuilder(content).setDisplayName(content.getItemMeta().getDisplayName()).build());

    }

    @EventHandler
    public void handleRespawn(PlayerRespawnEvent event) {
        final Player player = event.getPlayer();

        if (player.getBedSpawnLocation() == null) {
            if (!ChunkClaim.INSTANCE.getChunkPlayer(player).getPlayerData().getChunks().isEmpty())
                player.teleport(ChunkClaim.INSTANCE.getChunkManager().getCenter(ChunkClaim.INSTANCE.getChunkPlayer(player).getPlayerData().getChunks().get(0)));
            else
                player.teleport(new LocationManager("ChunkClaim").getLocation("Spawn"));
        } else {
            player.teleport(player.getBedSpawnLocation());
        }

        //player.getInventory().setItem(8, ChunkClaim.Items.MENU);

        /*for (VeranyItem item : instance.getKitManager().getKitByIcon(Material.LEATHER_CHESTPLATE).getItems())
            if (!item.isOffHand())
                player.getInventory().setItem(item.getSlot(), item.toItemStack());
            else
                player.getInventory().setItemInOffHand(item.toItemStack());*/
    }

}
