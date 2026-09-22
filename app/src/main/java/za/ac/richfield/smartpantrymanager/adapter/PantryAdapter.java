package za.ac.richfield.smartpantrymanager.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import za.ac.richfield.smartpantrymanager.R;
import za.ac.richfield.smartpantrymanager.model.PantryItem;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    public interface OnPantryItemClickListener {
        void onEditClick(PantryItem item);
        void onDeleteClick(PantryItem item);
    }

    private final List<PantryItem> items;
    private final OnPantryItemClickListener listener;

    public PantryAdapter(List<PantryItem> items, OnPantryItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = items.get(position);
        holder.name.setText(item.getName());

        String qtyText = trimTrailingZero(item.getQuantity()) + " " + item.getUnit();
        holder.quantity.setText(qtyText);

        if (item.getExpiryDate() != null && !item.getExpiryDate().isEmpty()) {
            holder.expiry.setVisibility(View.VISIBLE);
            holder.expiry.setText(holder.expiry.getContext()
                    .getString(R.string.expiry_prefix, item.getExpiryDate()));
        } else {
            holder.expiry.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onEditClick(item));
        holder.deleteButton.setOnClickListener(v -> listener.onDeleteClick(item));
    }

    private String trimTrailingZero(double value) {
        if (value == Math.floor(value)) {
            return String.valueOf((long) value);
        }
        return String.valueOf(value);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView name, quantity, expiry;
        View deleteButton;

        PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textPantryName);
            quantity = itemView.findViewById(R.id.textPantryQuantity);
            expiry = itemView.findViewById(R.id.textPantryExpiry);
            deleteButton = itemView.findViewById(R.id.buttonDeletePantryItem);
        }
    }
}
