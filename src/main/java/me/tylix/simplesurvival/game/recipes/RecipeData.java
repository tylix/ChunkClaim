package me.tylix.simplesurvival.game.recipes;

import org.bukkit.Material;

public class RecipeData {

    private final String recipeName;
    private final String itemName;
    private final Material result;
    private final boolean shaped;
    private final String[] shape;
    private final IngredientData[] ingredientData;
    private final ShapelessIngredientData shapelessIngredientData;

    public RecipeData(String recipeName, String itemName, Material result, boolean shaped, String[] shape, IngredientData[] ingredientData, ShapelessIngredientData shapelessIngredientData) {
        this.recipeName = recipeName;
        this.itemName = itemName;
        this.result = result;
        this.shaped = shaped;
        this.shape = shape;
        this.ingredientData = ingredientData;
        this.shapelessIngredientData = shapelessIngredientData;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public String getItemName() {
        return itemName;
    }

    public Material getResult() {
        return result;
    }

    public boolean isShaped() {
        return shaped;
    }

    public String[] getShape() {
        return shape;
    }

    public IngredientData[] getIngredientData() {
        return ingredientData;
    }

    public ShapelessIngredientData getShapelessIngredientData() {
        return shapelessIngredientData;
    }
}
