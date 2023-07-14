package com.example.pos1;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HappyHours extends AppCompatActivity {
    private TableLayout tableLayout;
    private Button createHappyHoursBtn;
    private EditText fromDayEditText;
    private EditText toDayEditText;
    private EditText timeFromEditText;
    private EditText timeToEditText;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_happy_hours);

        tableLayout = findViewById(R.id.happyHoursTable);
        createHappyHoursBtn = findViewById(R.id.createHappyHourBtn);
        fromDayEditText = findViewById(R.id.fromDayEditText);
        toDayEditText = findViewById(R.id.toDayEditText);
        timeFromEditText = findViewById(R.id.timeFromEditText);
        timeToEditText = findViewById(R.id.timeToEditText);


        fromDayEditText.setInputType(InputType.TYPE_NULL);
        toDayEditText.setInputType(InputType.TYPE_NULL);
        timeFromEditText.setInputType(InputType.TYPE_NULL);
        timeToEditText.setInputType(InputType.TYPE_NULL);

        fromDayEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    fromDayEditText.clearFocus();
                    showDayPickerDialog(fromDayEditText);
                    return true;
                }
                return false;
            }
        });
        toDayEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    toDayEditText.clearFocus();
                    showDayPickerDialog(toDayEditText);
                    return true;
                }
                return false;
            }
        });

        timeFromEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    timeFromEditText.clearFocus();
                    showTimePickerDialog(timeFromEditText);
                    return true;
                }
                return false;
            }
        });

        timeToEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    timeToEditText.clearFocus();
                    showTimePickerDialog(timeToEditText);
                    return true;
                }
                return false;
            }
        });
        createTableHeading();
        displayTableData();

        createHappyHoursBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createHappyHours();
            }
        });
    }
    private void createTableHeading() {
        TableRow headingRow = new TableRow(this);
        headingRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        headingRow.setBackgroundResource(R.drawable.table_header_border);

        addHeadingTextView(headingRow, "From Day");
        addHeadingTextView(headingRow, "To Day");
        addHeadingTextView(headingRow, "Time From");
        addHeadingTextView(headingRow, "Time to");
        addHeadingTextView(headingRow, "Delete");

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

    private void addDeleteButton(TableRow row, int id) {
        Button deleteButton = new Button(this);
        deleteButton.setText("Delete");
        deleteButton.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

                // Execute the delete operation
                int rowsAffected = db.delete("happy_hours", "id" + "=?", new String[]{String.valueOf(id)});

                // Close the database connection
                db.close();

                // Check if the delete operation was successful
                // Row deleted successfully
                if (rowsAffected > 0)
                    Toast.makeText(HappyHours.this, "Data deleted successfully", Toast.LENGTH_SHORT).show();
                else {
                    // Failed to delete row
                    Toast.makeText(HappyHours.this, "Failed to delete row", Toast.LENGTH_SHORT).show();
                }
                tableLayout.removeView(row);
            }
        });
        row.addView(deleteButton);
    }
    private void displayTableData() {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * from happy_hours", null);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String from_day = cursor.getString(cursor.getColumnIndex("from_day"));
            @SuppressLint("Range") String to_day = cursor.getString(cursor.getColumnIndex("to_day"));
            @SuppressLint("Range") String time_from = cursor.getString(cursor.getColumnIndex("time_from"));
            @SuppressLint("Range") String time_to = cursor.getString(cursor.getColumnIndex("time_to"));

            TableRow dataRow = new TableRow(this);
            dataRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));
            dataRow.setBackgroundResource(R.drawable.border);

            addDataTextView(dataRow, from_day);
            addDataTextView(dataRow, to_day);
            addDataTextView(dataRow, time_from);
            addDataTextView(dataRow, time_to);
            addDeleteButton(dataRow, id);

            tableLayout.addView(dataRow);
        }

        cursor.close();
        db.close();
    }

    private void createHappyHours() {
        String fromDay = fromDayEditText.getText().toString().trim();
        String toDay = toDayEditText.getText().toString().trim();
        String timeFrom = timeFromEditText.getText().toString().trim();
        String timeTo = timeToEditText.getText().toString().trim();

        if (fromDay.isEmpty() || toDay.isEmpty() || timeFrom.isEmpty() || timeTo.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Insert the happy hours data into the database
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("from_day", fromDay);
        values.put("to_day", toDay);
        values.put("time_from", timeFrom);
        values.put("time_to", timeTo);

        long result = db.insert("happy_hours", null, values);
        db.close();

        if (result != -1) {
            Toast.makeText(this, "Happy Hours created successfully", Toast.LENGTH_SHORT).show();
            // Clear the input fields
            fromDayEditText.setText("");
            toDayEditText.setText("");
            timeFromEditText.setText("");
            timeToEditText.setText("");

            // Refresh the table data
            tableLayout.removeAllViews();
            createTableHeading();
            displayTableData();
        } else {
            Toast.makeText(this, "Failed to create Happy Hours", Toast.LENGTH_SHORT).show();
        }
    }

    private void showDayPickerDialog(final EditText editText) {
        final String[] daysOfWeek = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Day");
        builder.setItems(daysOfWeek, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedDay = daysOfWeek[which];
                editText.setText(selectedDay);
            }
        });
        builder.show();
    }

    private void showTimePickerDialog(final EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String time = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        editText.setText(time);
                    }
                }, hour, minute, true);

        timePickerDialog.show();
    }

}