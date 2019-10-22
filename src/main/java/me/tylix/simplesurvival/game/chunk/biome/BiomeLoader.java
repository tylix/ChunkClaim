package me.tylix.simplesurvival.game.chunk.biome;

import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BiomeLoader {

    private final File file;
    private final YamlConfiguration cfg;

    public static final List<BiomeData> BIOMES = new ArrayList<>();

    public BiomeLoader() {
        file = new File("plugins/SimpleSurvival/data/chunkBiomes.yml");
        cfg = YamlConfiguration.loadConfiguration(file);

        if (cfg.getStringList("Biomes").isEmpty()) {
            final List<String> biomes = new ArrayList<>(Lists.newArrayList(Biome.PLAINS.name() + ";" + Material.GRASS_BLOCK.name(), Biome.DESERT.name() + ";" + Material.SAND.name()));
            cfg.set("Biomes", biomes);
            try {
                cfg.save(file);
            } catch (IOException ignore) {
            }
        }

        for (String biome : cfg.getStringList("Biomes"))
            BIOMES.add(new BiomeData(Biome.valueOf(biome.split(";")[0]), Material.valueOf(biome.split(";")[1])));
    }

}
