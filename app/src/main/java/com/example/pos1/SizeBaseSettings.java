package com.example.pos1;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class SizeBaseSettings extends AppCompatActivity {
    private TableLayout tableLayout;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_size_base_settings);

        tableLayout = findViewById(R.id.SizeBaseSettingTable);
        dbHelper = new DatabaseHelper(this);

        tableLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.table_header_border));

        TableRow headingRow = new TableRow(this);
        TableRow.LayoutParams headingLayoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        headingRow.setLayoutParams(headingLayoutParams);

        TextView bidHeadingTextView = new TextView(this);
        bidHeadingTextView.setText("Size");
        bidHeadingTextView.setPadding(16, 16, 16, 16);
        bidHeadingTextView.setTypeface(null, Typeface.BOLD);

        TextView sHeadingTextView = new TextView(this);
        sHeadingTextView.setText("Base");
        sHeadingTextView.setGravity(Gravity.END);
        sHeadingTextView.setPadding(16, 16, 16, 16);
        sHeadingTextView.setTypeface(null, Typeface.BOLD);

        headingRow.addView(bidHeadingTextView);
        headingRow.addView(sHeadingTextView);

        // Add the heading row to the table layout
        tableLayout.addView(headingRow);
        // Fetch the size data from the size_mast table
        List<SizeModel> sizeList = getSizeListFromDatabase();

        // Iterate through each size and display its associated bases
        for (SizeModel size : sizeList) {
            TableRow row = new TableRow(this);
            row.setBackground(ContextCompat.getDrawable(this, R.drawable.border));
            TableRow.LayoutParams dataLayoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            );
            row.setLayoutParams(dataLayoutParams);

            // Create and configure the TextView for the size name
            TextView sizeTextView = new TextView(this);
            sizeTextView.setText(size.getName());
            sizeTextView.setPadding(16, 16, 16, 16);
            row.addView(sizeTextView);


            // Fetch the base data for the current size from the base_mast table
            List<BaseModel> baseList = getBaseListForSizeFromDatabase(size.getId());

            // Iterate through each base and display its name with an on/off switch
            for (BaseModel base : baseList) {
                // Create and configure the Switch for the base
                Switch baseSwitch = new Switch(this);
                baseSwitch.setChecked(base.isActive());
//                baseSwitch.setText(base.getName()+"  ");
                baseSwitch.setGravity(Gravity.END);
                // Add an OnCheckedChangeListener to handle the switch action
                baseSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    // Update the data in size_base_data table based on the switch state
                    // You can access the base ID or other relevant information from the base object
                    if (isChecked) {
                        // Add or update the data in the size_base_data table based on the selected base
                        addToSizeBaseData(size.getId(), base.getId());  // Replace with your logic
                    } else {
                        // Remove the data from the size_base_data table based on the deselected base
                        removeFromSizeBaseData(size.getId(), base.getId()); // Replace with your logic
                    }
                });
                row.addView(baseSwitch);

                // Create and configure the TextView for the base name
                TextView baseTextView = new TextView(this);
                baseTextView.setText(base.getName());
                baseTextView.setPadding(16, 16, 0, 16);
                row.addView(baseTextView);
            }

            // Add the row to the table
            tableLayout.addView(row);
        }
    }

    // Fetch size data from the database (size_mast table)
    private List<SizeModel> getSizeListFromDatabase() {
        List<SizeModel> sizeList = new ArrayList<>();

        // Retrieve the size list from the size_mast table using your database query
        // Replace the following with your database query implementation
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // Replace dbHelper with your actual database helper
        String[] projection = { "szid", "szname" };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;
        Cursor cursor = db.query("size_mast", projection, selection, selectionArgs, null, null, sortOrder);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int sizeId = cursor.getInt(cursor.getColumnIndex("szid"));
                @SuppressLint("Range") String sizeName = cursor.getString(cursor.getColumnIndex("szname"));
                sizeList.add(new SizeModel(sizeId, sizeName));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return sizeList;
    }

    // Fetch base data for a specific size from the database (base_mast table)
    // Fetch base data for a specific size from the database (base_mast table)
    private List<BaseModel> getBaseListForSizeFromDatabase(int sizeId) {
        List<BaseModel> baseList = new ArrayList<>();

        // Retrieve the base list for the given size from the base_mast table using your database query
        // Replace the following with your database query implementation
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // Replace dbHelper with your actual database helper
        String[] projection = { "bid", "bname", "is_active" };
        String sortOrder = null;
        Cursor cursor = db.query("base_mast", projection, null, null, null, null, sortOrder);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int baseId = cursor.getInt(cursor.getColumnIndex("bid"));
                @SuppressLint("Range") String baseName = cursor.getString(cursor.getColumnIndex("bname"));
                boolean isActive = isBaseActive(sizeId, baseId); // Check if the base is active for the given size
                baseList.add(new BaseModel(baseId, baseName, isActive));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return baseList;
    }

    // Check if a base is active for a specific size based on size_base_data table
    private boolean isBaseActive(int sizeId, int baseId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase(); // Replace dbHelper with your actual database helper
        String[] projection = { "sbid" };
        String selection = "catid = ? AND bid = ?";
        String[] selectionArgs = { String.valueOf(sizeId), String.valueOf(baseId) };
        String sortOrder = null;
        Cursor cursor = db.query("size_base_data", projection, selection, selectionArgs, null, null, sortOrder);

        boolean isActive = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return isActive;
    }
    // Add or update data in size_base_data table based on the selected base
    private void addToSizeBaseData(int sizeId, int baseId) {
        // Add or update data in size_base_data table using your database update/insert logic
        // Replace the following with your database update/insert implementation
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Replace dbHelper with your actual database helper

        ContentValues values = new ContentValues();
        values.put("catid", sizeId);
        values.put("bid", baseId);
        // Add more column-value pairs as needed

        long result = db.insertWithOnConflict("size_base_data", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        Log.d("Tab l", "addToSizeBaseData: "+result);
        db.close();

        // Handle the result as needed
    }

    // Remove data from size_base_data table based on the deselected base
    private void removeFromSizeBaseData(int sizeId, int baseId) {
        // Remove data from size_base_data table using your database delete logic
        // Replace the following with your database delete implementation
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Replace dbHelper with your actual database helper

        String selection = "catid = ? AND bid = ?";
        String[] selectionArgs = { String.valueOf(sizeId), String.valueOf(baseId) };

        int result = db.delete("size_base_data", selection, selectionArgs);
        db.close();

        // Handle the result as needed
    }
    public class SizeModel {
        private int id;
        private String name;

        public SizeModel(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

}