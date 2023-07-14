package com.example.pos1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class StaffScreen extends AppCompatActivity {

    private TableLayout tableLayoutStaffData;
    private Button buttonAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_screen);

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
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM staff_mast", null);

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

            TableRow row = new TableRow(this);

            TextView stidTextView = new TextView(this);
            stidTextView.setText(String.valueOf(stid));
            row.addView(stidTextView);

            TextView unameTextView = new TextView(this);
            unameTextView.setText(uname);
            row.addView(unameTextView);

            TextView uemailTextView = new TextView(this);
            uemailTextView.setText(uemail);
            row.addView(uemailTextView);

            TextView uphnoTextView = new TextView(this);
            uphnoTextView.setText(uphno);
            row.addView(uphnoTextView);

            TextView delrateTextView = new TextView(this);
            delrateTextView.setText(String.valueOf(delrate));
            row.addView(delrateTextView);

            TextView hrrateTextView = new TextView(this);
            hrrateTextView.setText(String.valueOf(hrrate));
            row.addView(hrrateTextView);

            TextView sidTextView = new TextView(this);
            sidTextView.setText(String.valueOf(sid));
            row.addView(sidTextView);

            tableLayoutStaffData.addView(row);
        }

        cursor.close();
        db.close();
    }

    private void showAddDialog() {
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
        EditText sidEditText = view.findViewById(R.id.editTextSid);
        EditText stdtEditText = view.findViewById(R.id.editTextStdt);
        EditText pwdEditText = view.findViewById(R.id.editTextPwd);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String uname = unameEditText.getText().toString().trim();
                String uemail = uemailEditText.getText().toString().trim();
                String uphno = uphnoEditText.getText().toString().trim();
                double delrate = Double.parseDouble(delrateEditText.getText().toString().trim());
                double hrrate = Double.parseDouble(hrrateEditText.getText().toString().trim());
                int sid = Integer.parseInt(sidEditText.getText().toString().trim());
                String stdt = stdtEditText.getText().toString().trim();
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

    private void addStaffData(String uname, String uemail, String uphno, double delrate, double hrrate, int sid, String stdt, String pwd) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put("uname", uname);
        values.put("uemail", uemail);
        values.put("uphno", uphno);
        values.put("delrate", delrate);
        values.put("hrrate", hrrate);
        values.put("sid", sid);
        values.put("stdt", stdt);
        values.put("pwd", pwd);

        long rowId = db.insert("staff_mast", null, values);
        if (rowId != -1) {
            Toast.makeText(this, "Staff added successfully", Toast.LENGTH_SHORT).show();
            // Refresh the displayed data
            displayStaffData();
        } else {
            Toast.makeText(this, "Failed to add staff", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }

}
