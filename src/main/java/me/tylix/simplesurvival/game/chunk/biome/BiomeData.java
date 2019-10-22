package me.tylix.simplesurvival.game.chunk.biome;

import org.bukkit.Material;
import org.bukkit.block.Biome;

public class BiomeData {

    private final Biome biome;
    private final Material material;

    public BiomeData(Biome biome, Material material) {
        this.biome = biome;
        this.material = material;
    }

    public Biome getBiome() {
        return biome;
    }

    public Material getMaterial() {
        return material;
    }
}
