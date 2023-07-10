package com.example.pos1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ToppingMaster extends AppCompatActivity {
    private TableLayout tableLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topping_master);
        tableLayout = findViewById(R.id.toppingMasterTable);
        createTableHeading();

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
        addHeadingTextView(headingRow, "is_active");

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