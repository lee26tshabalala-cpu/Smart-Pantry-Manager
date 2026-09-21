# Smart Pantry Manager

A Java Android application that helps reduce food waste by tracking the
ingredients a user actually has at home and suggesting **only** recipes
they can make right now — no shopping trip required. A recipe is only
suggested if every one of its ingredients is present in the pantry, in
at least the required quantity (see `logic/RecipeMatcher.java`).

## Why SQLite

This project uses **SQLite** (via `SQLiteOpenHelper`) for local, on-device
persistence. It was chosen because:
- No network/account dependency — the app works fully offline, matching the
  "no shopping trip required" spirit of the concept.
- Full control over the relational schema (`pantry_items`, `recipes`,
  `recipe_ingredients`), which suits the recipe-ingredient one-to-many
  relationship needed for strict matching.
- Directly consistent with the module's persistent-data-storage chapter.

## Screens

1. **Pantry List** (`PantryListActivity`) — launcher screen, shows all pantry items via RecyclerView.
2. **Add/Edit Ingredient** (`AddEditIngredientActivity`) — Create and Update half of pantry CRUD, with input validation.
3. **Suggested Recipes** (`SuggestedRecipesActivity`) — runs the strict-matching algorithm and lists qualifying recipes.
4. **Recipe Detail** (`RecipeDetailActivity`) — full ingredient list and method for a chosen recipe.
5. **Settings** (`SettingsActivity`) — expiring-soon alert toggle and unit-system preference.

## Setup / Run Instructions

1. Clone this repository.
2. Open the project root folder in Android Studio (Hedgehog or later recommended).
3. Let Gradle sync (requires an internet connection the first time, to fetch dependencies).
4. Run on an emulator (API 24+) or a physical device via **Run ▶**.
5. The database is seeded automatically with 18 recipes on first launch — no manual setup needed.

## Project Structure

```
app/src/main/java/za/ac/richfield/smartpantrymanager/
  model/    - PantryItem, Recipe, RecipeIngredient (plain data classes)
  db/       - DatabaseHelper (SQLite schema + CRUD), RecipeSeeder (seed data)
  logic/    - RecipeMatcher (strict-matching algorithm), IngredientNormalizer
  adapter/  - PantryAdapter, RecipeAdapter (RecyclerView adapters)
  ui/       - the five Activities
```

## Out of Scope

Per the assignment brief, this app does **not** use Google Maps, any mapping
SDK, or device GPS/location services.
