package com.example.pos1;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashSet;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.CategoryViewHolder>  {

    private Context context;
    private List<CategoryModel> categories;
    private Map<CategoryModel, List<ItemModel>> itemsByCategory;
    private long customerId;

    public CategoryItemAdapter(Context context, List<CategoryModel> categories, Map<CategoryModel, List<ItemModel>> itemsByCategory, long customerId) {
        this.context = context;
        this.categories = categories;
        this.itemsByCategory = itemsByCategory;
        this.customerId = customerId;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_grid_layout, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryModel category = categories.get(position);
        Log.d("CategoryItemAdapter", "onBindViewHolder called for position: " + position);
//        holder.categoryTextView.setText(category.getCatname());
        holder.itemRecyclerView.setVisibility(View.VISIBLE);
        holder.itemRecyclerView.setLayoutManager(new GridLayoutManager(context, 3)); // 3 columns
        CategoryItemInnerAdapter itemAdapter = new CategoryItemInnerAdapter(context, itemsByCategory.get(category),customerId);
        holder.itemRecyclerView.setAdapter(itemAdapter);

    }


    @Override
    public int getItemCount() {
        return categories.size();
    }


    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
//        TextView categoryTextView;
        RecyclerView itemRecyclerView;

        public CategoryViewHolder(View itemView) {
            super(itemView);
//            categoryTextView = itemView.findViewById(R.id.categoryTextView);
            itemRecyclerView = itemView.findViewById(R.id.itemRecyclerView);
        }
    }
}
