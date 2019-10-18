package me.tylix.chunkclaim.game.deathcause;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import me.tylix.chunkclaim.ChunkClaim;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeathCauseLoader {

    private final List<DeathCauseData> dataList;

    private final File file;
    private final YamlConfiguration cfg;

    public DeathCauseLoader() {
        this.file = new File("plugins/ChunkClaim/data/messages/deathCause.yml");
        this.cfg = YamlConfiguration.loadConfiguration(file);
        dataList = new ArrayList<>();
    }

    public void loadDeathCauses() {
        if (cfg.getStringList("DeathCauses").isEmpty()) {
            final List<String> deathCauses = new ArrayList<>();
            for (EntityDamageEvent.DamageCause value : EntityDamageEvent.DamageCause.values())
                deathCauses.add(ChunkClaim.INSTANCE.getPrettyGson().toJson(new Gson().toJsonTree(new DeathCauseData(value, new String[]{value.name()}))));
            cfg.set("DeathCauses", deathCauses);
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (String deathCauses : cfg.getStringList("DeathCauses")) {
            final DeathCauseData data = new Gson().fromJson(deathCauses, DeathCauseData.class);
            dataList.add(data);
        }
    }

    public void reload() {
        dataList.clear();
        loadDeathCauses();
    }

    public DeathCauseData getData(EntityDamageEvent.DamageCause damageCause) {
        for (DeathCauseData data : dataList)
            if (data.getDamageCause().equals(damageCause))
                return data;
        return null;
    }

}
