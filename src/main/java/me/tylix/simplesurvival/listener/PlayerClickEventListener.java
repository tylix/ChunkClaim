package me.tylix.simplesurvival.listener;

import me.tylix.simplesurvival.SimpleSurvival;
import me.tylix.simplesurvival.game.home.data.HomeData;
import me.tylix.simplesurvival.game.menu.MenuInventory;
import me.tylix.simplesurvival.game.warp.data.WarpData;
import me.tylix.simplesurvival.game.warp.inventory.WarpInventory;
import me.tylix.simplesurvival.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class PlayerClickEventListener implements Listener {

    private SimpleSurvival instance = SimpleSurvival.INSTANCE;

    @EventHandler
    public void handleClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null) return;

        if (player.getOpenInventory().getTitle().equals(Message.WARP_INVENTORY_NAME.getMessage().replace("$page$", String.valueOf(instance.getWarpPage().get(player.getUniqueId()))).replace("$warp_size$", String.valueOf(instance.getWarpManager().getWarps().size())))) {
            event.setCancelled(true);
            if (event.getCurrentItem().getType().equals(Material.GREEN_STAINED_GLASS_PANE)) {
                instance.getWarpPage().put(player.getUniqueId(), instance.getWarpPage().get(player.getUniqueId()) + 1);
                new WarpInventory(player.getUniqueId()).setItems(instance.getWarpPage().get(player.getUniqueId())).load();
                return;
            }
            if (event.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE)) {
                instance.getWarpPage().put(player.getUniqueId(), instance.getWarpPage().get(player.getUniqueId()) - 1);
                new WarpInventory(player.getUniqueId()).setItems(instance.getWarpPage().get(player.getUniqueId())).load();
                return;
            }
            if (event.getCurrentItem().getType().equals(Material.GRAY_STAINED_GLASS_PANE)) return;
            final WarpData warpData = instance.getWarpManager().getWarpByName(ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName()));
            player.teleport(warpData.getLocation().toBukkitLocation());
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            return;
        }
    }

    @EventHandler
    public void handleClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (player.getOpenInventory().getTitle().equals(Message.WARP_INVENTORY_NAME.getMessage().replace("$page$", String.valueOf(instance.getWarpPage().get(player.getUniqueId()))).replace("$warp_size$", String.valueOf(instance.getWarpManager().getWarps().size()))))
            instance.getChunkPlayer(player).setWarpInventoryOpen(false);
    }

}
