package me.tylix.simplesurvival.game.achievements.manager;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AchievementLoader {

    private final File file;
    private final YamlConfiguration cfg;

    public AchievementLoader() {
        this.file = new File("plugins/SimpleSurvival/data/achievements.yml");
        this.cfg = YamlConfiguration.loadConfiguration(file);
    }

    public void loadAchievements() {
        /*if (cfg.getStringList("Achievements").isEmpty()) {
            final List<String> recipes = new ArrayList<>(Lists.newArrayList(SimpleSurvival.INSTANCE.getPrettyGson().toJson(new Gson().toJsonTree((KillAchievement) ArrayList::new))));
            cfg.set("Recipes", recipes);
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }*/
        for (CollectType value : CollectType.values()) {
            final List<String> values = new ArrayList<>();
            cfg.set(value.name(), values);
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public enum CollectType {
        PICKUP,
        CRAFT,
        BREAK,
        PLACE,
        KILL,
        EXPLORE,
        BREED,
        PLAYTIME,
        DISTANCE,
        SMELTING,
        ITEM_BREAK,
        PROGRESS;
    }

}
