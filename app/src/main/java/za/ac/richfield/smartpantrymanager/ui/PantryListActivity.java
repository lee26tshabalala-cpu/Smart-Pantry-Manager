package za.ac.richfield.smartpantrymanager.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import za.ac.richfield.smartpantrymanager.R;
import za.ac.richfield.smartpantrymanager.adapter.PantryAdapter;
import za.ac.richfield.smartpantrymanager.db.DatabaseHelper;
import za.ac.richfield.smartpantrymanager.model.PantryItem;

/**
 * Screen 1: Pantry List — shows everything the user currently has,
 * backed by a RecyclerView + PantryAdapter. Entry point (launcher) Activity.
 */
public class PantryListActivity extends AppCompatActivity implements PantryAdapter.OnPantryItemClickListener {

    private DatabaseHelper dbHelper;
    private RecyclerView recyclerView;
    private PantryAdapter adapter;
    private TextView emptyView;
    private List<PantryItem> pantryItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);
        setTitle(R.string.title_pantry_list);

        dbHelper = DatabaseHelper.getInstance(this);

        recyclerView = findViewById(R.id.recyclerPantry);
        emptyView = findViewById(R.id.textPantryEmpty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = findViewById(R.id.fabAddPantryItem);
        fab.setOnClickListener(v -> startActivity(
                new Intent(PantryListActivity.this, AddEditIngredientActivity.class)));

        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems(); // refresh in case an item was added/edited/deleted
    }

    private void loadPantryItems() {
        pantryItems = dbHelper.getAllPantryItems();
        adapter = new PantryAdapter(pantryItems, this);
        recyclerView.setAdapter(adapter);

        boolean empty = pantryItems.isEmpty();
        emptyView.setVisibility(empty ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onEditClick(PantryItem item) {
        Intent intent = new Intent(this, AddEditIngredientActivity.class);
        intent.putExtra(AddEditIngredientActivity.EXTRA_ITEM_ID, item.getId());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(PantryItem item) {
        dbHelper.deletePantryItem(item.getId());
        loadPantryItems();
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_pantry);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_pantry) {
                return true; // already here
            } else if (id == R.id.nav_suggested) {
                startActivity(new Intent(this, SuggestedRecipesActivity.class));
                return true;
            } else if (id == R.id.nav_settings) {
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            }
            return false;
        });
    }
}
