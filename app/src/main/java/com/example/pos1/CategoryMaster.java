package com.example.pos1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CategoryMaster extends AppCompatActivity {
    private TableLayout tableLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_master);

        tableLayout = findViewById(R.id.categoryMasterTable);
        displayTableData();
    }

    private void createTableHeading() {
        TableRow headingRow = new TableRow(this);
        headingRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        headingRow.setBackgroundResource(R.drawable.table_header_border);

        addHeadingTextView(headingRow, "Id");
        addHeadingTextView(headingRow, "Name");
        addHeadingTextView(headingRow, "Active/Inactive");
        addHeadingTextView(headingRow, "Is Combo");
        addHeadingTextView(headingRow, "Edit");

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
        Cursor cursor = db.rawQuery("SELECT * FROM category_mast", null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int cid = cursor.getInt(cursor.getColumnIndex("catid"));
            @SuppressLint("Range") String cname = cursor.getString(cursor.getColumnIndex("catname"));
            @SuppressLint("Range") int isActive = cursor.getInt(cursor.getColumnIndex("is_active"));
            @SuppressLint("Range") int isCombo = cursor.getInt(cursor.getColumnIndex("is_combo"));
            @SuppressLint("Range") String uentdt = cursor.getString(cursor.getColumnIndex("uentdt"));

            TableRow dataRow = new TableRow(this);
            dataRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            dataRow.setBackgroundResource(R.drawable.border);

            addDataTextView(dataRow, String.valueOf(cid));
            addDataTextView(dataRow, cname);
            addDataSwitch(dataRow, isActive == 1,cid,false);
            addDataSwitch(dataRow, isCombo == 1,cid,true);
            addEditButton(dataRow,cid);

//            addDataTextView(dataRow, uentdt);

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
    private void addDataSwitch(TableRow row, boolean isChecked,int id,boolean fromCombo) {
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

                if(fromCombo)
                    updateSizeIsComboValue(id,newIsActive);
                else
                    updateSizeIsActiveValue(id, newIsActive);
            }
        });
        row.addView(switchWidget);
    }
    private void addEditButton(TableRow row, int id) {
        Button editButton = new Button(this);
        editButton.setText("Edit");
        editButton.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditCategoryDialog(id);
            }
        });
        row.addView(editButton);
    }

    private void updateSizeIsActiveValue(int id, int newIsActive) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put("is_active", newIsActive);

        int rowsAffected = db.update("category_mast", values, "catid=?", new String[]{String.valueOf(id)});
        if (rowsAffected > 0) {
            Toast.makeText(this, "is_active value updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update is_active value", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
    private void updateSizeIsComboValue(int id, int newIsActive) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put("is_combo", newIsActive);

        int rowsAffected = db.update("category_mast", values, "catid=?", new String[]{String.valueOf(id)});
        if (rowsAffected > 0) {
            Toast.makeText(this, "Combo value updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update Combo value", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    private boolean updateCategoryName(int catId, String updatedCatName) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put("catname", updatedCatName);

        int rowsAffected = db.update("category_mast", values, "catid=?", new String[]{String.valueOf(catId)});
        db.close();

        return rowsAffected > 0;
    }

    private void showEditCategoryDialog(int catId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_topping, null);
        dialogBuilder.setView(dialogView);

        EditText toppingNameEditText = dialogView.findViewById(R.id.toppingNameEditText);

        // Fetch the topping data from the database using the toppingId
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM category_mast WHERE catid=?", new String[]{String.valueOf(catId)});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String toppingName = cursor.getString(cursor.getColumnIndex("catname"));

            // Set the retrieved topping name in the dialog field
            toppingNameEditText.setText(toppingName);
        }

        cursor.close();
        db.close();

        dialogBuilder.setPositiveButton("Update Category", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Retrieve the updated topping name from the dialog field
                String updatedToppingName = toppingNameEditText.getText().toString();

                // Update the topping name in the database
                if (updateCategoryName(catId, updatedToppingName)) {
                    Toast.makeText(CategoryMaster.this, "Topping name updated successfully", Toast.LENGTH_SHORT).show();
                    // Refresh the table data
                    displayTableData();
                } else {
                    Toast.makeText(CategoryMaster.this, "Failed to update topping name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }


}