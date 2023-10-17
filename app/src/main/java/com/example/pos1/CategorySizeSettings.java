package com.example.pos1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CategorySizeSettings extends AppCompatActivity {
    List<CatData> categoryList; // List of category names from "category_mast" table
    List<SizeData> sizeList;
    TableLayout tableLayout;
    int selectedSzid, selectedCategoryId;
    String selectedSzname, selectedCname;
    Button categorySaveBtn, categoryCancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_size);
        tableLayout = findViewById(R.id.CategorySizeSettingsTable);
        Spinner categorySpinner = findViewById(R.id.categorySpinner);
        Spinner sizeSpinner = findViewById(R.id.sizeSpinner);
        categorySaveBtn = findViewById(R.id.categorySaveBtn);
        categoryCancelBtn = findViewById(R.id.categoryCancelBtn);

        displaySizeBaseTableData();
        // Get the category names from the database
        categoryList = getCategoryData(this);

        // Get the size data from the database
        sizeList = getSizeData(this);

        // Extract only the szname from the sizeList
        List<String> sizeNameList = new ArrayList<>();
        for (SizeData sizeData : sizeList) {
            String szname = sizeData.getSzname();
            sizeNameList.add(szname);
        }

        // Create a custom adapter for the size names
        ArrayAdapter<SizeData> sizeAdapter = new SizeDataAdapter(this, android.R.layout.simple_spinner_item, sizeList);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);

        // Retrieve the selected SizeData object when an item is selected
        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                SizeData selectedSizeData = (SizeData) sizeSpinner.getSelectedItem();
                selectedSzid = selectedSizeData.getSzid();
                selectedSzname = selectedSizeData.getSzname();

                // Use the selected szid and szname as needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected
            }
        });

        // Extract only the catname from the categoryList
        List<String> categoryNameList = new ArrayList<>();
        for (CatData catData : categoryList) {
            String catname = catData.getCatname();
            categoryNameList.add(catname);
        }

        // Create a custom adapter for the category names
        ArrayAdapter<CatData> categoryAdapter = new CategoryDataAdapter(this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        // Retrieve the selected CatData object when an item is selected
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CatData selectedCategoryData = (CatData) categorySpinner.getSelectedItem();
                selectedCategoryId = selectedCategoryData.getCatid();
                selectedCname = selectedCategoryData.getCatname();

                // Use the selected catid and cname as needed
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected
            }
        });

        categorySaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertSizeBaseData(CategorySizeSettings.this, selectedCategoryId, selectedSzid, 1);
                categorySpinner.setSelection(0);
                sizeSpinner.setSelection(0);

                // Example: You can also reset the selected variables
                selectedSzid = 0;
                selectedSzname = "";
                selectedCategoryId = 0;
                selectedCname = "";

                // Display a toast message to indicate cancellation
                Toast.makeText(CategorySizeSettings.this, "Data Added", Toast.LENGTH_SHORT).show();
                displaySizeBaseTableData();
            }
        });
        categoryCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the selections and reset the spinners
                categorySpinner.setSelection(0);
                sizeSpinner.setSelection(0);

                // Example: You can also reset the selected variables
                selectedSzid = 0;
                selectedSzname = "";
                selectedCategoryId = 0;
                selectedCname = "";

                // Display a toast message to indicate cancellation
                Toast.makeText(CategorySizeSettings.this, "Selection canceled!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static List<CatData> getCategoryData(Context context) {
        List<CatData> categoryList = new ArrayList<>();

        SQLiteDatabase db = context.openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        String[] columns = {"catid", "catname"};
        String orderBy = "catid ASC";

        Cursor cursor = db.query("category_mast", columns, null, null, null, null, orderBy);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int catid = cursor.getInt(cursor.getColumnIndex("catid"));
                @SuppressLint("Range") String catname = cursor.getString(cursor.getColumnIndex("catname"));
                CatData categoryData = new CatData(catid, catname);
                categoryList.add(categoryData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return categoryList;
    }

    public static List<SizeData> getSizeData(Context context) {
        List<SizeData> sizeList = new ArrayList<>();

        SQLiteDatabase db = context.openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        String[] columns = {"smzid", "szname"};
        String orderBy = "smzid ASC";

        Cursor cursor = db.query("size_mast", columns, null, null, null, null, orderBy);
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int szid = cursor.getInt(cursor.getColumnIndex("smzid"));
                @SuppressLint("Range") String szname = cursor.getString(cursor.getColumnIndex("szname"));
                SizeData sizeData = new SizeData(szid, szname);
                sizeList.add(sizeData);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return sizeList;
    }

    public static class SizeDataAdapter extends ArrayAdapter<SizeData> {
        public SizeDataAdapter(Context context, int resource, List<SizeData> sizeList) {
            super(context, resource, sizeList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView textView = (TextView) super.getView(position, convertView, parent);
            SizeData sizeData = getItem(position);
            if (sizeData != null) {
                textView.setText(sizeData.getSzname());
            }
            return textView;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
            SizeData sizeData = getItem(position);
            if (sizeData != null) {
                textView.setText(sizeData.getSzname());
            }
            return textView;
        }
    }

    public static class CategoryDataAdapter extends ArrayAdapter<CatData> {
        public CategoryDataAdapter(Context context, int resource, List<CatData> categoryList) {
            super(context, resource, categoryList);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView textView = (TextView) super.getView(position, convertView, parent);
            CatData categoryData = getItem(position);
            if (categoryData != null) {
                textView.setText(categoryData.getCatname());
            }
            return textView;
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            TextView textView = (TextView) super.getDropDownView(position, convertView, parent);
            CatData categoryData = getItem(position);
            if (categoryData != null) {
                textView.setText(categoryData.getCatname());
            }
            return textView;
        }
    }

    private void displaySizeBaseTableData() {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT sbid, category_mast.catname, size_mast.szname FROM size_base_data " +
                "JOIN category_mast ON size_base_data.catid = category_mast.catid " +
                "JOIN size_mast ON size_base_data.szid = size_mast.smzid", null);

        if (tableLayout.getChildCount() > 0) {
            tableLayout.removeAllViews();
        }

        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(Color.LTGRAY);

        headerRow.addView(createTableCell("Id", true));
        headerRow.addView(createTableCell("Category", true));
        headerRow.addView(createTableCell("Size", true));
        headerRow.addView(createTableCell("Action", true));

        tableLayout.addView(headerRow);

        while (cursor.moveToNext()) {
            TableRow dataRow = new TableRow(this);

            if (cursor.getPosition() % 2 == 0) {
                dataRow.setBackgroundColor(Color.WHITE);
            } else {
                dataRow.setBackgroundColor(Color.parseColor("#F2F2F2"));
            }

            @SuppressLint("Range") final int itemId = cursor.getInt(cursor.getColumnIndex("sbid"));
            @SuppressLint("Range") String column1Value = cursor.getString(cursor.getColumnIndex("catname"));
            @SuppressLint("Range") String column2Value = cursor.getString(cursor.getColumnIndex("szname"));
            AppCompatButton column7Button = createTableCellButton("Edit", false);

            column7Button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Retrieve the selected sbid value
                    final int sbid = itemId;

                    // Retrieve the selected category ID and size ID from the clicked row
                    int selectedCategoryId = getCategoryFromTableData(sbid);
                    int selectedSizeId = getSizeFromTableData(sbid);

                    // Inflate the edit layout
                    View editView = LayoutInflater.from(CategorySizeSettings.this).inflate(R.layout.activity_category_size, null);

                    // Get references to the views in the edit layout
                    Spinner categorySpinner = editView.findViewById(R.id.categorySpinner);
                    Spinner sizeSpinner = editView.findViewById(R.id.sizeSpinner);
                    Button saveButton = editView.findViewById(R.id.categorySaveBtn);
                    Button cancelButton = editView.findViewById(R.id.categoryCancelBtn);

                    // Create the ArrayAdapter for the category names
                    ArrayAdapter<CatData> categoryAdapter = new CategoryDataAdapter(CategorySizeSettings.this, android.R.layout.simple_spinner_item, categoryList);
                    categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    categorySpinner.setAdapter(categoryAdapter);

                    // Create the ArrayAdapter for the size names
                    ArrayAdapter<SizeData> sizeAdapter = new SizeDataAdapter(CategorySizeSettings.this, android.R.layout.simple_spinner_item, sizeList);
                    sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    sizeSpinner.setAdapter(sizeAdapter);

                    // Find the position of the selected category in the spinner
                    int categoryPosition = getCategoryPosition(selectedCategoryId);
                    if (categoryPosition != -1) {
                        categorySpinner.setSelection(categoryPosition);
                    }

                    // Find the position of the selected size in the spinner
                    int sizePosition = getSizePosition(selectedSizeId);
                    if (sizePosition != -1) {
                        sizeSpinner.setSelection(sizePosition);
                    }

                    // Create the AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(CategorySizeSettings.this);
                    builder.setView(editView);
                    builder.setTitle("Edit Size Base Data");

                    // Handle the save button click inside the AlertDialog
                    builder.setPositiveButton(null, null);

                    // Handle the cancel button click inside the AlertDialog
                    builder.setNegativeButton(null, null);

                    final AlertDialog dialog = builder.create();
                    dialog.show();

                    // Override the positive button click listener to handle the save button click
                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Retrieve the selected category and size from the spinners
                            CatData selectedCategoryData = (CatData) categorySpinner.getSelectedItem();
                            SizeData selectedSizeData = (SizeData) sizeSpinner.getSelectedItem();

                            // Update the corresponding record in the size_base_data table based on sbid
                            updateSizeBaseData(sbid, selectedCategoryData.getCatid(), selectedSizeData.getSzid());

                            // Dismiss the dialog
                            dialog.dismiss();
                        }
                    });

                    cancelButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                }
            });


            dataRow.addView(createTableCell(String.valueOf(itemId), false));
            dataRow.addView(createTableCell(column1Value, false));
            dataRow.addView(createTableCell(column2Value, false));
            dataRow.addView(column7Button);

            tableLayout.addView(dataRow);
        }

        cursor.close();
        db.close();
    }

    private AppCompatButton createTableCell(String text, boolean isHeaderCell) {
        AppCompatButton cell = new AppCompatButton(this);
        cell.setText(text);
        cell.setPadding(16, 16, 16, 16);
        cell.setGravity(Gravity.CENTER);

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

        cell.setBackgroundResource(R.drawable.table_header_border);

        return cell;
    }

    private AppCompatButton createTableCellButton(String text, boolean isHeaderCell) {
        AppCompatButton cell = new AppCompatButton(this, null);
        cell.setText(text);
        return cell;
    }

    public static void insertSizeBaseData(Context context, int catid, int szid, int bid) {
        SQLiteDatabase db = context.openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put("catid", catid);
        values.put("szid", szid);
        values.put("bid", bid);

        db.insert("size_base_data", null, values);

        db.close();
    }
    private int getCategoryPosition(int categoryId) {
        for (int i = 0; i < categoryList.size(); i++) {
            CatData categoryData = categoryList.get(i);
            if (categoryData.getCatid() == categoryId) {
                return i;
            }
        }
        return -1;
    }

    private int getSizePosition(int sizeId) {
        for (int i = 0; i < sizeList.size(); i++) {
            SizeData sizeData = sizeList.get(i);
            if (sizeData.getSzid() == sizeId) {
                return i;
            }
        }
        return -1;
    }

    private void updateSizeBaseData(int selectedItemId,int categoryId, int sizeId) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put("catid", categoryId);
        values.put("szid", sizeId);

        int rowsAffected = db.update("size_base_data", values, "sbid=?", new String[]{String.valueOf(selectedItemId)});
        if (rowsAffected > 0) {
            Toast.makeText(CategorySizeSettings.this, "Data updated successfully", Toast.LENGTH_SHORT).show();
            displaySizeBaseTableData();
        } else {
            Toast.makeText(CategorySizeSettings.this, "Failed to update data", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    @SuppressLint("Range")
    private int getCategoryFromTableData(int sbid) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        int categoryId = 0;

        Cursor cursor = db.rawQuery("SELECT catid FROM size_base_data WHERE sbid = ?", new String[]{String.valueOf(sbid)});
        if (cursor.moveToFirst()) {
            categoryId = cursor.getInt(cursor.getColumnIndex("catid"));
        }
        cursor.close();
        db.close();

        return categoryId;
    }

    @SuppressLint("Range")
    private int getSizeFromTableData(int sbid) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        int sizeId = 0;

        Cursor cursor = db.rawQuery("SELECT szid FROM size_base_data WHERE sbid = ?", new String[]{String.valueOf(sbid)});
        if (cursor.moveToFirst()) {
            sizeId = cursor.getInt(cursor.getColumnIndex("szid"));
        }
        cursor.close();
        db.close();

        return sizeId;
    }

}

class SizeData {
    private int szid;
    private String szname;

    public SizeData(int szid, String szname) {
        this.szid = szid;
        this.szname = szname;
    }

    public int getSzid() {
        return szid;
    }

    public String getSzname() {
        return szname;
    }
}
class CatData {
    private int catid;
    private String catname;

    public CatData(int catid, String catname) {
        this.catid = catid;
        this.catname = catname;
    }

    public int getCatid() {
        return catid;
    }

    public String getCatname() {
        return catname;
    }
}
