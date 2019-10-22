package me.tylix.simplesurvival.game.recipes;

import org.bukkit.Material;

public class ShapelessIngredientData {

    private final Material material;
    private final int size;

    public ShapelessIngredientData(Material material, int size) {
        this.material = material;
        this.size = size;
    }

    public Material getMaterial() {
        return material;
    }

    public int getSize() {
        return size;
    }
}
