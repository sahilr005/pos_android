package com.example.pos1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CategoryItemInnerAdapter extends RecyclerView.Adapter<CategoryItemInnerAdapter.ItemViewHolder> {

    private Context context;
    private List<ItemModel> items;
    private long customerId;


    public CategoryItemInnerAdapter(Context context, List<ItemModel> items,long customerId) {
        this.context = context;
        this.items = items;
        this.customerId = customerId;

    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemModel item = items.get(position);
        // Check if this item should show the GridView
        holder.itemNameTextView.setText(item.getName());
        holder.itemView.setOnClickListener(v -> {
            Log.d("CategoryItemAdapter", "Item clicked ID: " + item.getTid());
//            Log.d("pickupPrice", "setPickupPrice: "+item.getEatInPrice());
            addSelectedItemToTmpOrderProductData(item);
            notifyDataSetChanged();
        });
//
    }


    private void addSelectedItemToTmpOrderProductData(ItemModel item) {
        // Assuming you have a DatabaseHelper instance
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("custid", customerId); // Replace with the appropriate customer ID
        values.put("catid", item.getCatId());
        values.put("catname", item.getCode()); // Replace with the appropriate category name
        values.put("itemid", item.getTid());
        values.put("itemname", item.getName());
        values.put("itemprice", item.getPickupPrice());
        values.put("qty", 1);

        long newRowId = db.insert("tmp_order_product_data", null, values);

        if (newRowId != -1) {
            // Item added successfully
            Log.d("CategoryItemAdapter", "Item added to tmp_order_product_data: " + newRowId);
        } else {
            // Failed to add the item
            Log.e("CategoryItemAdapter", "Failed to add item to tmp_order_product_data");
        }

        db.close();
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemNameTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemNameTextView = itemView.findViewById(R.id.itemNameTextView);

        }
    }

}

