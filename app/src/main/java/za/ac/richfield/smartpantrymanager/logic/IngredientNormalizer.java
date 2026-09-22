package za.ac.richfield.smartpantrymanager.logic;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles the "reasonably robust to real-world messiness" requirement
 * from the brief (Section 2.3): naive exact-string matching is explicitly
 * marked down, so this class:
 *
 *  1. Normalises ingredient NAMES — lower-cases, trims, and strips a
 *     trailing "s"/"es" so "tomato" and "tomatoes" are recognised as the
 *     same ingredient (a light stemmer, not a dictionary — good enough
 *     for the scope of this assignment, documented as such in the report).
 *
 *  2. Normalises UNITS into a common base so quantities can be compared
 *     fairly even when the pantry and the recipe use different units for
 *     the same kind of measurement (e.g. "kg" vs "g").
 */
public final class IngredientNormalizer {

    private IngredientNormalizer() {
    }

    /** Converts a raw ingredient name into a comparable key. */
    public static String normalizeName(String rawName) {
        if (rawName == null) return "";
        String name = rawName.trim().toLowerCase();

        // Strip a trailing "es" (tomatoes -> tomato-ish) or "s" (eggs -> egg),
        // but don't strip short words where that would be wrong (e.g. "gas").
        if (name.endsWith("es") && name.length() > 4) {
            name = name.substring(0, name.length() - 2);
        } else if (name.endsWith("s") && !name.endsWith("ss") && name.length() > 3) {
            name = name.substring(0, name.length() - 1);
        }
        return name;
    }

    /** Base units this app understands, grouped by dimension. */
    private static final Map<String, String> UNIT_TO_BASE = new HashMap<>();
    private static final Map<String, Double> UNIT_TO_BASE_FACTOR = new HashMap<>();

    static {
        // Mass -> grams
        UNIT_TO_BASE.put("g", "mass");
        UNIT_TO_BASE_FACTOR.put("g", 1.0);
        UNIT_TO_BASE.put("gram", "mass");
        UNIT_TO_BASE_FACTOR.put("gram", 1.0);
        UNIT_TO_BASE.put("kg", "mass");
        UNIT_TO_BASE_FACTOR.put("kg", 1000.0);

        // Volume -> millilitres
        UNIT_TO_BASE.put("ml", "volume");
        UNIT_TO_BASE_FACTOR.put("ml", 1.0);
        UNIT_TO_BASE.put("l", "volume");
        UNIT_TO_BASE_FACTOR.put("l", 1000.0);
        UNIT_TO_BASE.put("cup", "volume");
        UNIT_TO_BASE_FACTOR.put("cup", 250.0);
        UNIT_TO_BASE.put("tbsp", "volume");
        UNIT_TO_BASE_FACTOR.put("tbsp", 15.0);
        UNIT_TO_BASE.put("tsp", "volume");
        UNIT_TO_BASE_FACTOR.put("tsp", 5.0);

        // Count -> pieces (anything not otherwise recognised also falls back here)
        UNIT_TO_BASE.put("pcs", "count");
        UNIT_TO_BASE_FACTOR.put("pcs", 1.0);
        UNIT_TO_BASE.put("pc", "count");
        UNIT_TO_BASE_FACTOR.put("pc", 1.0);
    }

    private static String cleanUnit(String rawUnit) {
        if (rawUnit == null) return "pcs";
        String u = rawUnit.trim().toLowerCase();
        return u.isEmpty() ? "pcs" : u;
    }

    /** The measurement "dimension" a unit belongs to (mass / volume / count). */
    public static String dimensionOf(String rawUnit) {
        String u = cleanUnit(rawUnit);
        return UNIT_TO_BASE.getOrDefault(u, "count");
    }

    /** Converts a quantity into its base unit for the given unit's dimension. */
    public static double toBaseQuantity(double quantity, String rawUnit) {
        String u = cleanUnit(rawUnit);
        double factor = UNIT_TO_BASE_FACTOR.getOrDefault(u, 1.0);
        return quantity * factor;
    }
}
