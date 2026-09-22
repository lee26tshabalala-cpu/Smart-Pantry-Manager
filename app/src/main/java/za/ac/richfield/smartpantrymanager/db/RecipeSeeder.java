package za.ac.richfield.smartpantrymanager.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Seeds the recipes + recipe_ingredients tables on first run (onCreate).
 * 18 recipes, each with a realistic ingredient list, so the strict-matching
 * demo has enough variety to show both "matches" and "does not match".
 */
class RecipeSeeder {

    private static long insertRecipe(SQLiteDatabase db, String name, String steps) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_RECIPE_NAME, name);
        cv.put(DatabaseHelper.COL_RECIPE_STEPS, steps);
        return db.insert(DatabaseHelper.TABLE_RECIPES, null, cv);
    }

    private static void insertIngredient(SQLiteDatabase db, long recipeId, String name, double qty, String unit) {
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COL_RI_RECIPE_ID, recipeId);
        cv.put(DatabaseHelper.COL_RI_NAME, name);
        cv.put(DatabaseHelper.COL_RI_QTY, qty);
        cv.put(DatabaseHelper.COL_RI_UNIT, unit);
        db.insert(DatabaseHelper.TABLE_RECIPE_INGREDIENTS, null, cv);
    }

    static void seed(SQLiteDatabase db) {
        long id;

        id = insertRecipe(db, "Tomato Egg Stir-Fry",
                "1. Beat eggs with a pinch of salt.\n2. Fry tomato until soft.\n3. Add eggs, scramble together, serve hot.");
        insertIngredient(db, id, "egg", 3, "pcs");
        insertIngredient(db, id, "tomato", 2, "pcs");
        insertIngredient(db, id, "salt", 1, "tsp");
        insertIngredient(db, id, "cooking oil", 1, "tbsp");

        id = insertRecipe(db, "Garlic Butter Rice",
                "1. Melt butter, fry garlic until fragrant.\n2. Add cooked rice, stir until coated.\n3. Season and serve.");
        insertIngredient(db, id, "rice", 2, "cup");
        insertIngredient(db, id, "garlic", 3, "pcs");
        insertIngredient(db, id, "butter", 2, "tbsp");
        insertIngredient(db, id, "salt", 1, "tsp");

        id = insertRecipe(db, "Simple Vegetable Soup",
                "1. Sauté onion and carrot.\n2. Add stock and potato, simmer 20 min.\n3. Season and serve.");
        insertIngredient(db, id, "onion", 1, "pcs");
        insertIngredient(db, id, "carrot", 2, "pcs");
        insertIngredient(db, id, "potato", 2, "pcs");
        insertIngredient(db, id, "vegetable stock", 1, "l");

        id = insertRecipe(db, "Cheesy Scrambled Eggs",
                "1. Beat eggs with milk.\n2. Cook on low heat, folding gently.\n3. Fold in cheese off heat, serve.");
        insertIngredient(db, id, "egg", 3, "pcs");
        insertIngredient(db, id, "milk", 50, "ml");
        insertIngredient(db, id, "cheese", 30, "g");

        id = insertRecipe(db, "Pasta Aglio e Olio",
                "1. Boil pasta until al dente.\n2. Fry garlic in olive oil until golden.\n3. Toss pasta through, season, serve.");
        insertIngredient(db, id, "pasta", 200, "g");
        insertIngredient(db, id, "garlic", 4, "pcs");
        insertIngredient(db, id, "olive oil", 3, "tbsp");
        insertIngredient(db, id, "chilli flakes", 1, "tsp");

        id = insertRecipe(db, "Banana Oat Pancakes",
                "1. Mash banana, mix with oats and egg.\n2. Fry small pancakes until golden on both sides.\n3. Serve warm.");
        insertIngredient(db, id, "banana", 2, "pcs");
        insertIngredient(db, id, "oats", 1, "cup");
        insertIngredient(db, id, "egg", 2, "pcs");

        id = insertRecipe(db, "Chicken Fried Rice",
                "1. Fry diced chicken until cooked.\n2. Add rice, egg, and vegetables, stir-fry together.\n3. Season with soy sauce, serve.");
        insertIngredient(db, id, "chicken breast", 200, "g");
        insertIngredient(db, id, "rice", 2, "cup");
        insertIngredient(db, id, "egg", 1, "pcs");
        insertIngredient(db, id, "carrot", 1, "pcs");
        insertIngredient(db, id, "soy sauce", 2, "tbsp");

        id = insertRecipe(db, "Grilled Cheese Sandwich",
                "1. Butter bread on outside.\n2. Layer cheese between slices.\n3. Grill both sides until golden and melted.");
        insertIngredient(db, id, "bread", 2, "pcs");
        insertIngredient(db, id, "cheese", 2, "pcs");
        insertIngredient(db, id, "butter", 1, "tbsp");

        id = insertRecipe(db, "Lentil and Potato Curry",
                "1. Sauté onion and garlic.\n2. Add lentils, potato and stock, simmer until soft.\n3. Season with curry powder, serve.");
        insertIngredient(db, id, "lentils", 1, "cup");
        insertIngredient(db, id, "potato", 2, "pcs");
        insertIngredient(db, id, "onion", 1, "pcs");
        insertIngredient(db, id, "garlic", 2, "pcs");
        insertIngredient(db, id, "curry powder", 1, "tbsp");

        id = insertRecipe(db, "Classic Tomato Pasta",
                "1. Boil pasta.\n2. Simmer tomato with garlic and onion for sauce.\n3. Combine and serve.");
        insertIngredient(db, id, "pasta", 200, "g");
        insertIngredient(db, id, "tomato", 4, "pcs");
        insertIngredient(db, id, "onion", 1, "pcs");
        insertIngredient(db, id, "garlic", 2, "pcs");

        id = insertRecipe(db, "Carrot and Ginger Soup",
                "1. Sauté carrot, onion, and ginger.\n2. Add stock, simmer 20 min, blend smooth.\n3. Serve hot.");
        insertIngredient(db, id, "carrot", 4, "pcs");
        insertIngredient(db, id, "onion", 1, "pcs");
        insertIngredient(db, id, "ginger", 1, "tbsp");
        insertIngredient(db, id, "vegetable stock", 1, "l");

        id = insertRecipe(db, "Peanut Butter Banana Toast",
                "1. Toast bread.\n2. Spread peanut butter.\n3. Top with sliced banana.");
        insertIngredient(db, id, "bread", 2, "pcs");
        insertIngredient(db, id, "peanut butter", 2, "tbsp");
        insertIngredient(db, id, "banana", 1, "pcs");

        id = insertRecipe(db, "Chicken and Rice Bowl",
                "1. Grill or pan-fry chicken breast.\n2. Serve sliced over cooked rice.\n3. Season to taste.");
        insertIngredient(db, id, "chicken breast", 200, "g");
        insertIngredient(db, id, "rice", 1, "cup");
        insertIngredient(db, id, "salt", 1, "tsp");

        id = insertRecipe(db, "Onion Garlic Fried Potatoes",
                "1. Dice potato, parboil.\n2. Fry with onion and garlic until crisp.\n3. Season and serve.");
        insertIngredient(db, id, "potato", 3, "pcs");
        insertIngredient(db, id, "onion", 1, "pcs");
        insertIngredient(db, id, "garlic", 2, "pcs");
        insertIngredient(db, id, "cooking oil", 2, "tbsp");

        id = insertRecipe(db, "Milk and Oats Porridge",
                "1. Simmer oats in milk until thickened.\n2. Sweeten to taste.\n3. Serve warm.");
        insertIngredient(db, id, "oats", 1, "cup");
        insertIngredient(db, id, "milk", 250, "ml");

        id = insertRecipe(db, "Egg Fried Rice",
                "1. Scramble egg in a hot pan.\n2. Add rice, stir-fry together.\n3. Season with soy sauce, serve.");
        insertIngredient(db, id, "egg", 2, "pcs");
        insertIngredient(db, id, "rice", 2, "cup");
        insertIngredient(db, id, "soy sauce", 1, "tbsp");

        id = insertRecipe(db, "Tomato and Cheese Toast",
                "1. Toast bread.\n2. Top with sliced tomato and cheese.\n3. Grill until cheese melts.");
        insertIngredient(db, id, "bread", 2, "pcs");
        insertIngredient(db, id, "tomato", 1, "pcs");
        insertIngredient(db, id, "cheese", 30, "g");

        id = insertRecipe(db, "Chickpea and Potato Curry",
                "1. Sauté onion and garlic.\n2. Add chickpeas, potato, curry powder and stock.\n3. Simmer until potato is soft, serve.");
        insertIngredient(db, id, "chickpeas", 1, "cup");
        insertIngredient(db, id, "potato", 2, "pcs");
        insertIngredient(db, id, "onion", 1, "pcs");
        insertIngredient(db, id, "curry powder", 1, "tbsp");
    }
}
