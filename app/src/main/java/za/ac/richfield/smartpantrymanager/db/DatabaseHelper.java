package za.ac.richfield.smartpantrymanager.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import za.ac.richfield.smartpantrymanager.model.PantryItem;
import za.ac.richfield.smartpantrymanager.model.Recipe;
import za.ac.richfield.smartpantrymanager.model.RecipeIngredient;

/**
 * Central SQLite access point for the app.
 *
 * Schema (see report's ER diagram for the visual version):
 *
 *   pantry_items(_id, name, quantity, unit, expiry_date)
 *
 *   recipes(_id, name, steps)
 *
 *   recipe_ingredients(_id, recipe_id FK -> recipes._id, name, quantity, unit)
 *
 * recipes : recipe_ingredients is one-to-many, ON DELETE CASCADE so a
 * recipe removal cleans up its own ingredient rows.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;

    // Pantry table
    public static final String TABLE_PANTRY = "pantry_items";
    public static final String COL_PANTRY_ID = "_id";
    public static final String COL_PANTRY_NAME = "name";
    public static final String COL_PANTRY_QTY = "quantity";
    public static final String COL_PANTRY_UNIT = "unit";
    public static final String COL_PANTRY_EXPIRY = "expiry_date";

    // Recipes table
    public static final String TABLE_RECIPES = "recipes";
    public static final String COL_RECIPE_ID = "_id";
    public static final String COL_RECIPE_NAME = "name";
    public static final String COL_RECIPE_STEPS = "steps";

    // Recipe ingredients table
    public static final String TABLE_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String COL_RI_ID = "_id";
    public static final String COL_RI_RECIPE_ID = "recipe_id";
    public static final String COL_RI_NAME = "name";
    public static final String COL_RI_QTY = "quantity";
    public static final String COL_RI_UNIT = "unit";

    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PANTRY + " (" +
                COL_PANTRY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_PANTRY_NAME + " TEXT NOT NULL, " +
                COL_PANTRY_QTY + " REAL NOT NULL, " +
                COL_PANTRY_UNIT + " TEXT, " +
                COL_PANTRY_EXPIRY + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPES + " (" +
                COL_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RECIPE_NAME + " TEXT NOT NULL, " +
                COL_RECIPE_STEPS + " TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_RECIPE_INGREDIENTS + " (" +
                COL_RI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_RI_RECIPE_ID + " INTEGER NOT NULL, " +
                COL_RI_NAME + " TEXT NOT NULL, " +
                COL_RI_QTY + " REAL NOT NULL, " +
                COL_RI_UNIT + " TEXT, " +
                "FOREIGN KEY(" + COL_RI_RECIPE_ID + ") REFERENCES " +
                TABLE_RECIPES + "(" + COL_RECIPE_ID + ") ON DELETE CASCADE)");

        RecipeSeeder.seed(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PANTRY);
        onCreate(db);
    }

    // ---------------------------------------------------------------
    // Pantry CRUD
    // ---------------------------------------------------------------

    public long addPantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = pantryToValues(item);
        return db.insert(TABLE_PANTRY, null, cv);
    }

    public int updatePantryItem(PantryItem item) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = pantryToValues(item);
        return db.update(TABLE_PANTRY, cv, COL_PANTRY_ID + "=?",
                new String[]{String.valueOf(item.getId())});
    }

    public int deletePantryItem(long id) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_PANTRY, COL_PANTRY_ID + "=?", new String[]{String.valueOf(id)});
    }

    public List<PantryItem> getAllPantryItems() {
        List<PantryItem> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_PANTRY, null, null, null, null, null, COL_PANTRY_NAME + " ASC");
        while (c.moveToNext()) {
            items.add(cursorToPantryItem(c));
        }
        c.close();
        return items;
    }

    private ContentValues pantryToValues(PantryItem item) {
        ContentValues cv = new ContentValues();
        cv.put(COL_PANTRY_NAME, item.getName());
        cv.put(COL_PANTRY_QTY, item.getQuantity());
        cv.put(COL_PANTRY_UNIT, item.getUnit());
        cv.put(COL_PANTRY_EXPIRY, item.getExpiryDate());
        return cv;
    }

    private PantryItem cursorToPantryItem(Cursor c) {
        return new PantryItem(
                c.getLong(c.getColumnIndexOrThrow(COL_PANTRY_ID)),
                c.getString(c.getColumnIndexOrThrow(COL_PANTRY_NAME)),
                c.getDouble(c.getColumnIndexOrThrow(COL_PANTRY_QTY)),
                c.getString(c.getColumnIndexOrThrow(COL_PANTRY_UNIT)),
                c.getString(c.getColumnIndexOrThrow(COL_PANTRY_EXPIRY)));
    }

    // ---------------------------------------------------------------
    // Recipe reads (recipes are seeded once; app does not edit them)
    // ---------------------------------------------------------------

    public List<Recipe> getAllRecipesWithIngredients() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor rc = db.query(TABLE_RECIPES, null, null, null, null, null, COL_RECIPE_NAME + " ASC");
        while (rc.moveToNext()) {
            Recipe recipe = new Recipe(
                    rc.getLong(rc.getColumnIndexOrThrow(COL_RECIPE_ID)),
                    rc.getString(rc.getColumnIndexOrThrow(COL_RECIPE_NAME)),
                    rc.getString(rc.getColumnIndexOrThrow(COL_RECIPE_STEPS)));

            Cursor ic = db.query(TABLE_RECIPE_INGREDIENTS, null,
                    COL_RI_RECIPE_ID + "=?", new String[]{String.valueOf(recipe.getId())},
                    null, null, COL_RI_NAME + " ASC");
            while (ic.moveToNext()) {
                recipe.addIngredient(new RecipeIngredient(
                        ic.getLong(ic.getColumnIndexOrThrow(COL_RI_ID)),
                        recipe.getId(),
                        ic.getString(ic.getColumnIndexOrThrow(COL_RI_NAME)),
                        ic.getDouble(ic.getColumnIndexOrThrow(COL_RI_QTY)),
                        ic.getString(ic.getColumnIndexOrThrow(COL_RI_UNIT))));
            }
            ic.close();
            recipes.add(recipe);
        }
        rc.close();
        return recipes;
    }

    public Recipe getRecipeById(long recipeId) {
        for (Recipe r : getAllRecipesWithIngredients()) {
            if (r.getId() == recipeId) return r;
        }
        return null;
    }
}
