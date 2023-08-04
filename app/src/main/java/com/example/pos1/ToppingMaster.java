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

public class ToppingMaster extends AppCompatActivity {
    private TableLayout tableLayout;
    private Button addToppingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topping_master);
        tableLayout = findViewById(R.id.toppingMasterTable);
        addToppingBtn = findViewById(R.id.addToppingBtn);
        addToppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddToppingDialog();
            }
        });
        createTableHeading();

        displayTableData();
    }
    private void showAddToppingDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_topping, null);
        dialogBuilder.setView(dialogView);

        EditText toppingNameEditText = dialogView.findViewById(R.id.toppingNameEditText);

        dialogBuilder.setPositiveButton("Add Topping", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String toppingName = toppingNameEditText.getText().toString();

                if (!toppingName.isEmpty()) {
                    insertToppingIntoDatabase(toppingName);
                    tableLayout.removeAllViews();
                    createTableHeading();
                    displayTableData(); // Refresh the table data after insertion
                } else {
                    Toast.makeText(ToppingMaster.this, "Topping name cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }
    private void insertToppingIntoDatabase(String toppingName) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put("tname", toppingName);
        values.put("is_active", 1); // Set default values as needed

        long newRowId = db.insert("topping_mast", null, values);

        if (newRowId != -1) {
            Toast.makeText(ToppingMaster.this, "Topping added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ToppingMaster.this, "Failed to add topping", Toast.LENGTH_SHORT).show();
        }

        db.close();
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
        addHeadingTextView(headingRow, "Active");
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
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM topping_mast", null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int tid = cursor.getInt(cursor.getColumnIndex("tid"));
            @SuppressLint("Range") String tname = cursor.getString(cursor.getColumnIndex("tname"));
            @SuppressLint("Range") int isActive = cursor.getInt(cursor.getColumnIndex("is_active"));
            @SuppressLint("Range") String uentdt = cursor.getString(cursor.getColumnIndex("uentdt"));

            TableRow dataRow = new TableRow(this);
            dataRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            dataRow.setBackgroundResource(R.drawable.border);

            addDataTextView(dataRow, String.valueOf(tid));
            addDataTextView(dataRow, tname);
            addDataSwitch(dataRow, isActive == 1,tid);
            addEditButton(dataRow,tid);

            addDataTextView(dataRow, uentdt);

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
                showEditToppingDialog(id);
            }
        });
        row.addView(editButton);
    }

    private void showEditToppingDialog(int toppingId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_topping, null);
        dialogBuilder.setView(dialogView);

        EditText toppingNameEditText = dialogView.findViewById(R.id.toppingNameEditText);

        // Fetch the topping data from the database using the toppingId
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM topping_mast WHERE tid=?", new String[]{String.valueOf(toppingId)});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String toppingName = cursor.getString(cursor.getColumnIndex("tname"));

            // Set the retrieved topping name in the dialog field
            toppingNameEditText.setText(toppingName);
        }

        cursor.close();
        db.close();

        dialogBuilder.setPositiveButton("Update Topping", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Retrieve the updated topping name from the dialog field
                String updatedToppingName = toppingNameEditText.getText().toString();

                // Update the topping name in the database
                if (updateToppingName(toppingId, updatedToppingName)) {
                    Toast.makeText(ToppingMaster.this, "Topping name updated successfully", Toast.LENGTH_SHORT).show();
                    // Refresh the table data
                    tableLayout.removeAllViews();
                    createTableHeading();
                    displayTableData();
                } else {
                    Toast.makeText(ToppingMaster.this, "Failed to update topping name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private boolean updateToppingName(int toppingId, String updatedToppingName) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put("tname", updatedToppingName);

        int rowsAffected = db.update("topping_mast", values, "tid=?", new String[]{String.valueOf(toppingId)});
        db.close();

        return rowsAffected > 0;
    }

    private void updateSizeIsActiveValue(int szid, int newIsActive) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put("is_active", newIsActive);

        int rowsAffected = db.update("topping_mast", values, "tid=?", new String[]{String.valueOf(szid)});
        if (rowsAffected > 0) {
            Toast.makeText(this, "is_active value updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update is_active value", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}