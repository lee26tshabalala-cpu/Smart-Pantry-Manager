package za.ac.richfield.smartpantrymanager.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import za.ac.richfield.smartpantrymanager.R;
import za.ac.richfield.smartpantrymanager.db.DatabaseHelper;
import za.ac.richfield.smartpantrymanager.model.Recipe;
import za.ac.richfield.smartpantrymanager.model.RecipeIngredient;

/**
 * Screen 4: Recipe Detail — full ingredient list and method for a
 * single recipe, reached via Intent extra from either SuggestedRecipesActivity.
 */
public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "extra_recipe_id";

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        setTitle(R.string.title_recipe_detail);

        dbHelper = DatabaseHelper.getInstance(this);

        long recipeId = getIntent().getLongExtra(EXTRA_RECIPE_ID, -1);
        Recipe recipe = dbHelper.getRecipeById(recipeId);

        TextView nameView = findViewById(R.id.textDetailRecipeName);
        TextView ingredientsView = findViewById(R.id.textDetailIngredients);
        TextView stepsView = findViewById(R.id.textDetailSteps);

        if (recipe == null) {
            nameView.setText(R.string.recipe_not_found);
            return;
        }

        nameView.setText(recipe.getName());

        StringBuilder ingredientsText = new StringBuilder();
        for (RecipeIngredient ri : recipe.getIngredients()) {
            ingredientsText.append("\u2022 ")
                    .append(trimTrailingZero(ri.getQuantity()))
                    .append(" ")
                    .append(ri.getUnit())
                    .append(" ")
                    .append(ri.getName())
                    .append("\n");
        }
        ingredientsView.setText(ingredientsText.toString().trim());
        stepsView.setText(recipe.getSteps());
    }

    private String trimTrailingZero(double value) {
        if (value == Math.floor(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }
}
