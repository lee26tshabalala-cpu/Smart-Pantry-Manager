package za.ac.richfield.smartpantrymanager.model;

/**
 * Represents a single ingredient the user currently has at home.
 * Quantity + unit are kept separate so we can normalise for matching
 * (e.g. 2 "cups" of rice vs 500 "g" of rice).
 */
public class PantryItem {

    private long id;
    private String name;
    private double quantity;
    private String unit;       // e.g. "g", "ml", "pcs" — free text, normalised at match time
    private String expiryDate; // ISO format "yyyy-MM-dd", nullable

    public PantryItem() {
    }

    public PantryItem(long id, String name, double quantity, String unit, String expiryDate) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.expiryDate = expiryDate;
    }

    public PantryItem(String name, double quantity, String unit, String expiryDate) {
        this(-1, name, quantity, unit, expiryDate);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
}
