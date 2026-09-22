package za.ac.richfield.smartpantrymanager.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import za.ac.richfield.smartpantrymanager.R;
import za.ac.richfield.smartpantrymanager.db.DatabaseHelper;
import za.ac.richfield.smartpantrymanager.model.PantryItem;

/**
 * Screen 2: Add / Edit Ingredient. Doubles as both the "Create" and
 * "Update" halves of pantry CRUD, depending on whether EXTRA_ITEM_ID
 * was passed in.
 */
public class AddEditIngredientActivity extends AppCompatActivity {

    public static final String EXTRA_ITEM_ID = "extra_item_id";

    private static final String[] UNITS = {"g", "kg", "ml", "l", "cup", "tbsp", "tsp", "pcs"};

    private DatabaseHelper dbHelper;
    private EditText editName, editQuantity, editExpiry;
    private Spinner spinnerUnit;

    private long editingItemId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_ingredient);

        dbHelper = DatabaseHelper.getInstance(this);

        editName = findViewById(R.id.editIngredientName);
        editQuantity = findViewById(R.id.editIngredientQuantity);
        editExpiry = findViewById(R.id.editIngredientExpiry);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        Button saveButton = findViewById(R.id.buttonSaveIngredient);

        spinnerUnit.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, UNITS));

        editingItemId = getIntent().getLongExtra(EXTRA_ITEM_ID, -1);
        if (editingItemId != -1) {
            setTitle(R.string.title_edit_ingredient);
            prefillForEdit(editingItemId);
        } else {
            setTitle(R.string.title_add_ingredient);
        }

        saveButton.setOnClickListener(v -> onSaveClicked());
    }

    private void prefillForEdit(long id) {
        for (PantryItem item : dbHelper.getAllPantryItems()) {
            if (item.getId() == id) {
                editName.setText(item.getName());
                editQuantity.setText(String.valueOf(item.getQuantity()));
                editExpiry.setText(item.getExpiryDate());
                int unitIndex = indexOf(item.getUnit());
                if (unitIndex >= 0) spinnerUnit.setSelection(unitIndex);
                break;
            }
        }
    }

    private int indexOf(String unit) {
        if (unit == null) return -1;
        for (int i = 0; i < UNITS.length; i++) {
            if (UNITS[i].equalsIgnoreCase(unit)) return i;
        }
        return -1;
    }

    /** Input validation as required by Section 3.1 of the brief. */
    private void onSaveClicked() {
        String name = editName.getText().toString().trim();
        String qtyText = editQuantity.getText().toString().trim();
        String expiry = editExpiry.getText().toString().trim();
        String unit = (String) spinnerUnit.getSelectedItem();

        if (name.isEmpty()) {
            editName.setError(getString(R.string.error_name_required));
            return;
        }
        if (qtyText.isEmpty()) {
            editQuantity.setError(getString(R.string.error_quantity_required));
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(qtyText);
        } catch (NumberFormatException e) {
            editQuantity.setError(getString(R.string.error_quantity_invalid));
            return;
        }
        if (quantity <= 0) {
            editQuantity.setError(getString(R.string.error_quantity_positive));
            return;
        }

        PantryItem item = new PantryItem(editingItemId, name, quantity, unit,
                expiry.isEmpty() ? null : expiry);

        if (editingItemId == -1) {
            dbHelper.addPantryItem(item);
        } else {
            dbHelper.updatePantryItem(item);
        }
        finish();
    }
}
