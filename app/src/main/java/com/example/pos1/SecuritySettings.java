package com.example.pos1;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SecuritySettings extends AppCompatActivity {

    Button addSecurityProfileBtn;
    Spinner securityProfileSpinner;
    LinearLayout checkBoxContainer; // LinearLayout to hold CheckBoxes
    private int selectedProfileSid = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_settings);

        addSecurityProfileBtn = findViewById(R.id.addSecurityProfileBtn);
        securityProfileSpinner = findViewById(R.id.securityProfileSpinner);
        checkBoxContainer = findViewById(R.id.checkBoxContainer); // Initialize LinearLayout

        addSecurityProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button click to add security profile
                // You can implement the logic to add a new security profile here.
                // For simplicity, I'm assuming you have implemented the AddSecurityProfile activity.
                // You can replace this with the appropriate logic for adding a new profile.
                Intent intent = new Intent(SecuritySettings.this,AddSecurityProfile.class);
                startActivity(intent);
                showToast("Add Security Profile button clicked!");
            }
        });

        // Fetch and populate CheckBoxes with data from the security_settings_mast table
        populateCheckBoxes();
        // Fetch and populate the securityProfileSpinner with profile_name values
        populateSecurityProfileSpinner();

        // Set up event listener for the securityProfileSpinner
        securityProfileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When a profile is selected, update the CheckBoxes based on the selected profile
                String selectedProfile = securityProfileSpinner.getSelectedItem().toString();
                selectedProfileSid = getSelectedProfileSid(selectedProfile);
                updateCheckBoxes(selectedProfileSid);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle case when nothing is selected (optional)
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh the CheckBoxes and Spinner data whenever the activity is resumed.
        populateCheckBoxes();
        populateSecurityProfileSpinner();
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

    private void updateCheckBoxes(int selectedSid) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        // Query to fetch all ssid values associated with the selected profile from user_security_data table
        Cursor cursor = db.rawQuery("SELECT ssid FROM user_security_data WHERE sid = ?", new String[]{String.valueOf(selectedSid)});

        // Create a list to keep track of existing ssids for the selected profile
        List<Integer> existingSsids = new ArrayList<>();

        // Check if the cursor contains data and store existing ssids in the list
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int ssid = cursor.getInt(cursor.getColumnIndex("ssid"));
                existingSsids.add(ssid);
            } while (cursor.moveToNext());
        }

        // Close the cursor
        cursor.close();

        // Clear existing CheckBoxes
        checkBoxContainer.removeAllViews();

        SQLiteDatabase db2 = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        // Query to fetch all data from the security_settings_mast table
        Cursor cursor2 = db2.rawQuery("SELECT * FROM security_settings_mast", null);

        // Check if the cursor contains data
        if (cursor2.moveToFirst()) {
            do {
                // Extract data from the cursor
                @SuppressLint("Range") int ssid = cursor2.getInt(cursor2.getColumnIndex("ssid"));
                @SuppressLint("Range") String ssname = cursor2.getString(cursor2.getColumnIndex("ssname"));

                // Create a new CheckBox
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(ssname);
                checkBox.setTag(ssid); // Set the ssid as a tag to identify the CheckBox later if needed

                // Check if the ssid exists in the existingSsids list and set the CheckBox state accordingly
                if (existingSsids.contains(ssid)) {
                    checkBox.setChecked(true);
                } else {
                    checkBox.setChecked(false);
                }

                checkBoxContainer.addView(checkBox);

                // Set up event listener for the CheckBox
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        // When a CheckBox state is changed, update the user_security_data table
                        int selectedSsid = (int) checkBox.getTag();

                        if (isChecked) {
                            // If the CheckBox is checked, insert a new row in user_security_data table
                            insertSecurityData(selectedProfileSid, selectedSsid);
                        } else {
                            // If the CheckBox is unchecked, delete the corresponding row from user_security_data table
                            deleteSecurityData(selectedProfileSid, selectedSsid);
                        }
                    }
                });

            } while (cursor2.moveToNext());
        }

        // Close the cursor and database
        cursor2.close();
        db2.close();
    }

    // Method to set up event listeners for the CheckBoxes
    private void setUpCheckBoxListeners() {
        for (int i = 0; i < checkBoxContainer.getChildCount(); i++) {
            CheckBox checkBox = (CheckBox) checkBoxContainer.getChildAt(i);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    // When a CheckBox state is changed, update the user_security_data table
                    int selectedSsid = (int) checkBox.getTag();

                    if (isChecked) {
                        // If the CheckBox is checked, insert a new row in user_security_data table
                        insertSecurityData(selectedProfileSid, selectedSsid);
                    } else {
                        // If the CheckBox is unchecked, delete the corresponding row from user_security_data table
                        deleteSecurityData(selectedProfileSid, selectedSsid);
                    }
                }
            });
        }
    }

    // Method to insert data into user_security_data table
    private void insertSecurityData(int sid, int ssid) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("sid", sid);
        values.put("ssid", ssid);

        long rowId = db.insert("user_security_data", null, values);
        db.close();

        if (rowId != -1) {
            showToast("Successfully added to security settings.");
        } else {
            showToast("Failed to add to security settings.");
        }
    }

    // Method to delete data from user_security_data table
    private void deleteSecurityData(int sid, int ssid) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        int deletedRows = db.delete("user_security_data", "sid = ? AND ssid = ?", new String[]{String.valueOf(sid), String.valueOf(ssid)});
        db.close();

        if (deletedRows > 0) {
            showToast("Successfully removed from security settings.");
        } else {
            showToast("Failed to remove from security settings.");
        }
    }

    private void populateSecurityProfileSpinner() {
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
        securityProfileSpinner.setAdapter(adapter);
    }

    private void populateCheckBoxes() {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        // Query to fetch all data from the security_settings_mast table
        Cursor cursor = db.rawQuery("SELECT * FROM security_settings_mast", null);

        // Clear existing CheckBoxes
        checkBoxContainer.removeAllViews();

        // Check if the cursor contains data
        if (cursor.moveToFirst()) {
            do {
                // Extract data from the cursor
                @SuppressLint("Range") int ssid = cursor.getInt(cursor.getColumnIndex("ssid"));
                @SuppressLint("Range") String ssname = cursor.getString(cursor.getColumnIndex("ssname"));

                // Create a new CheckBox
                CheckBox checkBox = new CheckBox(this);
                checkBox.setText(ssname);
                checkBox.setTag(ssid); // Set the ssid as a tag to identify the CheckBox later if needed
                checkBox.setChecked(isCheckBoxChecked(ssid)); // Set the CheckBox state based on user_security_data
                checkBoxContainer.addView(checkBox);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        // Set up event listeners for the CheckBoxes after they are populated
        setUpCheckBoxListeners();
    }

    private boolean isCheckBoxChecked(int ssid) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        // Query to check if the given ssid is present in the user_security_data table
        Cursor cursor = db.rawQuery("SELECT ssid FROM user_security_data WHERE ssid = ?", new String[]{String.valueOf(ssid)});

        boolean isChecked = cursor.getCount() > 0;

        cursor.close();
        db.close();

        return isChecked;
    }

    // Helper method to show Toast messages
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
