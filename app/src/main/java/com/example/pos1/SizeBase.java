package com.example.pos1;

import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.TableRow.LayoutParams;

import android.widget.CompoundButton;
import android.widget.Switch;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.chip.Chip;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class SizeBase extends AppCompatActivity {

    private TableLayout tableLayout,baseTableLayout;
    private EditText sizeEditTextSize,baseEditTextSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_size_base);

        tableLayout = findViewById(R.id.SizeTableLayout);
        baseTableLayout = findViewById(R.id.BaseTableLayout);
        sizeEditTextSize = findViewById(R.id.SizeEditTextSize);
        baseEditTextSize = findViewById(R.id.BaseeditTextSize);
        Button sizeSaveBtn = findViewById(R.id.SizeSaveBtn);
        Button SizeCancelBtn = findViewById(R.id.SizeCancelBtn);
        Button BaseSaveBtn = findViewById(R.id.BaseSaveBtn);
        Button BaseCancelBtn = findViewById(R.id.BaseCancelBtn);

        createTableHeading();

        displayTableData();
        displayBaseTableData();

        sizeSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String size = sizeEditTextSize.getText().toString();
                sizeEditTextSize.setText("");
                insertDataIntoSizeMast(size, 1, 0, LocalDate.now().toString());
                if (tableLayout.getChildCount() > 0) {
                    tableLayout.removeAllViews();
                }
                displayTableData();
            }
        });

        SizeCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sizeEditTextSize.setText("");
            }
        });

        BaseSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String size = baseEditTextSize.getText().toString();
                baseEditTextSize.setText("");
                insertDataIntoBaseMast(size, 1, 0, LocalDate.now().toString());
                displayBaseTableData();
            }
        });

        BaseCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                baseEditTextSize.setText("");
            }
        });
    }

    private void createTableHeading() {
        TableRow headingRow = new TableRow(this);
        headingRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));

        addHeadingTextView(headingRow, "szid");
        addHeadingTextView(headingRow, "szname");
        addHeadingTextView(headingRow, "cat_id");
        addHeadingTextView(headingRow, "is_active");
        addHeadingTextView(headingRow, "uentdt");

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
        Cursor cursor = db.rawQuery("SELECT * FROM size_mast", null);

        while (cursor.moveToNext()) {
            int szid = cursor.getInt(cursor.getColumnIndex("szid"));
            String szname = cursor.getString(cursor.getColumnIndex("szname"));
            int catId = cursor.getInt(cursor.getColumnIndex("cat_id"));
            int isActive = cursor.getInt(cursor.getColumnIndex("is_active"));
            String uentdt = cursor.getString(cursor.getColumnIndex("uentdt"));

            TableRow dataRow = new TableRow(this);
            dataRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            addDataTextView(dataRow, String.valueOf(szid));
            addDataTextView(dataRow, szname);
            addDataTextView(dataRow, String.valueOf(catId));
            addDataSwitch(dataRow, isActive == 1,szid);

            addDataTextView(dataRow, uentdt);

            tableLayout.addView(dataRow);
        }

        cursor.close();
        db.close();
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


    private void addDataTextView(TableRow row, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        row.addView(textView);
    }

    private void insertDataIntoSizeMast(String szname, int isActive, int serverCheck, String uentdt) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("szname", szname);
        values.put("is_active", isActive);
        values.put("server_check", serverCheck);
        values.put("uentdt", uentdt);

        long rowId = db.insert("size_mast", null, values);
        if (rowId != -1) {
            Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to insert data", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
    private void insertDataIntoBaseMast(String bname, int isActive, int serverCheck, String uentdt) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("bname", bname);
        values.put("is_active", isActive);
        values.put("server_check", serverCheck);
        values.put("uentdt", uentdt);

        long rowId = db.insert("base_mast", null, values);
        if (rowId != -1) {
            Toast.makeText(this, "Data inserted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to insert data", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

    private void displayBaseTableData() {

        // Clear existing table rows if any
        baseTableLayout.removeAllViews();

        // Create a row for the table headings
        TableRow headingRow = new TableRow(this);
        TableRow.LayoutParams headingLayoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        headingRow.setLayoutParams(headingLayoutParams);

        // Create TextViews for the table headings and set their properties
        TextView bidHeadingTextView = new TextView(this);
        bidHeadingTextView.setText("bid");
        bidHeadingTextView.setPadding(16, 16, 16, 16);
        bidHeadingTextView.setTypeface(null, Typeface.BOLD);

        TextView bnameHeadingTextView = new TextView(this);
        bnameHeadingTextView.setText("bname");
        bnameHeadingTextView.setPadding(16, 16, 16, 16);
        bnameHeadingTextView.setTypeface(null, Typeface.BOLD);

        TextView isActiveHeadingTextView = new TextView(this);
        isActiveHeadingTextView.setText("is_active");
        isActiveHeadingTextView.setPadding(16, 16, 16, 16);
        isActiveHeadingTextView.setTypeface(null, Typeface.BOLD);


        TextView uentdtHeadingTextView = new TextView(this);
        uentdtHeadingTextView.setText("uentdt");
        uentdtHeadingTextView.setPadding(16, 16, 16, 16);
        uentdtHeadingTextView.setTypeface(null, Typeface.BOLD);

        // Add the heading TextViews to the heading row
        headingRow.addView(bidHeadingTextView);
        headingRow.addView(bnameHeadingTextView);
        headingRow.addView(isActiveHeadingTextView);
        headingRow.addView(uentdtHeadingTextView);

        // Add the heading row to the table layout
        baseTableLayout.addView(headingRow);

        // Fetch the data from the base_mast table and populate the table layout
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM base_mast", null);

        while (cursor.moveToNext()) {
            int bid = cursor.getInt(cursor.getColumnIndex("bid"));
            String bname = cursor.getString(cursor.getColumnIndex("bname"));
            int isActive = cursor.getInt(cursor.getColumnIndex("is_active"));
            int serverCheck = cursor.getInt(cursor.getColumnIndex("server_check"));
            String uentdt = cursor.getString(cursor.getColumnIndex("uentdt"));

            // Create a row for the table data
            TableRow dataRow = new TableRow(this);
            TableRow.LayoutParams dataLayoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            );
            dataRow.setLayoutParams(dataLayoutParams);

            // Create TextViews for the table data and set their properties
            TextView bidTextView = new TextView(this);
            bidTextView.setText(String.valueOf(bid));
            bidTextView.setPadding(16, 16, 16, 16);

            TextView bnameTextView = new TextView(this);
            bnameTextView.setText(bname);
            bnameTextView.setPadding(16, 16, 16, 16);

            TextView uentdtTextView = new TextView(this);
            uentdtTextView.setText(uentdt);
            uentdtTextView.setPadding(16, 16, 16, 16);


            Switch isActiveSwitch = new Switch(this);
            isActiveSwitch.setChecked(isActive == 1);
            // Set layout parameters for the Switch to center it in the row
            TableRow.LayoutParams switchLayoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            );
            switchLayoutParams.gravity = Gravity.START;
            isActiveSwitch.setLayoutParams(switchLayoutParams);


// Set OnCheckedChangeListener for the Switch
            isActiveSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int newIsActive = isChecked ? 1 : 0;
                    updateIsActiveValue(bid, newIsActive);
                }
            });

            // Add the data TextViews to the data row
            dataRow.addView(bidTextView);
            dataRow.addView(bnameTextView);
            dataRow.addView(isActiveSwitch,switchLayoutParams);
            dataRow.addView(uentdtTextView);

            // Add the data row to the table layout
            baseTableLayout.addView(dataRow);
        }

        cursor.close();
        db.close();
    }

    private void updateIsActiveValue(int bid, int newIsActive) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put("is_active", newIsActive);

        int rowsAffected = db.update("base_mast", values, "bid=?", new String[]{String.valueOf(bid)});
        if (rowsAffected > 0) {
            Toast.makeText(this, "is_active value updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update is_active value", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
    private void updateSizeIsActiveValue(int szid, int newIsActive) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put("is_active", newIsActive);

        int rowsAffected = db.update("size_mast", values, "szid=?", new String[]{String.valueOf(szid)});
        if (rowsAffected > 0) {
            Toast.makeText(this, "is_active value updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update is_active value", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}
