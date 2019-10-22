package me.tylix.simplesurvival.listener;

import me.tylix.simplesurvival.SimpleSurvival;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class ProtectionListener implements Listener {

    private SimpleSurvival instance = SimpleSurvival.INSTANCE;

    @EventHandler
    public void handleBreak(BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Chunk chunk = event.getBlock().getChunk();

        if (instance.getChunkManager().isFree(chunk)) {
            event.setCancelled(true);
            return;
        }

        if (!instance.getChunkManager().getOwner(chunk).equals(player.getUniqueId())) {
            event.setCancelled(true);
            return;
        }

    }

    @EventHandler
    public void handleBreak(BlockPlaceEvent event) {
        final Player player = event.getPlayer();
        final Chunk chunk = event.getBlock().getChunk();

        if (instance.getChunkManager().isFree(chunk)) {
            event.setCancelled(true);
            return;
        }

        if (!instance.getChunkManager().getOwner(chunk).equals(player.getUniqueId())) {
            event.setCancelled(true);
            return;
        }

    }

    @EventHandler
    public void handleBreak(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            final Chunk chunk = event.getClickedBlock().getChunk();

            if (instance.getChunkManager().isFree(chunk)) {
                event.setCancelled(true);
                return;
            }

            if (!instance.getChunkManager().getOwner(chunk).equals(player.getUniqueId())) {
                event.setCancelled(true);
                return;
            }
        }
        if (event.getAction().equals(Action.PHYSICAL)) {
            final Chunk chunk = event.getClickedBlock().getChunk();

            if (instance.getChunkManager().isFree(chunk)) {
                event.setCancelled(true);
                return;
            }

            if (!instance.getChunkManager().getOwner(chunk).equals(player.getUniqueId())) {
                event.setCancelled(true);
                return;
            }
        }

    }

    @EventHandler
    public void handleDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            Entity entity = event.getEntity();
            Chunk entityChunk = entity.getLocation().getChunk();

            if (SimpleSurvival.INSTANCE.getChunkManager().getOwner(entityChunk) != null && !SimpleSurvival.INSTANCE.getChunkManager().getOwner(entityChunk).equals(player.getUniqueId())) {
                event.setCancelled(true);
                event.setDamage(0);
                return;
            }
        }
    }

    @EventHandler
    public void handleDeath(EntityDeathEvent event) {
        final LivingEntity entity = event.getEntity();

        if (entity.getKiller() != null) {
            int money = 10;
            money += entity.getMaxHealth() / 5;
            event.getEntity().getKiller().playSound(event.getEntity().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 2, 1.2F);

            event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation(), SimpleSurvival.Items.MONEY_SKULL.setDisplayname("§6" + money).build());
        }
    }


    @EventHandler
    public void handlePickup(PlayerPickupItemEvent event) {
        Player player = event.getPlayer();

        if (event.getItem().getItemStack().getType().equals(Material.PLAYER_HEAD) && event.getItem().getItemStack().getItemMeta().getDisplayName().startsWith("§6")) {
            final int money = Integer.parseInt(event.getItem().getItemStack().getItemMeta().getDisplayName().replace("§6", ""));
            instance.getChunkPlayer(player).addMoney(money);
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 2, 1);
            event.setCancelled(true);
            event.getItem().remove();
            return;
        }
    }

    @EventHandler
    public void handleClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getCurrentItem() == null) return;
        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER))
            event.setCancelled(event.getCurrentItem().equals(SimpleSurvival.Items.MENU));
    }

    @EventHandler
    public void handleDrop(InventoryMoveItemEvent event) {
        event.setCancelled(event.getItem().equals(SimpleSurvival.Items.MENU));
    }

    @EventHandler
    public void handleDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        event.setCancelled(event.getItemDrop().getItemStack().equals(SimpleSurvival.Items.MENU));
    }

    @EventHandler
    public void handleSwap(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        event.setCancelled(event.getMainHandItem().equals(SimpleSurvival.Items.MENU) || event.getOffHandItem().equals(SimpleSurvival.Items.MENU));
    }

}
