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

public class Customer extends AppCompatActivity {
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);

        tableLayout = findViewById(R.id.customerMstTable);

        displayTableData();
    }

    private void createTableHeading() {
        TableRow headingRow = new TableRow(this);

        headingRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        headingRow.setBackgroundResource(R.drawable.table_header_border);

        addHeadingTextView(headingRow, "Customer No.");
        addHeadingTextView(headingRow, "Name");
        addHeadingTextView(headingRow, "Phone");
        addHeadingTextView(headingRow, "Email");
        addHeadingTextView(headingRow, "Points");
        addHeadingTextView(headingRow, "Address");
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
    private void addDataTextView(TableRow row, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        row.addView(textView);
    }
    private void addButtonToRow(TableRow row, String buttonText, View.OnClickListener clickListener) {
        Button button = new Button(this);
        button.setText(buttonText);
        button.setOnClickListener(clickListener);
        row.addView(button);
    }
    @SuppressLint("Range")
    private void displayTableData() {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM customers", null);

        tableLayout.removeAllViews();
        createTableHeading();

        while (cursor.moveToNext()) {
             int id = cursor.getInt(cursor.getColumnIndex("id"));
            int phone = cursor.getInt(cursor.getColumnIndex("phone"));
            int points = cursor.getInt(cursor.getColumnIndex("loyalty_num"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String email = cursor.getString(cursor.getColumnIndex("email"));
            String addr1 = cursor.getString(cursor.getColumnIndex("addr1"));

            TableRow row = new TableRow(this);
            row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            row.setBackgroundResource(R.drawable.border);

            addDataTextView(row, String.valueOf(id));
            addDataTextView(row, name);
            addDataTextView(row, String.valueOf(phone));
            addDataTextView(row, email);
            addDataTextView(row, String.valueOf(points));
            addDataTextView(row, String.valueOf(addr1));
            addButtonToRow(row, "Edit", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showEditCustomerDialog(id);
                }
            });

            tableLayout.addView(row);
        }

        cursor.close();
        db.close();
    }

    @SuppressLint("Range")
    private void showEditCustomerDialog(int customerId) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_customer, null);
        dialogBuilder.setView(dialogView);

        EditText nameEditText = dialogView.findViewById(R.id.CDeditName);
        EditText phoneEditText = dialogView.findViewById(R.id.CDeditPhone);
        EditText emailEditText = dialogView.findViewById(R.id.CDeditEmail);
        EditText pointsEditText = dialogView.findViewById(R.id.CDeditPoints);
        EditText addr1EditText = dialogView.findViewById(R.id.CDeditAddr1);
        EditText suburbEditText = dialogView.findViewById(R.id.CDeditSuburb);
        EditText postCodeEditText = dialogView.findViewById(R.id.CDeditPostCode);
        // Add more EditText fields for other attributes

        // Fetch customer data from the database using customerId
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM customers WHERE id=?", new String[]{String.valueOf(customerId)});

        if (cursor.moveToFirst()) {
            nameEditText.setText(cursor.getString(cursor.getColumnIndex("name")));
            phoneEditText.setText(cursor.getString(cursor.getColumnIndex("phone")));
            emailEditText.setText(cursor.getString(cursor.getColumnIndex("email")));
            pointsEditText.setText(cursor.getString(cursor.getColumnIndex("loyalty_num")));
            addr1EditText.setText(cursor.getString(cursor.getColumnIndex("addr1")));
            suburbEditText.setText(cursor.getString(cursor.getColumnIndex("suburb")));
            postCodeEditText.setText(cursor.getString(cursor.getColumnIndex("pin_code")));
        }

        cursor.close();
        db.close();

        dialogBuilder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Retrieve updated data from the EditText fields
                String updatedName = nameEditText.getText().toString();
                String updatedPhone = phoneEditText.getText().toString();
                String updatedEmail = emailEditText.getText().toString();
                String updatedPoints = pointsEditText.getText().toString();
                String updatedAddr1 = addr1EditText.getText().toString();
                String updatedSuburb = suburbEditText.getText().toString();
                String updatedPostCode = addr1EditText.getText().toString();
                // Get other updated data

                // Update customer data in the database
                updateCustomerData(customerId, updatedName, updatedPhone, updatedEmail, updatedPoints, updatedAddr1,updatedSuburb, updatedPostCode);

                // Refresh the table data
                tableLayout.removeAllViews();
                createTableHeading();
                displayTableData();
            }
        });

        dialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
    }

    private void updateCustomerData(int customerId, String name, String phone, String email, String points, String addr1,String suburb , String pinCode) {
        // Update customer data in the database
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("phone", phone);
        values.put("email", email);
        values.put("loyalty_num", points);
        values.put("addr1", addr1);
        values.put("suburb", suburb);
        values.put("pin_code", pinCode);

        int rowsAffected = db.update("customers", values, "id=?", new String[]{String.valueOf(customerId)});
        db.close();

        if (rowsAffected > 0) {
            // Data updated successfully
        } else {
            // Failed to update data
        }
    }
}