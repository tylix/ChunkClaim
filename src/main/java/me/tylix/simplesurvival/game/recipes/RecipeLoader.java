package me.tylix.simplesurvival.game.recipes;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import me.tylix.simplesurvival.SimpleSurvival;
import me.tylix.simplesurvival.game.item.ItemBuilder;
import me.tylix.simplesurvival.message.Description;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecipeLoader {

    private final File file;
    private final YamlConfiguration cfg;

    private final List<RecipeData> recipeData;

    public RecipeLoader() {
        recipeData = new ArrayList<>();

        this.file = new File("plugins/SimpleSurvival/data/recipes.yml");
        this.cfg = YamlConfiguration.loadConfiguration(file);
    }

    public void loadRecipes() {
        if (cfg.getStringList("Recipes").isEmpty()) {
            final List<String> recipes = new ArrayList<>(Lists.newArrayList(SimpleSurvival.INSTANCE.getPrettyGson().toJson(new Gson().toJsonTree(new RecipeData("test0", "§aTest", Material.STONE, true, new Description(" A ", "AAA", " A ").getDescription(), new IngredientData[]{new IngredientData('A', Material.COBBLESTONE)}, null))), SimpleSurvival.INSTANCE.getPrettyGson().toJson(new Gson().toJsonTree(new RecipeData("test1", "§bTest", Material.COBBLESTONE, false, new Description(" A ", "AAA", " A ").getDescription(), null, new ShapelessIngredientData(Material.STONE, 2))))));
            cfg.set("Recipes", recipes);
            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        for (String recipes : cfg.getStringList("Recipes")) {
            final RecipeData data = new Gson().fromJson(recipes, RecipeData.class);
            recipeData.add(data);
        }

        for (RecipeData recipeDatum : recipeData) {
            if (recipeDatum.isShaped()) {
                final ShapedRecipe shapedRecipe = new ShapedRecipe(new NamespacedKey(SimpleSurvival.INSTANCE, recipeDatum.getRecipeName()), new ItemBuilder(recipeDatum.getResult()).setDisplayName(recipeDatum.getItemName()).build());
                shapedRecipe.shape(recipeDatum.getShape());
                for (IngredientData key : recipeDatum.getIngredientData())
                    shapedRecipe.setIngredient(key.getKey(), key.getMaterial());
                Bukkit.addRecipe(shapedRecipe);
            } else {
                final ShapelessRecipe shapelessRecipe = new ShapelessRecipe(new NamespacedKey(SimpleSurvival.INSTANCE, recipeDatum.getRecipeName()), new ItemBuilder(recipeDatum.getResult()).setDisplayName(recipeDatum.getItemName()).build());
                for (int i = 0; i < recipeDatum.getShapelessIngredientData().getSize(); i++)
                    shapelessRecipe.addIngredient(recipeDatum.getShapelessIngredientData().getMaterial());
                Bukkit.addRecipe(shapelessRecipe);
            }
        }
    }

    public void reload() {
        Bukkit.resetRecipes();

        recipeData.clear();
        loadRecipes();
    }

    public List<RecipeData> getRecipeData() {
        return recipeData;
    }
}
