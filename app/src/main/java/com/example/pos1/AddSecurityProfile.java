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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddSecurityProfile extends AppCompatActivity {

    private TableLayout tableLayoutProfiles;
    private Button buttonAddProfile;
    private List<UserProfile> userProfileList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_security_profile);

        tableLayoutProfiles = findViewById(R.id.addNewSecurityTable);
        buttonAddProfile = findViewById(R.id.buttonAddProfile);

        buttonAddProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddProfileDialog();
            }
        });

        // Fetch data and populate the table
        fetchUserProfiles();
    }

    private void createTableHeading() {
        TableRow headingRow = new TableRow(this);
        headingRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        headingRow.setBackgroundResource(R.drawable.table_header_border);

        addHeadingTextView(headingRow, "Security Profile");
        addHeadingTextView(headingRow, "Order No.");
        addHeadingTextView(headingRow, "Action");

        tableLayoutProfiles.addView(headingRow);
    }

    private void addHeadingTextView(TableRow row, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        textView.setTypeface(null, Typeface.BOLD);
        row.addView(textView);
    }

    private void fetchUserProfiles() {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        // Query to fetch all data from the user_security_mast table
        Cursor cursor = db.rawQuery("SELECT * FROM user_security_mast ORDER BY order_no", null);

        // Clear existing rows from the table
        tableLayoutProfiles.removeAllViews();
        createTableHeading();

        // Check if the cursor contains data
        if (cursor.moveToFirst()) {
            userProfileList = new ArrayList<>();

            do {
                // Extract data from the cursor
                @SuppressLint("Range") int sid = cursor.getInt(cursor.getColumnIndex("sid"));
                @SuppressLint("Range") String profileName = cursor.getString(cursor.getColumnIndex("profile_name"));
                @SuppressLint("Range") int orderNo = cursor.getInt(cursor.getColumnIndex("order_no"));

                // Create a UserProfile object and add it to the list
                UserProfile userProfile = new UserProfile(sid, profileName, orderNo);
                userProfileList.add(userProfile);

                // Add rows to the table
                TableRow row = new TableRow(this);
                row.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.WRAP_CONTENT
                ));

                TextView profileNameTextView = new TextView(this);
                profileNameTextView.setText(userProfile.getProfileName());
                row.addView(profileNameTextView);

                TextView orderNoTextView = new TextView(this);
                orderNoTextView.setText(String.valueOf(userProfile.getOrderNo()));
                row.addView(orderNoTextView);

                Button actionButton = new Button(this);
                actionButton.setText("Edit");
                actionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Open dialog for editing the profile details
                        showEditProfileDialog(userProfile);
                    }
                });
                row.addView(actionButton);

                tableLayoutProfiles.addView(row);

            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
    }

    private void showEditProfileDialog(UserProfile userProfile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Profile");

        // Inflate the dialog_edit_profile.xml layout
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_profile, null);
        builder.setView(view);

        // Get references to the input fields in the dialog
        EditText profileNameEditText = view.findViewById(R.id.editTextProfileName);
        EditText orderNoEditText = view.findViewById(R.id.editTextOrderNo);

        // Pre-fill the input fields with the profile details
        profileNameEditText.setText(userProfile.getProfileName());
        orderNoEditText.setText(String.valueOf(userProfile.getOrderNo()));

        builder.setPositiveButton("Save Changes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the updated user inputs from the EditTexts
                String profileName = profileNameEditText.getText().toString().trim();
                int orderNo = Integer.parseInt(orderNoEditText.getText().toString().trim());

                // Update the profile in the database
                updateProfile(userProfile.getSid(), profileName, orderNo);

                // Refresh the displayed data in the table
                fetchUserProfiles();
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

    private void updateProfile(int sid, String profileName, int orderNo) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put("profile_name", profileName);
        values.put("order_no", orderNo);

        int rowsAffected = db.update("user_security_mast", values, "sid = ?", new String[]{String.valueOf(sid)});
        db.close();

        if (rowsAffected > 0) {
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to update profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddProfileDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add New Profile");

        // Inflate the dialog_add_profile.xml layout
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_profile, null);
        builder.setView(view);

        // Get references to the input fields in the dialog
        EditText profileNameEditText = view.findViewById(R.id.editTextProfileName);
        EditText orderNoEditText = view.findViewById(R.id.editTextOrderNo);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Get the user inputs from the EditTexts
                String profileName = profileNameEditText.getText().toString().trim();
                int orderNo = Integer.parseInt(orderNoEditText.getText().toString().trim());

                // Add the new profile to the database
                addNewProfile(profileName, orderNo);

                // Refresh the displayed data in the table
                fetchUserProfiles();
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

    private void addNewProfile(String profileName, int orderNo) {
        // Implement the logic to add the new profile to the user_security_mast table.
        // Use SQLiteOpenHelper, SQLiteDatabase, and ContentValues to insert the new profile.
        // Replace this with your actual data insertion logic.
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("profile_name", profileName);
        values.put("order_no", orderNo);
        values.put("is_delete", 0); // Default value for is_delete column
        values.put("system_defined", 0); // Default value for system_defined column

        long rowId = db.insert("user_security_mast", null, values);
        db.close();

        if (rowId != -1) {
            Toast.makeText(this, "Profile added successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add profile", Toast.LENGTH_SHORT).show();
        }
    }
}

class UserProfile {
    private int sid;
    private String profileName;
    private int orderNo;

    public UserProfile(int sid, String profileName, int orderNo) {
        this.sid = sid;
        this.profileName = profileName;
        this.orderNo = orderNo;
    }

    public int getSid() {
        return sid;
    }

    public String getProfileName() {
        return profileName;
    }

    public int getOrderNo() {
        return orderNo;
    }
}

