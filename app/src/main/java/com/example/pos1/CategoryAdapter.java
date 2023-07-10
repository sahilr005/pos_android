package com.example.pos1;

import java.util.List;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private List<Category> categories;

    public CategoryAdapter(List<Category> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            // Set column heading values for the first row
            holder.textCatId.setText("Cat ID");
            holder.textCatName.setText("Category Name");
            holder.is_active.setText("Active/Inactive");
            holder.is_combo.setText("Is Combo");
            holder.actionBtn.setText("Action");

            // Set column heading styles, such as bold or different background color
            // You can modify the TextView properties as needed
        } else {
            Category category = categories.get(position - 1);
            holder.textCatId.setText(String.valueOf(category.getCatId()));
            holder.textCatName.setText(category.getCatName());
            holder.is_active.setText(category.getIs_active() == 1 ? "Publish" : "Unpublish");
            holder.is_combo.setText(category.isIs_combo() ? "Yes" : "No");
            holder.actionBtn.setText("Edit");
        }
    }

    @Override
    public int getItemCount() {
        return categories.size() + 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textCatId;
        public TextView textCatName;
        public TextView is_active;
        public TextView is_combo;
        public AppCompatButton actionBtn;

        // Add other TextViews here

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textCatId = itemView.findViewById(R.id.textCatId);
          textCatName = itemView.findViewById(R.id.textCatName);
            is_active = itemView.findViewById(R.id.is_active);
             is_combo = itemView.findViewById(R.id.is_combo);
            actionBtn = itemView.findViewById(R.id.catActionBtn);
        }
    }
}


