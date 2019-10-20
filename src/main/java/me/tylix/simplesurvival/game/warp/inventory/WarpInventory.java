package me.tylix.simplesurvival.game.warp.inventory;

import me.tylix.simplesurvival.SimpleSurvival;
import me.tylix.simplesurvival.game.item.ItemBuilder;
import me.tylix.simplesurvival.game.warp.data.WarpData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarpInventory {

    private SimpleSurvival instance = SimpleSurvival.INSTANCE;

    private final UUID uuid;
    private Inventory inventory;

    public WarpInventory(UUID uuid) {
        this.uuid = uuid;
    }

    public WarpInventory setItems(final int page) {
        this.inventory = Bukkit.createInventory(null, 9 * 6, "§7All Warps §8(§3" + instance.getWarpManager().getWarps().size() + "§8): §8[§bPage " + page + "§8]");

        for (int i = 0; i < 9; ++i) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
            }
        }
        for (int i = inventory.getSize() - 9; i < inventory.getSize(); ++i) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
            }
        }
        int j = 0;
        for (int i = 0; i < inventory.getSize() / 9; ++i) {
            if (i != 0 && i != inventory.getSize() - 9)
                inventory.setItem(j, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
            j += 9;
        }
        int k = 8;
        for (int i = 0; i < inventory.getSize() / 9; ++i) {
            if (i != 0 && i != inventory.getSize() - 9)
                inventory.setItem(k, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).setNoName().build());
            k += 9;
        }

        inventory.setItem(17, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(26, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(35, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setNoName().build());
        inventory.setItem(44, new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setNoName().build());

        final List<WarpData> warps;
        if (page == 1)
            warps = this.getWarps(0, 21);
        else {
            warps = this.getWarps(((page - 1) * 21) + 1, page * 21);

            inventory.setItem(9, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setNoName().build());
            inventory.setItem(18, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setNoName().build());
            inventory.setItem(27, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setNoName().build());
            inventory.setItem(36, new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setNoName().build());
        }

        for (WarpData warp : warps)
            inventory.addItem(new ItemBuilder(warp.getItemStack()).setDisplayName("§b" + warp.getName()).addLoreArray(new String[]{" ", " §8» §7Click to teleport"}).build());

        return this;
    }

    private List<WarpData> getWarps(final int from, final int to) {
        final List<WarpData> warps = instance.getWarpManager().getWarps();
        final List<WarpData> toReturn = new ArrayList<>();
        for (int i = from; i < to; i++)
            if (warps.size() > i)
                toReturn.add(warps.get(i));
        return toReturn;
    }

    public void load() {
        Bukkit.getPlayer(uuid).openInventory(inventory);
    }

}
