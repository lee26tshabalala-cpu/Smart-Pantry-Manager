package za.ac.richfield.smartpantrymanager.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import za.ac.richfield.smartpantrymanager.model.PantryItem;
import za.ac.richfield.smartpantrymanager.model.Recipe;
import za.ac.richfield.smartpantrymanager.model.RecipeIngredient;

/**
 * Implements the assignment's Section 2.3 "Strict-Matching Rule":
 *
 *   A recipe may only be suggested if EVERY ingredient it requires is
 *   present in the pantry in at least the required quantity. One missing
 *   or insufficient ingredient disqualifies the whole recipe.
 *
 * This is deliberately a separate, dependency-free class (no Android
 * imports) so it can be unit-tested in isolation and so it's easy to
 * point to in the video's "concept explanation" section.
 */
public class RecipeMatcher {

    /** Result for a single recipe against the current pantry. */
    public static class MatchResult {
        public final Recipe recipe;
        public final boolean fullyMatched;
        public final List<String> missingIngredients; // human-readable names

        MatchResult(Recipe recipe, boolean fullyMatched, List<String> missingIngredients) {
            this.recipe = recipe;
            this.fullyMatched = fullyMatched;
            this.missingIngredients = missingIngredients;
        }
    }

    /**
     * Builds a lookup of normalised-name -> total quantity-in-base-units
     * available in the pantry. If the same ingredient appears more than
     * once in the pantry (e.g. two separate "egg" entries), quantities
     * are summed.
     */
    private Map<String, Double> buildPantryIndex(List<PantryItem> pantry) {
        Map<String, Double> index = new HashMap<>();
        for (PantryItem item : pantry) {
            String key = IngredientNormalizer.normalizeName(item.getName());
            double baseQty = IngredientNormalizer.toBaseQuantity(item.getQuantity(), item.getUnit());
            index.merge(key, baseQty, Double::sum);
        }
        return index;
    }

    /** Checks one recipe against the pantry index. */
    private MatchResult evaluate(Recipe recipe, Map<String, Double> pantryIndex) {
        List<String> missing = new ArrayList<>();

        for (RecipeIngredient required : recipe.getIngredients()) {
            String key = IngredientNormalizer.normalizeName(required.getName());
            double requiredBaseQty = IngredientNormalizer.toBaseQuantity(required.getQuantity(), required.getUnit());
            Double available = pantryIndex.get(key);

            if (available == null || available < requiredBaseQty) {
                missing.add(required.getName());
            }
        }

        return new MatchResult(recipe, missing.isEmpty(), missing);
    }

    /**
     * Returns only recipes where every ingredient is satisfied
     * (this is the list shown on the "Suggested Recipes" screen).
     */
    public List<Recipe> getStrictMatches(List<Recipe> allRecipes, List<PantryItem> pantry) {
        Map<String, Double> pantryIndex = buildPantryIndex(pantry);
        List<Recipe> matches = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            if (evaluate(recipe, pantryIndex).fullyMatched) {
                matches.add(recipe);
            }
        }
        return matches;
    }

    /**
     * Optional stretch goal (Section 8): recipes missing exactly one
     * ingredient, for a separate "Almost There" list. Kept clearly
     * separate from getStrictMatches() per the brief's requirement.
     */
    public List<MatchResult> getAlmostThereMatches(List<Recipe> allRecipes, List<PantryItem> pantry) {
        Map<String, Double> pantryIndex = buildPantryIndex(pantry);
        List<MatchResult> almost = new ArrayList<>();
        for (Recipe recipe : allRecipes) {
            MatchResult result = evaluate(recipe, pantryIndex);
            if (!result.fullyMatched && result.missingIngredients.size() == 1) {
                almost.add(result);
            }
        }
        return almost;
    }
}
