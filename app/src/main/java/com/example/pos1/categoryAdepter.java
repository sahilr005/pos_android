package com.example.pos1;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class categoryAdepter extends BaseAdapter {
    private Context context;
    private List<CategoryModel> categoryList;
    private RecyclerView recyclerView; // Add a reference to the RecyclerView

    public categoryAdepter(Context context, List<CategoryModel> categoryList, RecyclerView recyclerView) {
        this.context = context;
        this.categoryList = categoryList;
        this.recyclerView = recyclerView;
    }

    @Override
    public int getCount() {
        return categoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return categoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return categoryList.get(position).getCatid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView catNameTextView;

        if (convertView == null) {
            // If convertView is null, create a new TextView
            catNameTextView = new TextView(context);
            catNameTextView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            ));
            catNameTextView.setTextColor(Color.WHITE);
            catNameTextView.setBackgroundResource(R.drawable.red_button_shap);
            catNameTextView.setPadding(8, 8, 8, 8);
            catNameTextView.setTextSize(16);

            // Add a click listener to each category item
            catNameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Scroll to the clicked category in the RecyclerView
                    int categoryPosition = position;
                    scrollToCategory(categoryPosition);
                }
            });
        } else {
            // If convertView is not null, reuse it
            catNameTextView = (TextView) convertView;
        }

        CategoryModel category = categoryList.get(position);
        catNameTextView.setText(category.getCatname());

        return catNameTextView;
    }

    private void scrollToCategory(int targetCategoryPosition) {
        // Check if the target position is valid
        if (targetCategoryPosition >= 0 && targetCategoryPosition < categoryList.size()) {
            // Scroll to the desired category position
            recyclerView.scrollToPosition(targetCategoryPosition);
        }
    }
}
