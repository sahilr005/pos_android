package com.example.pos1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UploadToppings extends AppCompatActivity {

    private static final int FILE_PICKER_REQUEST_CODE = 1;
    private List<List<String>> csvData;
    // Launch the file picker
    private void launchFilePicker() {
        // Check if the permission is already granted
        if (isStoragePermissionGranted()) {
            // Permission is granted, launch the file picker
            launchFilePickerIntent();
        } else {
            // Permission is not granted, request the permission
            requestStoragePermission();
        }
    }
    private void launchFilePickerIntent() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*"); // Set the desired MIME type here, e.g., "image/*" for images
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String[] mimeTypes = {"text/comma-separated-values"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);
    }

    private boolean isStoragePermissionGranted() {
        return ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // Show a rationale dialog explaining the need for the permission
            new AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This permission is required to access files on your device.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Request the permission
                            ActivityCompat.requestPermissions(UploadToppings.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},1);
                        }
                    })
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        } else {
            // Request the permission without showing a rationale dialog
            ActivityCompat.requestPermissions(UploadToppings.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission is granted, launch the file picker
                launchFilePickerIntent();
//            } else {
                // Permission is denied, handle accordingly (e.g., show a toast or display an error message)
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    AppCompatButton filePickerBtn,uploadItemCsv ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_toppings);
        filePickerBtn  = findViewById(R.id.ToppingFilePickerBtn);
        uploadItemCsv  = findViewById(R.id.uploadToppingCsv);
        uploadItemCsv.setVisibility(csvData != null ? View.VISIBLE : View.GONE);
        filePickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchFilePicker();
            }
        });
        uploadItemCsv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertCSVDataIntoDatabase();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_PICKER_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data != null) {
                Uri fileUri = data.getData();
                String filePath = fileUri.toString();
                Log.d("FILE", "onActivityResult: " + filePath);

                if (isCSVFile(fileUri)) {
                    csvData = readCSVFile(filePath); // Store CSV data in the member variable
                    displayCSVData(csvData);
                } else {
                    Toast.makeText(this, "Invalid file format. Please select a CSV file.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private boolean isCSVFile(Uri uri) {
        String mimeType = getContentResolver().getType(uri);
        return mimeType != null && mimeType.equals("text/comma-separated-values");
    }

    private List<List<String>> readCSVFile(String filePath) {
        List<List<String>> csvData = new ArrayList<>();

        try {
            InputStream inputStream = getContentResolver().openInputStream(Uri.parse(filePath));
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = reader.readLine()) != null) {
                List<String> row = Arrays.asList(line.split(","));
                csvData.add(row);
            }

            reader.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvData;
    }


    private void displayCSVData(List<List<String>> csvData) {
        TableLayout tableLayout = findViewById(R.id.ToppingUploadTableLayout);
        tableLayout.removeAllViews(); // Clear the table layout

        // Check if the CSV data is not empty
        if (csvData != null && !csvData.isEmpty()) {

            for (List<String> row : csvData) {
                TableRow tableRow = new TableRow(this);

                for (String value : row) {
                    TextView textView = new TextView(this);
                    textView.setText(value);
                    textView.setPadding(16, 16, 16, 16);
                    textView.setBackgroundResource(R.drawable.table_header_border); // Apply the border style
                    tableRow.addView(textView);
                }

                tableLayout.addView(tableRow);
            }
            uploadItemCsv.setVisibility(View.VISIBLE); // Show the "Upload" button
        } else {
            uploadItemCsv.setVisibility(View.GONE); // Hide the "Upload" button
        }
    }
    private void insertCSVDataIntoDatabase() {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        // Create topping_mast table
        db.execSQL("CREATE TABLE IF NOT EXISTS topping_mast (tid INTEGER PRIMARY KEY AUTOINCREMENT, tname TEXT, is_active INT, server_check INTEGER DEFAULT 0, uentdt TEXT)");

        for (int i = 1; i < csvData.size(); i++) {
            List<String> row = csvData.get(i);
            ContentValues values = new ContentValues();
            values.put("tname", getValueAtIndex(row, 0));

            // Parse the string value to an integer
            String isActiveValue = getValueAtIndex(row, 1);
            int isActive = 0;
            try {
                isActive = Integer.parseInt(isActiveValue);
            } catch (NumberFormatException e) {
                // Handle the exception if the value cannot be parsed to an integer
                e.printStackTrace();
            }

            values.put("is_active", isActive);
            values.put("server_check", 0);
            values.put("uentdt", getValueAtIndex(row, 2));

            long result = db.insert("topping_mast", null, values);

            if (result == -1) {
                // Failed to insert row, handle the error
                Toast.makeText(this, "Failed to insert row into topping_mast", Toast.LENGTH_SHORT).show();
            }
        }

        db.close();
        Toast.makeText(this, "Toppings uploaded successfully", Toast.LENGTH_SHORT).show();
        TableLayout tableLayout = findViewById(R.id.ToppingUploadTableLayout);
        tableLayout.removeAllViews();
        csvData = null;
    }

    private String getValueAtIndex(List<String> row, int index) {
        if (index >= 0 && index < row.size()) {
            String value = row.get(index);
            return value.isEmpty() ? "" : value;
        }
        return "";
    }

    private boolean parseBoolean(String value) {
        return value != null && (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1"));
    }

}