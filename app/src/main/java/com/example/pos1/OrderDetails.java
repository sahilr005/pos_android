package com.example.pos1;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {

    DatabaseHelper dbHelper;
    RecyclerView recyclerView;
    Button orderClearbtn;
    long customerId;
    LinearLayout orderItemCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);
        dbHelper = new DatabaseHelper(this);
        customerId = getIntent().getLongExtra("customerId", -1);
        recyclerView = findViewById(R.id.categoryRecyclerView);
        orderItemCard = findViewById(R.id.orderItemCard);
        orderClearbtn = findViewById(R.id.orderClearbtn);

        List<CategoryModel> categoryList = fetchCategoriesFromDatabase();
        List<ItemModel> itemList = fetchItemsFromDatabase();
        orderClearbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearOrderData();
            }
        });
        List<List<Object>> itemsByCategory = new ArrayList<>();
        for (CategoryModel category : categoryList) {
            List<Object> categoryItems = new ArrayList<>();
            categoryItems.add(category);
            for (ItemModel item : itemList) {
                if (item.getCatId().equals(String.valueOf(category.getCatid()))) {
                    categoryItems.add(item);
                }
            }
            itemsByCategory.add(categoryItems);
        }

        Map<CategoryModel, List<ItemModel>> itemsByCategoryMap = new HashMap<>();
        for (List<Object> categoryItems : itemsByCategory) {
            if (!categoryItems.isEmpty() && categoryItems.get(0) instanceof CategoryModel) {
                CategoryModel category = (CategoryModel) categoryItems.get(0);
                List<ItemModel> itemsWithinCategory = new ArrayList<>();
                for (int i = 1; i < categoryItems.size(); i++) {
                    if (categoryItems.get(i) instanceof ItemModel) {
                        itemsWithinCategory.add((ItemModel) categoryItems.get(i));
                    }
                }
                itemsByCategoryMap.put(category, itemsWithinCategory);
            }
        }


        CategoryItemAdapter categoryItemAdapter = new CategoryItemAdapter(this, categoryList, itemsByCategoryMap, customerId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(categoryItemAdapter);

        GridView bottomCategoryGridView = findViewById(R.id.counterItemCatGrid);

        categoryAdepter categoryAdapter = new categoryAdepter(this, categoryList, recyclerView);
        bottomCategoryGridView.setAdapter(categoryAdapter);

        // Fetch and display data from tmp_order_product_data
        fetchAndDisplayOrderItems(customerId);
    }
    private void clearOrderData() {
        // Assuming you have a DatabaseHelper instance
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Assuming you have a SQLiteDatabase instance
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            String deleteQuery = "DELETE FROM tmp_order_product_data WHERE custid = " + customerId;
            db.execSQL(deleteQuery);
            orderItemCard.removeAllViews();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }

    @SuppressLint("Range")
    private void fetchAndDisplayOrderItems(long customerId) {
        // Assuming you have a DatabaseHelper instance
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Create a SQL query to retrieve data based on the customerId
        String query = "SELECT itemname, qty, totprice FROM tmp_order_product_data WHERE custid = " + customerId;

        // Assuming you have a SQLiteDatabase instance
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);

            // Iterate through the cursor to retrieve and display order items
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Retrieve data from the cursor
                    String itemname = cursor.getString(cursor.getColumnIndex("itemname"));
                    double qty = cursor.getDouble(cursor.getColumnIndex("qty"));
                    double totprice = cursor.getDouble(cursor.getColumnIndex("totprice"));

                    // Create TextViews to display the item details
                    TextView itemNameTextView = new TextView(this);
                    itemNameTextView.setText(itemname);

                    TextView qtyTextView = new TextView(this);
                    qtyTextView.setText(String.valueOf(qty));

                    TextView amountTextView = new TextView(this);
                    amountTextView.setText(String.valueOf(totprice));

                    // Create a LinearLayout to hold the TextViews horizontally
                    LinearLayout itemDetailsLayout = new LinearLayout(this);
                    itemDetailsLayout.setOrientation(LinearLayout.HORIZONTAL);

                    // Add TextViews to the LinearLayout
                    itemDetailsLayout.addView(itemNameTextView);
                    itemDetailsLayout.addView(qtyTextView);
                    itemDetailsLayout.addView(amountTextView);

                    // Add the itemDetailsLayout to the orderItemCard
                    orderItemCard.addView(itemDetailsLayout);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }
    }
    @SuppressLint("Range")
    private List<CategoryModel> fetchCategoriesFromDatabase() {
        List<CategoryModel> categoryList = new ArrayList<>();

        // Assuming you have a DatabaseHelper instance
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Replace "your_category_table_name" with the actual table name
        String query = "SELECT catid, catname FROM category_mast";

        // Assuming you have a SQLiteDatabase instance
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = null;

        try {
            cursor = db.rawQuery(query, null);

            // Iterate through the cursor to retrieve category data
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int catid = cursor.getInt(cursor.getColumnIndex("catid"));
                    String catname = cursor.getString(cursor.getColumnIndex("catname"));

                    // Create a CategoryModel object and add it to the list
                    CategoryModel category = new CategoryModel(catid, catname);
                    categoryList.add(category);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        // Close the database connection
        db.close();

        return categoryList;
    }

    @SuppressLint("Range")
    private List<ItemModel> fetchItemsFromDatabase() {
        List<ItemModel> itemList = new ArrayList<>();

        String query = "SELECT item_master.tid, item_master.name, item_master.pickup_price, item_master.cat_id, category_mast.catname " +
                "FROM item_master " +
                "INNER JOIN category_mast ON item_master.cat_id = category_mast.catid";

        // Assuming you have a SQLiteDatabase instance
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(query, null);
            // Iterate through the cursor to retrieve item data
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    ItemModel item = new ItemModel();
                    item.setTid(cursor.getInt(cursor.getColumnIndex("tid")));
                    item.setName(cursor.getString(cursor.getColumnIndex("name")));
                    item.setPickupPrice(String.valueOf(cursor.getDouble(cursor.getColumnIndex("pickup_price"))));
                    item.setEatInPrice(String.valueOf((cursor.getColumnIndex("eat_in_price"))));
                    item.setCatId(String.valueOf(cursor.getInt(cursor.getColumnIndex("cat_id"))));
                    item.setCode(cursor.getString(cursor.getColumnIndex("catname")));
                    itemList.add(item);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        db.close();
        return itemList;
    }

}
