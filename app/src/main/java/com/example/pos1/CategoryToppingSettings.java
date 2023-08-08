package com.example.pos1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class CategoryToppingSettings extends AppCompatActivity {

    private TableLayout tableLayoutCategories;
    private TableLayout tableLayoutToppings;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_topping_settings);

        tableLayoutCategories = findViewById(R.id.tableLayoutCategories);
        tableLayoutToppings = findViewById(R.id.tableLayoutToppings);

        database = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

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
                    displayToppingsForCategory(catid, catname);
                }
            });

            row.addView(catnameTextView);
            tableLayoutCategories.addView(row);
        }

        cursor.close();
    }

    private void displayToppingsForCategory(int catid, String catname) {
        // Clear the toppings table layout
        tableLayoutToppings.removeAllViews();

        Cursor cursor = database.rawQuery("SELECT tid, tname FROM topping_mast WHERE catid = ?", new String[]{String.valueOf(catid)});

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int tid = cursor.getInt(cursor.getColumnIndex("tid"));
            @SuppressLint("Range") String tname = cursor.getString(cursor.getColumnIndex("tname"));

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            TextView tnameTextView = new TextView(this);
            tnameTextView.setText(tname);
            tnameTextView.setPadding(16, 16, 16, 16);

            row.addView(tnameTextView);
            tableLayoutToppings.addView(row);
        }

        cursor.close();
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
