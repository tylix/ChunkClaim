package me.tylix.chunkclaim.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class LocationManager {

    private File file;
    private YamlConfiguration cfg;

    public LocationManager(final String plugin) {
        this.file = new File("plugins//" + plugin + "//locations.yml");
        this.cfg = YamlConfiguration.loadConfiguration(this.file);
    }

    public LocationManager(final String plugin, final String file) {
        this.file = new File("plugins//" + plugin + "//" + file + ".yml");
        this.cfg = YamlConfiguration.loadConfiguration(this.file);
    }

    public LocationManager createLocation(final Location location, final String path) {
        double x = location.getX();
        double y = location.getBlockY();
        double z = location.getZ();
        float yaw = location.getYaw();
        float pitch = location.getPitch();
        World world = location.getWorld();

        cfg.set(path, world.getName() + ";" + x + ";" + y + ";" + z + ";" + yaw + ";" + pitch);
        return this;
    }

    public LocationManager createLocation(final Player player, final String path) {
        double x = player.getLocation().getX();
        double y = player.getLocation().getBlockY();
        double z = player.getLocation().getZ();
        float yaw = player.getLocation().getYaw();
        float pitch = player.getLocation().getPitch();
        World world = player.getWorld();

        cfg.set(path, world.getName() + ";" + x + ";" + y + ";" + z + ";" + yaw + ";" + pitch);
        return this;
    }

    public Location getLocation(final String path) {
        String[] location = cfg.getString(path).split(";");
        return new Location(Bukkit.getWorld(location[0]), Double.parseDouble(location[1]), Double.parseDouble(location[2]), Double.parseDouble(location[3]), Float.parseFloat(location[4]), Float.parseFloat(location[5]));
    }

    public void save() {
        try {
            cfg.save(file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
