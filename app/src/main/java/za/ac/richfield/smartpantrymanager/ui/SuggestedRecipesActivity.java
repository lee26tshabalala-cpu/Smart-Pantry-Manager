package za.ac.richfield.smartpantrymanager.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import za.ac.richfield.smartpantrymanager.R;
import za.ac.richfield.smartpantrymanager.adapter.RecipeAdapter;
import za.ac.richfield.smartpantrymanager.db.DatabaseHelper;
import za.ac.richfield.smartpantrymanager.logic.RecipeMatcher;
import za.ac.richfield.smartpantrymanager.model.PantryItem;
import za.ac.richfield.smartpantrymanager.model.Recipe;

/**
 * Screen 3: Suggested Recipes — the screen the strict-matching rule
 * (Section 2.3) exists to feed. Re-runs the match every time the screen
 * is shown, so adding/removing a pantry item and coming back here
 * immediately reflects the change (this is exactly what the video
 * demo in Section 5.1.2 needs to prove).
 */
public class SuggestedRecipesActivity extends AppCompatActivity implements RecipeAdapter.OnRecipeClickListener {

    private DatabaseHelper dbHelper;
    private final RecipeMatcher matcher = new RecipeMatcher();
    private RecyclerView recyclerView;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggested_recipes);
        setTitle(R.string.title_suggested_recipes);

        dbHelper = DatabaseHelper.getInstance(this);
        recyclerView = findViewById(R.id.recyclerSuggestedRecipes);
        emptyView = findViewById(R.id.textSuggestedEmpty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        runMatchingAndDisplay();
    }

    private void runMatchingAndDisplay() {
        List<PantryItem> pantry = dbHelper.getAllPantryItems();
        List<Recipe> allRecipes = dbHelper.getAllRecipesWithIngredients();
        List<Recipe> matches = matcher.getStrictMatches(allRecipes, pantry);

        RecipeAdapter adapter = new RecipeAdapter(matches, this);
        recyclerView.setAdapter(adapter);

        boolean empty = matches.isEmpty();
        emptyView.setVisibility(empty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);

        // Respects the Settings screen's "expiring soon" alert toggle.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean alertsOn = prefs.getBoolean(SettingsActivity.KEY_EXPIRY_ALERTS, true);
        if (alertsOn) {
            checkExpiringSoon(pantry);
        }
    }

    private void checkExpiringSoon(List<PantryItem> pantry) {
        // Placeholder hook: a fuller implementation would compare
        // expiryDate against today's date and show a banner/snackbar
        // for items expiring within e.g. 2 days.
    }

    @Override
    public void onRecipeClick(Recipe recipe) {
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra(RecipeDetailActivity.EXTRA_RECIPE_ID, recipe.getId());
        startActivity(intent);
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_suggested);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_pantry) {
                startActivity(new Intent(this, PantryListActivity.class));
                return true;
            } else if (id == R.id.nav_suggested) {
                return true;
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }
}
