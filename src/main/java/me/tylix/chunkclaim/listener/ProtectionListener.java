package me.tylix.chunkclaim.listener;

import me.tylix.chunkclaim.ChunkClaim;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class ProtectionListener implements Listener {

    private ChunkClaim instance = ChunkClaim.INSTANCE;

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

            if (ChunkClaim.INSTANCE.getChunkManager().getOwner(entityChunk) != null && !ChunkClaim.INSTANCE.getChunkManager().getOwner(entityChunk).equals(player.getUniqueId())) {
                event.setCancelled(true);
                event.setDamage(0);
                return;
            }
        }
    }

    @EventHandler
    public void handleClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        if (event.getClickedInventory().getType().equals(InventoryType.PLAYER))
            event.setCancelled(event.getCurrentItem().equals(ChunkClaim.Items.MENU));
    }

    @EventHandler
    public void handleDrop(InventoryMoveItemEvent event) {
        event.setCancelled(event.getItem().equals(ChunkClaim.Items.MENU));
    }

    @EventHandler
    public void handleDrop(PlayerDropItemEvent event) {
        Player player = event.getPlayer();

        event.setCancelled(event.getItemDrop().getItemStack().equals(ChunkClaim.Items.MENU));
    }

    @EventHandler
    public void handleSwap(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();

        event.setCancelled(event.getMainHandItem().equals(ChunkClaim.Items.MENU) || event.getOffHandItem().equals(ChunkClaim.Items.MENU));
    }

}
