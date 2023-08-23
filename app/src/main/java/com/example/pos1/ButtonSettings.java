package com.example.pos1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TableLayout;
import android.widget.TableRow;

public class ButtonSettings extends AppCompatActivity {
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button_settings);

        tableLayout = findViewById(R.id.tableLayout);

        displayTableData();
    }

    private void createTableHeading() {
        TableRow headingRow = new TableRow(this);
        TableRow.LayoutParams headingLayoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        headingRow.setLayoutParams(headingLayoutParams);

        TextView idHeading = new TextView(this);
        idHeading.setText("ID");
        idHeading.setPadding(16, 16, 16, 16);
        idHeading.setTypeface(null, Typeface.BOLD);
        headingRow.addView(idHeading);

        TextView slugHeading = new TextView(this);
        slugHeading.setText("Default Name");
        slugHeading.setPadding(16, 16, 16, 16);
        slugHeading.setTypeface(null, Typeface.BOLD);
        headingRow.addView(slugHeading);

        TextView nameHeading = new TextView(this);
        nameHeading.setText("Edit Name");
        nameHeading.setPadding(16, 16, 16, 16);
        nameHeading.setTypeface(null, Typeface.BOLD);
        headingRow.addView(nameHeading);

        TextView statusHeading = new TextView(this);
        statusHeading.setText("Active/InActive");
        statusHeading.setPadding(16, 16, 16, 16);
        statusHeading.setTypeface(null, Typeface.BOLD);
        headingRow.addView(statusHeading);

        tableLayout.addView(headingRow);
    }

    @SuppressLint("Range")
    private void displayTableData() {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM button_settings", null);

        tableLayout.removeAllViews();
        createTableHeading();

        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String slug = cursor.getString(cursor.getColumnIndex("slug"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            // Create TextView for id
            TextView idTextView = new TextView(this);
            idTextView.setText(String.valueOf(id));
            row.addView(idTextView);

            // Create EditText for name
            TextView slugEditText = new TextView(this);
            slugEditText.setText(slug);
            row.addView(slugEditText);

            // Create EditText for name
            EditText nameEditText = new EditText(this);
            nameEditText.setText(name);
            nameEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // Update the name value in the database in real-time
                    updateNameInDatabase(id, charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            row.addView(nameEditText);

            // Create Switch for status
            Switch statusSwitch = new Switch(this);
            TableRow.LayoutParams switchLayoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            );
            switchLayoutParams.gravity = Gravity.CENTER;  // Align the Switch to the start (left)
            statusSwitch.setLayoutParams(switchLayoutParams);

            statusSwitch.setChecked(status == 1);
            row.addView(statusSwitch);

            tableLayout.addView(row);
        }

        cursor.close();
        db.close();
    }

    private void updateNameInDatabase(int id, String newName) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put("name", newName);

        int rowsAffected = db.update("button_settings", values, "id=?", new String[]{String.valueOf(id)});
        db.close();

//        if (rowsAffected > 0) {
//            Toast.makeText(this, "Name updated successfully", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Failed to update name", Toast.LENGTH_SHORT).show();
//        }
    }
}
