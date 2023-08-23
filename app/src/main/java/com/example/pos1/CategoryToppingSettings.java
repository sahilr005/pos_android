package com.example.pos1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CategoryToppingSettings extends AppCompatActivity {

    private TableLayout tableLayoutCategories;
    private SQLiteDatabase database;
    private int selectedCategoryId;
    private TextView lastClickedCategoryTextView;
    ArrayList<Integer> toppingIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_topping_settings);

        tableLayoutCategories = findViewById(R.id.tableLayoutCategories);
        lastClickedCategoryTextView = null;
        database = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        toppingIds = new ArrayList<>();
        displayCategories();
    }

    private void displayCategories() {
        Cursor cursor = database.rawQuery("SELECT catid, catname FROM category_mast", null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int catid = cursor.getInt(cursor.getColumnIndex("catid"));
            @SuppressLint("Range") String catname = cursor.getString(cursor.getColumnIndex("catname"));

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            TextView catnameTextView = new TextView(this);
            catnameTextView.setText(catname);
            catnameTextView.setPadding(16, 16, 16, 16);
            catnameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedCategoryId = catid;

                    if (lastClickedCategoryTextView != null) {
                        lastClickedCategoryTextView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                        lastClickedCategoryTextView.setTextColor(getResources().getColor(R.color.black));
                    }

                    lastClickedCategoryTextView = catnameTextView;
                    lastClickedCategoryTextView.setBackgroundColor(getResources().getColor(R.color.purple_500));
                    lastClickedCategoryTextView.setTextColor(getResources().getColor(R.color.white));
                    updateToppingsColors(catid);

                    displayToppingsForCategory(catid, catname);
                }
            });
            row.addView(catnameTextView);
            tableLayoutCategories.addView(row);
        }

        cursor.close();
    }

    private void displayToppingsForCategory(int catid, String catname) {
        Cursor cursor = database.rawQuery("SELECT tid, tname FROM topping_mast", null);

        ArrayList<String> toppingNames = new ArrayList<>();

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int tid = cursor.getInt(cursor.getColumnIndex("tid"));
            @SuppressLint("Range") String tname = cursor.getString(cursor.getColumnIndex("tname"));

            toppingNames.add(tname);
            toppingIds.add(tid);
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, toppingNames);
        GridView gridView = findViewById(R.id.gridViewToppings);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addToppingToCategory(selectedCategoryId, toppingIds.get(position));
                view.setBackgroundColor(getResources().getColor(R.color.teal));
            }
        });
    }

    private void addToppingToCategory(int catid, int tid) {
        String contains = retrieveContainsCategory(catid);

        if (contains == null) {
            contains = "";
        } else {
            // Remove tid if it already exists in contains
            contains = contains.replace("," + tid, "").replace(tid + ",", "");
        }

        if (!contains.isEmpty()) {
            contains += ",";
        }
        contains += String.valueOf(tid);

        ContentValues values = new ContentValues();
        values.put("contains", contains);

        int rowsAffected = database.update("category_mast", values, "catid=?", new String[]{String.valueOf(catid)});

        if (rowsAffected > 0) {
            Toast.makeText(this, "Topping added to category", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add topping to category", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("Range")
    private String retrieveContainsCategory(int catid) {
        String containsCategory = "";
        Cursor cursor = database.rawQuery("SELECT contains FROM category_mast WHERE catid = ?", new String[]{String.valueOf(catid)});
        if (cursor.moveToFirst()) {
            containsCategory = cursor.getString(cursor.getColumnIndex("contains"));
        }
        cursor.close();
        return containsCategory;
    }
    private void updateToppingsColors(int catid) {
        // Get the list of toppings contained in the selected category
        String contains = retrieveContainsCategory(catid);
        Log.d("contains -- ", "updateToppingsColors: "+contains);
        if (contains != null) {
            String[] containedTids = contains.split(",");

            GridView gridView = findViewById(R.id.gridViewToppings);
            for (int i = 0; i < gridView.getChildCount(); i++) {
                View view = gridView.getChildAt(i);
                TextView toppingTextView = view.findViewById(android.R.id.text1);

                int tid = toppingIds.get(i); // Retrieve the topping ID from the list
                if (contains.contains("," + tid + ",")) {
                    toppingTextView.setBackgroundColor(getResources().getColor(R.color.teal));
                } else {
                    toppingTextView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the database when the activity is destroyed
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}
