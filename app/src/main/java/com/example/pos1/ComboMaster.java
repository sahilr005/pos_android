package com.example.pos1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ComboMaster extends AppCompatActivity {
    private TableLayout tableLayout;
    private Button createComboBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_combo_master);
        tableLayout = findViewById(R.id.comboMasterTable);
        createComboBtn = findViewById(R.id.createComboBtn);

        displayTableData();

        createComboBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCreateComboDialog();
            }
        });
    }
    private void createTableHeading() {
        TableRow headingRow = new TableRow(this);
        headingRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        headingRow.setBackgroundResource(R.drawable.table_header_border);

        addHeadingTextView(headingRow, "Category");
        addHeadingTextView(headingRow, "Name");
        addHeadingTextView(headingRow, "Total Cost");
        addHeadingTextView(headingRow, "Type / Discount");
        addHeadingTextView(headingRow, "Combo Cost");
        addHeadingTextView(headingRow, "Status");
        addHeadingTextView(headingRow, "Action");

        tableLayout.addView(headingRow);
    }

    private void addHeadingTextView(TableRow row, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        textView.setTypeface(null, Typeface.BOLD);
        row.addView(textView);
    }

    private void displayTableData() {
        tableLayout.removeAllViews();
        createTableHeading();

        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT combo_mast.combo_id, combo_mast.catid, combo_mast.combo_name, combo_mast.totcost, combo_mast.discount_type, combo_mast.combocost, combo_mast.is_active, category_mast.catname FROM combo_mast INNER JOIN category_mast ON combo_mast.catid = category_mast.catid", null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int combo_id = cursor.getInt(cursor.getColumnIndex("combo_id"));
            @SuppressLint("Range") int catid = cursor.getInt(cursor.getColumnIndex("catid"));
            @SuppressLint("Range") String cname = cursor.getString(cursor.getColumnIndex("catname"));
            @SuppressLint("Range") String comboName = cursor.getString(cursor.getColumnIndex("combo_name"));
            @SuppressLint("Range") double totcost = cursor.getDouble(cursor.getColumnIndex("totcost"));
            @SuppressLint("Range") String discountType = cursor.getString(cursor.getColumnIndex("discount_type"));
            @SuppressLint("Range") double combocost = cursor.getDouble(cursor.getColumnIndex("combocost"));
            @SuppressLint("Range") int isActive = cursor.getInt(cursor.getColumnIndex("is_active"));

            TableRow dataRow = new TableRow(this);
            dataRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            dataRow.setBackgroundResource(R.drawable.border);

            addDataTextView(dataRow, cname);
            addDataTextView(dataRow, comboName);
            addDataTextView(dataRow, String.valueOf(totcost));
            addDataTextView(dataRow, discountType);
            addDataTextView(dataRow, String.valueOf(combocost));
            addDataSwitch(dataRow, isActive == 1, combo_id);
            addEditButton(dataRow, combo_id); // Add the Edit button to the row

            tableLayout.addView(dataRow);
        }

        cursor.close();
        db.close();
    }

    private void addDataTextView(TableRow row, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        row.addView(textView);
    }
    private void addDataSwitch(TableRow row, boolean isChecked,int szid) {
        Switch switchWidget = new Switch(this);
        switchWidget.setChecked(isChecked);
        TableRow.LayoutParams switchLayoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        switchLayoutParams.gravity = Gravity.START;  // Align the Switch to the start (left)
        switchWidget.setLayoutParams(switchLayoutParams);

        switchWidget.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int newIsActive = isChecked ? 1 : 0;
                updateSizeIsActiveValue(szid, newIsActive);
            }
        });
        row.addView(switchWidget);
    }
    private void updateSizeIsActiveValue(int szid, int newIsActive) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put("is_active", newIsActive);

        int rowsAffected = db.update("combo_mast", values, "combo_id=?", new String[]{String.valueOf(szid)});
        if (rowsAffected > 0) {
            Toast.makeText(this, "is_active value updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update is_active value", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    private void showCreateComboDialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_combo, null);
        dialogBuilder.setView(dialogView);

        Spinner discountTypeSpinner = dialogView.findViewById(R.id.discountTypeSpinner);
        EditText discountValueEditText = dialogView.findViewById(R.id.discountValueEditText);

        Spinner categorySpinner = dialogView.findViewById(R.id.categorySpinner);
        EditText comboNameEditText = dialogView.findViewById(R.id.comboNameEditText);
        EditText totCostEditText = dialogView.findViewById(R.id.totCostEditText);

        List<String> categoryList = getCategoryListFromDatabase();

        // Create the ArrayAdapter for the category dropdown
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> discountTypeAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.discount_types,
                android.R.layout.simple_spinner_item
        );
        discountTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        discountTypeSpinner.setAdapter(discountTypeAdapter);

        dialogBuilder.setPositiveButton("Add Combo", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                String comboName = comboNameEditText.getText().toString();
//                double totalCost = Double.parseDouble(totCostEditText.getText().toString());
//                double comboCost = Double.parseDouble(comboCostEditText.getText().toString());

                String selectedDiscountType = discountTypeSpinner.getSelectedItem().toString();
                String discountValue = discountValueEditText.getText().toString();


                double totalCost = Double.parseDouble(totCostEditText.getText().toString());
                double discountAmount = 0.0;

                if (!discountValue.isEmpty()) {
                    if (selectedDiscountType.equals("Percentage")) {
                        double percentage = Double.parseDouble(discountValue);
                        discountAmount = totalCost * (percentage / 100);
                    } else if (selectedDiscountType.equals("Amount")) {
                        discountAmount = Double.parseDouble(discountValue);
                    }
                }

                double comboCost = totalCost - discountAmount;

//                comboCostEditText.setText(String.valueOf(comboCost));
                // Add the data to the combo_mast table
                insertComboIntoDatabase(selectedCategory, comboName, totalCost, selectedDiscountType, comboCost);

                // Refresh the table data
                displayTableData();
            }
        });

        dialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private List<String> getCategoryListFromDatabase() {
        List<String> categoryList = new ArrayList<>();

        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT catname FROM category_mast", null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String categoryName = cursor.getString(cursor.getColumnIndex("catname"));
            categoryList.add(categoryName);
        }

        cursor.close();
        db.close();

        return categoryList;
    }

    private long insertComboIntoDatabase(String categoryName, String comboName, double totCost, String discountType, double comboCost) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put("catid", getCategoryIdByName(categoryName));
        values.put("combo_name", comboName);
        values.put("totcost", totCost);
        values.put("discount_type", discountType);
        values.put("combocost", comboCost);
        values.put("is_active", 1); // Set initial status as active

        long result = db.insert("combo_mast", null, values);
        db.close();

        return result;
    }

    @SuppressLint("Range")
    private int getCategoryIdByName(String categoryName) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        Cursor cursor = db.rawQuery("SELECT catid FROM category_mast WHERE catname=?", new String[]{categoryName});

        int categoryId = -1;

        if (cursor.moveToFirst()) categoryId = cursor.getInt(cursor.getColumnIndex("catid"));

        cursor.close();
        db.close();

        return categoryId;
    }

    private void addEditButton(TableRow row, int comboId) {
        Button editButton = new Button(this);
        editButton.setText("Edit");
        editButton.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditComboDialog(comboId);
            }
        });
        row.addView(editButton);
    }

    private void showEditComboDialog(int comboId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_create_combo, null);
        dialogBuilder.setView(dialogView);

        Spinner discountTypeSpinner = dialogView.findViewById(R.id.discountTypeSpinner);
        EditText discountValueEditText = dialogView.findViewById(R.id.discountValueEditText);

        Spinner categorySpinner = dialogView.findViewById(R.id.categorySpinner);
        EditText comboNameEditText = dialogView.findViewById(R.id.comboNameEditText);
        EditText totCostEditText = dialogView.findViewById(R.id.totCostEditText);

        List<String> categoryList = getCategoryListFromDatabase();

        // Create the ArrayAdapter for the category dropdown
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);

        ArrayAdapter<CharSequence> discountTypeAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.discount_types,
                android.R.layout.simple_spinner_item
        );
        discountTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        discountTypeSpinner.setAdapter(discountTypeAdapter);

        // Fetch the combo data from the database using the comboId
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM combo_mast WHERE combo_id=?", new String[]{String.valueOf(comboId)});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String categoryName = getCategoryNameById(cursor.getInt(cursor.getColumnIndex("catid")));
            @SuppressLint("Range") String comboName = cursor.getString(cursor.getColumnIndex("combo_name"));
            @SuppressLint("Range") double totalCost = cursor.getDouble(cursor.getColumnIndex("totcost"));
            @SuppressLint("Range") String discountType = cursor.getString(cursor.getColumnIndex("discount_type"));
            @SuppressLint("Range") double comboCost = cursor.getDouble(cursor.getColumnIndex("combocost"));

            // Set the retrieved values in the dialog fields
            int categoryPosition = getCategoryPosition(categoryName);
            categorySpinner.setSelection(categoryPosition);

            comboNameEditText.setText(comboName);
            totCostEditText.setText(String.valueOf(totalCost));

            int discountTypePosition = getDiscountTypePosition(discountType);
            discountTypeSpinner.setSelection(discountTypePosition);

            discountValueEditText.setText(getDiscountValue(discountType, totalCost, comboCost));
        }

        cursor.close();
        db.close();

        dialogBuilder.setPositiveButton("Update Combo", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Retrieve the updated values from the dialog fields
                String updatedCategory = categorySpinner.getSelectedItem() != null ? categorySpinner.getSelectedItem().toString() : "";
                String updatedComboName = comboNameEditText.getText().toString();
                String updatedTotalCostStr = totCostEditText.getText().toString();
                String updatedDiscountValue = discountValueEditText.getText().toString();

                // Check if any of the fields are null or empty
                if (updatedCategory.isEmpty() || updatedComboName.isEmpty() || updatedTotalCostStr.isEmpty() || updatedDiscountValue.isEmpty()) {
                    Toast.makeText(ComboMaster.this, "Please fill in all the fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                double updatedTotalCost = Double.parseDouble(updatedTotalCostStr);
                String updatedDiscountType = discountTypeSpinner.getSelectedItem() != null ? discountTypeSpinner.getSelectedItem().toString() : "";

                // Calculate the updated combo cost based on the discount type and value
                double updatedComboCost = calculateUpdatedComboCost(updatedTotalCost, updatedDiscountType, updatedDiscountValue);

                // Update the combo data in the database
                boolean isUpdated = updateComboData(comboId, updatedCategory, updatedComboName, updatedTotalCost, updatedDiscountType, updatedComboCost);

                if (isUpdated) {
                    Toast.makeText(ComboMaster.this, "Combo updated successfully", Toast.LENGTH_SHORT).show();
                    // Refresh the table data
                    displayTableData();
                } else {
                    Toast.makeText(ComboMaster.this, "Failed to update combo", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    @SuppressLint("Range")
    private String getCategoryNameById(int categoryId) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT catname FROM category_mast WHERE catid=?", new String[]{String.valueOf(categoryId)});

        String categoryName = "";

        if (cursor.moveToFirst()) {
            categoryName = cursor.getString(cursor.getColumnIndex("catname"));
        }

        cursor.close();
        db.close();

        return categoryName;
    }

    private int getCategoryPosition(String categoryName) {
        List<String> categoryList = getCategoryListFromDatabase();
        return categoryList.indexOf(categoryName);
    }

    private int getDiscountTypePosition(String discountType) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.discount_types,
                android.R.layout.simple_spinner_item
        );

        return adapter.getPosition(discountType);
    }

    private String getDiscountValue(String discountType, double totalCost, double comboCost) {
        if (discountType.equals("Percentage")) {
            double discountPercentage = ((totalCost - comboCost) / totalCost) * 100;
            return String.valueOf(discountPercentage);
        } else if (discountType.equals("Amount")) {
            double discountAmount = totalCost - comboCost;
            return String.valueOf(discountAmount);
        }

        return "";
    }

    private double calculateUpdatedComboCost(double totalCost, String discountType, String discountValue) {
        double comboCost = totalCost;

        if (!discountValue.isEmpty()) {
            if (discountType.equals("Percentage")) {
                double discountPercentage = Double.parseDouble(discountValue);
                double discountAmount = totalCost * (discountPercentage / 100);
                comboCost = totalCost - discountAmount;
            } else if (discountType.equals("Amount")) {
                double discountAmount = Double.parseDouble(discountValue);
                comboCost = totalCost - discountAmount;
            }
        }

        return comboCost;
    }
    private boolean updateComboData(int comboId, String updatedCategory, String updatedComboName, double updatedTotalCost, String updatedDiscountType, double updatedComboCost) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put("catid", getCategoryIdByName(updatedCategory));
        values.put("combo_name", updatedComboName);
        values.put("totcost", updatedTotalCost);
        values.put("discount_type", updatedDiscountType);
        values.put("combocost", updatedComboCost);

        int rowsAffected = db.update("combo_mast", values, "combo_id=?", new String[]{String.valueOf(comboId)});
        db.close();

        return rowsAffected > 0;
    }

}