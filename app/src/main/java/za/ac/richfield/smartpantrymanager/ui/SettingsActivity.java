package za.ac.richfield.smartpantrymanager.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RadioGroup;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import za.ac.richfield.smartpantrymanager.R;

/**
 * Screen 5: Settings — satisfies the minimum-screens requirement
 * (Section 3.1). Two simple preferences: expiring-soon alerts toggle
 * and a default units preference, both persisted via SharedPreferences.
 */
public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_EXPIRY_ALERTS = "pref_expiry_alerts";
    public static final String KEY_DEFAULT_UNIT_SYSTEM = "pref_unit_system"; // "metric" or "imperial"

    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle(R.string.title_settings);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Switch expiryAlertsSwitch = findViewById(R.id.switchExpiryAlerts);
        RadioGroup unitGroup = findViewById(R.id.radioGroupUnits);

        expiryAlertsSwitch.setChecked(prefs.getBoolean(KEY_EXPIRY_ALERTS, true));
        expiryAlertsSwitch.setOnCheckedChangeListener((buttonView, isChecked) ->
                prefs.edit().putBoolean(KEY_EXPIRY_ALERTS, isChecked).apply());

        String savedSystem = prefs.getString(KEY_DEFAULT_UNIT_SYSTEM, "metric");
        unitGroup.check(savedSystem.equals("imperial") ? R.id.radioImperial : R.id.radioMetric);
        unitGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String system = (checkedId == R.id.radioImperial) ? "imperial" : "metric";
            prefs.edit().putString(KEY_DEFAULT_UNIT_SYSTEM, system).apply();
        });

        setupBottomNav();
    }

    private void setupBottomNav() {
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigation);
        bottomNav.setSelectedItemId(R.id.nav_settings);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_pantry) {
                startActivity(new Intent(this, PantryListActivity.class));
                return true;
            } else if (id == R.id.nav_suggested) {
                startActivity(new Intent(this, SuggestedRecipesActivity.class));
                return true;
            } else if (id == R.id.nav_settings) {
                return true;
            }
            return false;
        });
    }
}
