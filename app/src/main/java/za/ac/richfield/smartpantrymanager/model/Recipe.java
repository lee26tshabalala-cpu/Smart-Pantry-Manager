package za.ac.richfield.smartpantrymanager.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A recipe: a name, its method, and the list of ingredients it requires.
 * The ingredient list is populated separately after the base recipe row
 * is loaded (see DatabaseHelper#getAllRecipesWithIngredients).
 */
public class Recipe {

    private long id;
    private String name;
    private String steps;
    private List<RecipeIngredient> ingredients = new ArrayList<>();

    public Recipe() {
    }

    public Recipe(long id, String name, String steps) {
        this.id = id;
        this.name = name;
        this.steps = steps;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public List<RecipeIngredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void addIngredient(RecipeIngredient ingredient) {
        this.ingredients.add(ingredient);
    }
}
