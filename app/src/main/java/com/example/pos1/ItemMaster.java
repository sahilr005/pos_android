package com.example.pos1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class ItemMaster extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_master);

        EditText editTextSearch = findViewById(R.id.editTextSearch);
        Button buttonSearch = findViewById(R.id.buttonSearch);
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String searchQuery = editTextSearch.getText().toString();
                displayItemMasterData(searchQuery);
            }
        });
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchQuery = editTextSearch.getText().toString();
                displayItemMasterData(searchQuery);
            }
        });
        displayItemMasterData("");
    }

    private void displayItemMasterData(String searchQuery) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        // Query the item_master table to retrieve all rows
        Cursor cursor;
        if(searchQuery.isEmpty()){
            cursor = db.rawQuery("SELECT * FROM item_master", null);}
        else {
            cursor = db.rawQuery("SELECT * FROM item_master WHERE name LIKE ?", new String[]{"%" + searchQuery + "%"});
        }

        TableLayout tableLayout = findViewById(R.id.ItemUploadTableLayout);

        // Clear existing table rows
        if (tableLayout.getChildCount() > 0) {
            tableLayout.removeAllViews();
        }

        // Create table header row
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(Color.LTGRAY);  // Set header row background color

        // Add header cells with borders
        headerRow.addView(createTableCell("Category", true));
        headerRow.addView(createTableCell("Item Name", true));
        headerRow.addView(createTableCell("PICKUP PRICE",true));
        headerRow.addView(createTableCell("DELIVERY PRICE",true));
        headerRow.addView(createTableCell("EAT IN PRICE",true));
        headerRow.addView(createTableCell("STATUS",true));
        headerRow.addView(createTableCell("ACTION",true));
        // ...

        // Add the header row to the table
        tableLayout.addView(headerRow);

        // Iterate through the cursor to populate the table with data rows
        while (cursor.moveToNext()) {
            TableRow dataRow = new TableRow(this);

            // Alternate row background color for better readability
            if (cursor.getPosition() % 2 == 0) {
                dataRow.setBackgroundColor(Color.WHITE);
            } else {
                dataRow.setBackgroundColor(Color.parseColor("#F2F2F2"));
            }
            // Extract data from the cursor for each column
            @SuppressLint("Range") final int itemId = cursor.getInt(cursor.getColumnIndex("tid"));
            @SuppressLint("Range") String column1Value = cursor.getString(cursor.getColumnIndex("code"));
            @SuppressLint("Range") String column2Value = cursor.getString(cursor.getColumnIndex("name"));
            @SuppressLint("Range") String column3Value = cursor.getString(cursor.getColumnIndex("pickup_price"));
            @SuppressLint("Range") String column4Value = cursor.getString(cursor.getColumnIndex("delivery_price"));
            @SuppressLint("Range") String column5Value = cursor.getString(cursor.getColumnIndex("eat_in_price"));
            @SuppressLint("Range") String column6Value = cursor.getString(cursor.getColumnIndex("status")).equals("1")?"Active":"Inactive";
            AppCompatButton column7Button = createTableCellButton("Edit", false);


            column7Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ItemMaster.this, EditItems.class);
                    intent.putExtra("itemId", itemId);
                    startActivity(intent);
                }
            });
            // Create data cells with borders and add them to the data row
            dataRow.addView(createTableCell(column1Value, false));
            dataRow.addView(createTableCell(column2Value, false));
            dataRow.addView(createTableCell(column3Value,false));
            dataRow.addView(createTableCell(column4Value,false));
            dataRow.addView(createTableCell(column5Value,false));
            dataRow.addView(createTableCell(column6Value,false));
            dataRow.addView(column7Button);
            // ...

            // Add the data row to the table
            tableLayout.addView(dataRow);
        }

        // Close the cursor and database
        cursor.close();
        db.close();
    }

    // Helper method to create a TextView as a table cell
    private AppCompatButton createTableCell(String text, boolean isHeaderCell) {
        AppCompatButton cell = new AppCompatButton(this);
        // Set cell properties
        cell.setText(text);
        cell.setPadding(16, 16, 16, 16);
        cell.setGravity(Gravity.CENTER);

        // Set cell background color and border
        if (isHeaderCell) {
            cell.setBackgroundColor(Color.parseColor("#CCCCCC"));
            cell.setTextColor(Color.BLACK);
            cell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            cell.setTypeface(null, Typeface.BOLD);
        } else {
            cell.setBackgroundColor(Color.WHITE);
            cell.setTextColor(Color.BLACK);
            cell.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }

        // Set cell borders

        cell.setBackgroundResource(R.drawable.table_header_border); // Create a cell_border.xml drawable in your drawable folder

        return cell;
    }
    private AppCompatButton createTableCellButton(String text, boolean isHeaderCell) {
        AppCompatButton cell = new AppCompatButton(this, null);
        cell.setText(text);
        return cell;
    }


}