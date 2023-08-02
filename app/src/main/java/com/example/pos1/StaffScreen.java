package com.example.pos1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class StaffScreen extends AppCompatActivity {

    private TableLayout tableLayoutStaffData;
    private Button buttonAdd;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_screen);
        dbHelper = new DatabaseHelper(this);

        tableLayoutStaffData = findViewById(R.id.tableLayoutStaffData);
        buttonAdd = findViewById(R.id.buttonAdd);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });

        displayStaffData();
    }

    private void displayStaffData() {
        TableRow headingRow = new TableRow(this);
        headingRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        headingRow.setBackgroundResource(R.drawable.table_header_border);
//        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM staff_mast", null);

        addHeadingTextView(headingRow, "Id");
        addHeadingTextView(headingRow, "Name");
        addHeadingTextView(headingRow, "Email");
        addHeadingTextView(headingRow, "Phone No");
        addHeadingTextView(headingRow, "Delivery Rate");
        addHeadingTextView(headingRow, "Hourly Rate");
        addHeadingTextView(headingRow, "Security Profile");
        addHeadingTextView(headingRow, "Date Started");
        addHeadingTextView(headingRow, "Actions");

        tableLayoutStaffData.addView(headingRow);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") int stid = cursor.getInt(cursor.getColumnIndex("stid"));
            @SuppressLint("Range") String uname = cursor.getString(cursor.getColumnIndex("uname"));
            @SuppressLint("Range") String uemail = cursor.getString(cursor.getColumnIndex("uemail"));
            @SuppressLint("Range") String uphno = cursor.getString(cursor.getColumnIndex("uphno"));
            @SuppressLint("Range") double delrate = cursor.getDouble(cursor.getColumnIndex("delrate"));
            @SuppressLint("Range") double hrrate = cursor.getDouble(cursor.getColumnIndex("hrrate"));
            @SuppressLint("Range") int sid = cursor.getInt(cursor.getColumnIndex("sid"));
            @SuppressLint("Range") String stdt = cursor.getString(cursor.getColumnIndex("stdt"));
            @SuppressLint("Range") String pwd = cursor.getString(cursor.getColumnIndex("pwd"));
            // Get the profile_name from the user_security_mast table based on the sid
            @SuppressLint("Range") String profileName = getProfileNameBySid(sid);

            TableRow dataRow = new TableRow(this);
            dataRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            dataRow.setBackgroundResource(R.drawable.border);
            addDataTextView(dataRow,String.valueOf(stid));
            addDataTextView(dataRow,uname);
            addDataTextView(dataRow,uemail);
            addDataTextView(dataRow,uphno);
            addDataTextView(dataRow,String.valueOf(delrate));
            addDataTextView(dataRow,String.valueOf(hrrate));
            addDataTextView(dataRow, profileName);
            addDataTextView(dataRow,String.valueOf(stdt));
            // Add an "Edit" button to the "Actions" column
            addButtonToRow(dataRow, "Edit", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the staff ID from the current row
                    int staffId = stid;

                    // Call the editStaffData function with the staff ID
                    editStaffData(staffId);
                }
            });

            tableLayoutStaffData.addView(dataRow);
        }

        cursor.close();
        db.close();
    }

    private void editStaffData(int staffId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM staff_mast WHERE stid = ?", new String[]{String.valueOf(staffId)});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String uname = cursor.getString(cursor.getColumnIndex("uname"));
            @SuppressLint("Range") String uemail = cursor.getString(cursor.getColumnIndex("uemail"));
            @SuppressLint("Range") String uphno = cursor.getString(cursor.getColumnIndex("uphno"));
            @SuppressLint("Range") double delrate = cursor.getDouble(cursor.getColumnIndex("delrate"));
            @SuppressLint("Range") double hrrate = cursor.getDouble(cursor.getColumnIndex("hrrate"));
            @SuppressLint("Range") int sid = cursor.getInt(cursor.getColumnIndex("sid"));
            @SuppressLint("Range") String stdt = cursor.getString(cursor.getColumnIndex("stdt"));
            @SuppressLint("Range") String pwd = cursor.getString(cursor.getColumnIndex("pwd"));

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Edit Staff");

            // Inflate the layout for the dialog
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_staff, null);
            builder.setView(view);

            // Get references to the input fields
            EditText unameEditText = view.findViewById(R.id.editTextUname);
            EditText uemailEditText = view.findViewById(R.id.editTextUemail);
            EditText uphnoEditText = view.findViewById(R.id.editTextUphno);
            EditText delrateEditText = view.findViewById(R.id.editTextDelrate);
            EditText hrrateEditText = view.findViewById(R.id.editTextHrrate);
            Spinner securityProfileSpinner = view.findViewById(R.id.spinnerSecurityProfile); // Add the spinner
            EditText pwdEditText = view.findViewById(R.id.editTextPwd);

            // Populate the security profile spinner with data from the user_security_mast table
            populateSecurityProfileSpinner(securityProfileSpinner);

            // Pre-fill the input fields with the existing staff details
            unameEditText.setText(uname);
            uemailEditText.setText(uemail);
            uphnoEditText.setText(uphno);
            delrateEditText.setText(String.valueOf(delrate));
            hrrateEditText.setText(String.valueOf(hrrate));
            securityProfileSpinner.setSelection(getProfileNameIndex(sid)); // Select the staff's security profile
            pwdEditText.setText(pwd);

            builder.setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String updatedUname = unameEditText.getText().toString().trim();
                    String updatedUemail = uemailEditText.getText().toString().trim();
                    String updatedUphno = uphnoEditText.getText().toString().trim();
                    double updatedDelrate = Double.parseDouble(delrateEditText.getText().toString().trim());
                    double updatedHrrate = Double.parseDouble(hrrateEditText.getText().toString().trim());
                    String selectedProfile = securityProfileSpinner.getSelectedItem().toString();
                    int updatedSid = getSelectedProfileSid(selectedProfile); // Get the sid based on the selected profile
                    String updatedPwd = pwdEditText.getText().toString().trim();

                    updateStaffData(staffId, updatedUname, updatedUemail, updatedUphno, updatedDelrate, updatedHrrate, updatedSid, updatedPwd);
                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

        cursor.close();
        db.close();
    }

    private int getProfileNameIndex(int sid) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
//        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        // Query to fetch profile_name and sid from user_security_mast table
        Cursor cursor = db.rawQuery("SELECT sid, profile_name FROM user_security_mast", null);

        int index = 0;
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int profileSid = cursor.getInt(cursor.getColumnIndex("sid"));

                // Check if the sid matches the provided sid (the staff's security profile)
                if (profileSid == sid) {
                    // Get the index of the matched profile_name in the spinner
                    index = cursor.getPosition();
                    break;
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return index;
    }

    private void updateStaffData(int staffId, String updatedUname, String updatedUemail, String updatedUphno,
                                 double updatedDelrate, double updatedHrrate, int updatedSid, String updatedPwd) {
      SQLiteDatabase db = dbHelper.getReadableDatabase();
//        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("uname", updatedUname);
        values.put("uemail", updatedUemail);
        values.put("uphno", updatedUphno);
        values.put("delrate", updatedDelrate);
        values.put("hrrate", updatedHrrate);
        values.put("sid", updatedSid);
        values.put("pwd", updatedPwd);

        int rowsAffected = db.update("staff_mast", values, "stid = ?", new String[]{String.valueOf(staffId)});
        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(this, "Staff updated successfully", Toast.LENGTH_SHORT).show();
            // Refresh the displayed data
            tableLayoutStaffData.removeAllViews();
            displayStaffData();
        } else {
            Toast.makeText(this, "Failed to update staff", Toast.LENGTH_SHORT).show();
        }
    }

    private void addButtonToRow(TableRow row, String buttonText, View.OnClickListener clickListener) {
        Button button = new Button(this);
        button.setText(buttonText);
        button.setOnClickListener(clickListener);
        row.addView(button);
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

    private void showAddDialog() {
        LocalDateTime now = LocalDateTime.now();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Staff");

        // Inflate the layout for the dialog
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_staff, null);
        builder.setView(view);

        // Get references to the input fields
        EditText unameEditText = view.findViewById(R.id.editTextUname);
        EditText uemailEditText = view.findViewById(R.id.editTextUemail);
        EditText uphnoEditText = view.findViewById(R.id.editTextUphno);
        EditText delrateEditText = view.findViewById(R.id.editTextDelrate);
        EditText hrrateEditText = view.findViewById(R.id.editTextHrrate);
        Spinner securityProfileSpinner = view.findViewById(R.id.spinnerSecurityProfile); // Add the spinner

        // Fetch data from user_security_mast table and populate the spinner
        populateSecurityProfileSpinner(securityProfileSpinner);

        EditText pwdEditText = view.findViewById(R.id.editTextPwd);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String uname = unameEditText.getText().toString().trim();
                String uemail = uemailEditText.getText().toString().trim();
                String uphno = uphnoEditText.getText().toString().trim();
                double delrate = Double.parseDouble(delrateEditText.getText().toString().trim());
                double hrrate = Double.parseDouble(hrrateEditText.getText().toString().trim());
                String selectedProfile = securityProfileSpinner.getSelectedItem().toString();
                int sid = getSelectedProfileSid(selectedProfile); // Get the sid based on the selected profile
                String stdt = now.toString();
                String pwd = pwdEditText.getText().toString().trim();

                addStaffData(uname, uemail, uphno, delrate, hrrate, sid, stdt, pwd);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void populateSecurityProfileSpinner(Spinner spinner) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        // Query to fetch all profile_name values from user_security_mast table
        Cursor cursor = db.rawQuery("SELECT profile_name FROM user_security_mast", null);

        List<String> profileNames = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String profileName = cursor.getString(cursor.getColumnIndex("profile_name"));
                profileNames.add(profileName);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // Populate the spinner with the profile names
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, profileNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @SuppressLint("Range")
    private int getSelectedProfileSid(String selectedProfile) {
        int sid = -1; // Default value if the selected profile is not found

        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT sid FROM user_security_mast WHERE profile_name = ?", new String[]{selectedProfile});

        if (cursor.moveToFirst()) {
            sid = cursor.getInt(cursor.getColumnIndex("sid"));
        }

        cursor.close();
        db.close();

        return sid;
    }

    @SuppressLint("Range")
    private String getProfileNameBySid(int sid) {
        String profileName = "";

        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT profile_name FROM user_security_mast WHERE sid = ?", new String[]{String.valueOf(sid)});

        if (cursor.moveToFirst()) {
            profileName = cursor.getString(cursor.getColumnIndex("profile_name"));
        }

        cursor.close();
        db.close();

        return profileName;
    }


    private void addStaffData(String uname, String uemail, String uphno, double delrate, double hrrate, int sid, String stdt, String pwd) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        LocalDateTime now = LocalDateTime.now();
        ContentValues values = new ContentValues();
        values.put("uname", uname);
        values.put("uemail", uemail);
        values.put("uphno", uphno);
        values.put("delrate", delrate);
        values.put("hrrate", hrrate);
        values.put("sid", sid); // Add the selected sid
        values.put("stdt", stdt);
        values.put("pwd", pwd);
        values.put("stdt", now.toString());

        long rowId = db.insert("staff_mast", null, values);
        if (rowId != -1) {
            Toast.makeText(this, "Staff added successfully", Toast.LENGTH_SHORT).show();
            // Refresh the displayed data
            tableLayoutStaffData.removeAllViews();
            displayStaffData();
        } else {
            Toast.makeText(this, "Failed to add staff", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

}
