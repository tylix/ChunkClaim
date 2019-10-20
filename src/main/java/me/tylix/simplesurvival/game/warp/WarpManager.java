package me.tylix.simplesurvival.game.warp;

import com.google.gson.Gson;
import me.tylix.simplesurvival.game.warp.data.WarpData;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WarpManager {

    private final File file;
    private final YamlConfiguration cfg;
    private final List<WarpData> warps;

    public WarpManager() {
        this.file = new File("plugins/SimpleSurvival/data/warps.yml");
        this.cfg = YamlConfiguration.loadConfiguration(file);

        this.warps = new ArrayList<>();
        this.loadWarps();
    }

    private void loadWarps() {
        System.out.println("Loading all Warps...");
        for (String s : this.cfg.getStringList("Warps")) {
            final WarpData warpData = new Gson().fromJson(s, WarpData.class);
            this.warps.add(warpData);
        }
        System.out.println("All Warps (" + this.warps.size() + ") loaded");
    }

    public void addWarp(final WarpData warpData) {
        this.warps.add(warpData);
        final List<String> warps = this.cfg.getStringList("Warps");
        warps.add(new Gson().toJson(warpData));
        this.cfg.set("Warps", warps);
        try {
            this.cfg.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeWarp(final WarpData warpData) {
        final List<String> warps = this.cfg.getStringList("Warps");
        warps.remove(new Gson().toJson(this.getWarpByName(warpData.getName())));
        this.cfg.set("Warps", warps);
        try {
            this.cfg.save(this.file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.warps.remove(warpData);
    }

    public boolean existsWarp(final String name) {
        for (WarpData warp : this.warps)
            if (warp.getName().equalsIgnoreCase(name))
                return true;
        return false;
    }

    public WarpData getWarpByMaterial(final Material material) {
        for (WarpData warp : this.warps)
            if (warp.getItemStack().getType().equals(material))
                return warp;
        return null;
    }

    public WarpData getWarpByName(final String name) {
        for (WarpData warp : this.warps)
            if (warp.getName().equalsIgnoreCase(name))
                return warp;
        return null;
    }

    public List<WarpData> getWarps() {
        return warps;
    }
}
