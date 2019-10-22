package me.tylix.simplesurvival.game.recipes;

import org.bukkit.Material;

public class IngredientData {

    private final char key;
    private final Material material;

    public IngredientData(char key, Material material) {
        this.key = key;
        this.material = material;
    }

    public char getKey() {
        return key;
    }

    public Material getMaterial() {
        return material;
    }
}
