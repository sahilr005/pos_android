package com.example.pos1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class CategoryMaster extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    private DatabaseHelper databaseHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_master);

        recyclerView = findViewById(R.id.categoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList);
        recyclerView.setAdapter(categoryAdapter);

        databaseHelper = new DatabaseHelper(this);
        loadCategories();
    }
    private void loadCategories() {
        categoryList.clear();
        categoryList.addAll(databaseHelper.getAllCategories());
        categoryAdapter.notifyDataSetChanged();
    }
}