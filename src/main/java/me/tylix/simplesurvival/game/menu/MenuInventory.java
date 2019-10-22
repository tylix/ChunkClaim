package me.tylix.simplesurvival.game.menu;

import me.tylix.simplesurvival.SimpleSurvival;
import me.tylix.simplesurvival.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.UUID;

public class MenuInventory {

    private final UUID uuid;
    private Inventory inventory;
    private Player player;

    public MenuInventory(UUID uuid) {
        this.uuid = uuid;

        player = Bukkit.getPlayer(uuid);
    }

    public MenuInventory setItems() {
        inventory = Bukkit.createInventory(null, 9*6, Message.MENU_INVENTORY_NAME.getMessage());

        SimpleSurvival.INSTANCE.fillInventory(inventory, SimpleSurvival.INSTANCE.getPlaceholder());



        return this;
    }

    public void load() {
        player.openInventory(inventory);
    }
}
