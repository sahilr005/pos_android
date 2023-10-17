package com.example.pos1;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    AppCompatButton eatInButton,pickupButton,deliveryButton,counterButton;
    Button changeStatusButton;
    private TableRow selectedRow;
    private List<String> selectedFilters = new ArrayList<>();
    TableLayout tableLayout;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private NavigationView navigationView;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        eatInButton = findViewById(R.id.eatInButton);
        changeStatusButton = findViewById(R.id.changeStatusButton);
        tableLayout = findViewById(R.id.DashBoardOrdersTableLayout);
        pickupButton = findViewById(R.id.pickupButton);
        deliveryButton = findViewById(R.id.deliveryButton);
        counterButton = findViewById(R.id.counterButton);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        drawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, R.string.drawer_open, R.string.drawer_close);

        // Set the ActionBarDrawerToggle as the drawer listener
        drawerLayout.addDrawerListener(drawerToggle);

        // Enable the back arrow in the action bar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        // Set the navigation item click listener
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                // Handle navigation item clicks here
                switch (item.getItemId()) {
                    case R.id.nav_manger:
                        openManagerActivity();
                        return true;
                    // Handle other menu items if needed
                }
                // Close the drawer when an item is selected
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        pickupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickupDialog("Pickup");
            }
        });
        deliveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickupDialog("Delivery");
            }
        });
        counterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderDetails.class);
                startActivity(intent);
//                showPickupDialog("Counter");
            }
        });
        eatInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickupDialog("EatIn");
            }
        });

        LinearLayout statusButtonsContainer = findViewById(R.id.statusButtonsContainer);

        String[] orderTypes = {"All Orders", "Active Orders", "Completed", "EatIn", "Counter", "Pickup", "Delivery"};
        for (String type : orderTypes) {
            Button statusButton = new Button(this);
            statusButton.setText(type);
            statusButton.setBackgroundResource(R.drawable.button_shape);
// Set the LayoutParams to add a margin between buttons
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            layoutParams.setMargins(10, 10, 10, 10); // left, top, right, bottom margins
            statusButton.setLayoutParams(layoutParams);
            statusButton.setTextColor(ContextCompat.getColor(this, android.R.color.white));

            statusButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleFilter(type);
                }
            });
            statusButtonsContainer.addView(statusButton);
        }
        changeStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onChangeStatusClicked(v);
            }
        });
        toggleFilter("All Orders");

    }

    private void showPickupDialog(String orderType) {
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View dialogView = layoutInflater.inflate(R.layout.dasboard_dialog_pickup, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setTitle(orderType+" Customer Order");

        // Find the EditText fields in the dialog layout
        EditText phoneNumberEditText = dialogView.findViewById(R.id.phoneNumberEditText);
        EditText nameEditText = dialogView.findViewById(R.id.nameEditText);
        EditText addressLine1EditText = dialogView.findViewById(R.id.addressLine1EditText);
        EditText addressLine2EditText = dialogView.findViewById(R.id.addressLine2EditText);
        EditText emailEditText = dialogView.findViewById(R.id.emailEditText);
        EditText loyaltyCardNoEditText = dialogView.findViewById(R.id.loyaltyCardNoEditText);
        EditText suburbEditText = dialogView.findViewById(R.id.suburbEditText);
        EditText stateEditText = dialogView.findViewById(R.id.stateEditText);
        EditText postcodeEditText = dialogView.findViewById(R.id.postcodeEditText);
        EditText notesEditText = dialogView.findViewById(R.id.notesEditText);

        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Retrieve data from the EditText fields
                String phoneNumber = phoneNumberEditText.getText().toString().trim();
                String name = nameEditText.getText().toString().trim();
                String addressLine1 = addressLine1EditText.getText().toString().trim();
                String addressLine2 = addressLine2EditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String loyaltyCardNo = loyaltyCardNoEditText.getText().toString().trim();
                String suburb = suburbEditText.getText().toString().trim();
                String state = stateEditText.getText().toString().trim();
                String postcode = postcodeEditText.getText().toString().trim();
                String notes = notesEditText.getText().toString().trim();

                // Check if any of the required fields are empty
                if (TextUtils.isEmpty(phoneNumber) || TextUtils.isEmpty(name) ) {
                    // Display an error message if any of the required fields are empty
                    // You can customize this as per your requirement
                     Toast.makeText(MainActivity.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                    return ;
                }else {

                    // Save data to the users table
                   long userId= saveUserDetails(name, phoneNumber, email, addressLine1, addressLine2, suburb, state, postcode, loyaltyCardNo,notes,orderType);

                    // Save data to the order_data table
                    savePickupOrderData(userId,orderType);
                    // Refresh the table data after adding the new order
                    displayTableData();
                }
            }
        });

        alertDialogBuilder.setNegativeButton("Cancel", null);

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void savePickupOrderData(long userId, String orderType) {
        // Open the database for writing
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        // Get the current date in the required format
        String orderDate =  LocalDate.now().toString(); // You can customize this as needed
        double orderCost = 0.00; // You can set the actual order cost if required

        // Create a ContentValues object to hold the data to be inserted
        ContentValues orderValues = new ContentValues();
        orderValues.put("odate", orderDate);
        orderValues.put("custid", userId);
        orderValues.put("ordertype", orderType);
        orderValues.put("ordercost", orderCost);
        orderValues.put("order_status", "Pay Later"); // You can set the actual status as required

        // Insert the data into the order_data table
        long orderId = db.insert("order_data", null, orderValues);

        // Close the database after inserting data
        db.close();
    }
    private long saveUserDetails(String name, String phone, String email, String addressLine1,
                                 String addressLine2, String suburb, String state, String postcode,
                                 String loyaltyCardNo,String notes,String orderType) {
        // Open the database for writing
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        // Create a ContentValues object to hold the data to be inserted
        ContentValues userValues = new ContentValues();

        userValues.put("name", name);
        userValues.put("phone", phone);
        userValues.put("email", email);
        userValues.put("addr1", addressLine1);
        userValues.put("addr2", addressLine2);
        userValues.put("suburb", suburb);
        userValues.put("state", state);
        userValues.put("pin_code", postcode);
        userValues.put("loyalty_num", loyaltyCardNo);
        userValues.put("notes", notes);
        userValues.put("order_type", orderType);

        // Insert the data into the users table
        long userId = db.insert("customers", null, userValues);

        // Close the database after inserting data
        db.close();
        return userId;
    }

    private void toggleFilter(String filterType) {
        if (filterType.equals("All Orders")) {
            // For "All Orders," unselect all other filters and display all data
            selectedFilters.clear();
            selectedFilters.add(filterType);
            selectedFilters.remove("All Orders");

        } else if (filterType.equals("Active Orders") || filterType.equals("Completed")) {
            selectedFilters.clear();

            selectedFilters.add(filterType);
        } else if (filterType.equals("EatIn") || filterType.equals("Counter") ||
                filterType.equals("Pickup") || filterType.equals("Delivery")) {
            // For "walkin," "counter," "pickup," or "delivery," allow only single selection
            selectedFilters.remove("All Orders");
            if (selectedFilters.contains("EatIn") || selectedFilters.contains("Counter") ||
                    selectedFilters.contains("Pickup") || selectedFilters.contains("Delivery")) {
                selectedFilters.remove("EatIn");
                selectedFilters.remove("Counter");
                selectedFilters.remove("Pickup");
                selectedFilters.remove("Delivery");
            }

                selectedFilters.add(filterType);
        } else {
            // For other filters, allow multiple selections
            selectedFilters.remove("All Orders");
            if (selectedFilters.contains(filterType)) {
                selectedFilters.remove(filterType);
            } else {
                selectedFilters.add(filterType);
            }
        }
        // Hide the change status button by default
        Button changeStatusButton = findViewById(R.id.changeStatusButton);
        changeStatusButton.setVisibility(View.GONE);
        updateSelectedStatusButtons();
        displayTableData();

    }

    private void updateSelectedStatusButtons() {
        LinearLayout statusButtonsContainer = findViewById(R.id.statusButtonsContainer);
        for (int i = 0; i < statusButtonsContainer.getChildCount(); i++) {
            View view = statusButtonsContainer.getChildAt(i);
            if (view instanceof Button) {
                Button button = (Button) view;
                String buttonText = button.getText().toString();

                if (selectedFilters.contains(buttonText)) {
                    button.setBackgroundResource(R.drawable.red_button_shap);
                } else {
                    button.setBackgroundResource(R.drawable.button_shape);
                }
                if(selectedFilters.isEmpty()&&buttonText.contains("All Orders")){
                    button.setBackgroundResource(R.drawable.red_button_shap);
                }
            }
        }
    }

    @SuppressLint("Range")
    private void displayTableData() {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
//        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Log.d("kkkn", "displayTableData0000: ");
        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM order_data");
        if (!selectedFilters.isEmpty() && !selectedFilters.contains("All Orders")) {
            queryBuilder.append(" WHERE");
            for (int i = 0; i < selectedFilters.size(); i++) {
                if (i > 0) {
                    queryBuilder.append(" AND");
                }
                String filter = selectedFilters.get(i);
                switch (filter.toLowerCase()) { // Make the filter case-insensitive
                    case "active orders":
                        queryBuilder.append(" payment_status <> 'Completed'");
                        break;
                    case "completed":
                        queryBuilder.append(" payment_status = 'Completed'");
                        break;
                    default:
                        queryBuilder.append(" ordertype = '").append(filter).append("'");
                        break;
                }
            }
        }
        if (selectedFilters.contains("All Orders")) {
            queryBuilder = new StringBuilder("SELECT * FROM order_data");
        }
        Log.d("DB QUERY", "displayTableData: "+queryBuilder.toString());
        Log.d("DB QUERY", "displayTableData: -----");
//        addOdersSampleData();
         @SuppressLint("Recycle") Cursor cursor = db.rawQuery("SELECT * FROM order_data", null);
        tableLayout.removeAllViews();
        createTableHeading();
        // Add data to the table rows
        if (cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(cursor.getColumnIndex("order_id"));
                int webOrderId = cursor.getInt(cursor.getColumnIndex("order_id"));
                Double orderCost = cursor.getDouble(cursor.getColumnIndex("ordercost"));
                Double payCost = cursor.getDouble(cursor.getColumnIndex("paycost"));
                String order_status = cursor.getString(cursor.getColumnIndex("order_status"));
                String orderType = cursor.getString(cursor.getColumnIndex("ordertype"));
                String paymentType = cursor.getString(cursor.getColumnIndex("payment_type"));
                String payment_status = cursor.getString(cursor.getColumnIndex("payment_status"));
                String orderDate = cursor.getString(cursor.getColumnIndex("odate"));
                long customerId = cursor.getLong(cursor.getColumnIndex("custid"));

                // Retrieve data from the "customers" table based on customer ID
                Cursor customerCursor = db.rawQuery("SELECT name, phone FROM customers WHERE id = ?", new String[]{String.valueOf(customerId)});

                String customerName = "";
                String customerPhone = "";

                // Check if customerCursor has data
                if (customerCursor.getCount() > 0 && customerCursor.moveToFirst()) {
                    customerName = customerCursor.getString(customerCursor.getColumnIndex("name"));
                    customerPhone = String.valueOf(customerCursor.getLong(customerCursor.getColumnIndex("phone")));
                }

                customerCursor.close();

                // Check if the order meets all selected filter conditions
                boolean meetsAllFilters = true;
                for (String filter : selectedFilters) {
                    if (filter.equalsIgnoreCase("active orders")) {
                        if ("Completed".equalsIgnoreCase(payment_status)) {
                            meetsAllFilters = false;
                            break;
                        }
                    } else if (filter.equalsIgnoreCase("completed")) {
                        if (!"Completed".equalsIgnoreCase(payment_status)) {
                            meetsAllFilters = false;
                            break;
                        }
                    } else {
                        if (!filter.equalsIgnoreCase(orderType)) {
                            meetsAllFilters = false;
                            break;
                        }
                    }
                }
                // If the order meets all selected filter conditions, add it to the table
                if (meetsAllFilters) {
                    TableRow dataRow = new TableRow(this);
                    dataRow.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT
                    ));
                    dataRow.setBackgroundResource(R.drawable.border);

                    addDataTextView(dataRow, String.valueOf(orderId));
                    addDataTextView(dataRow, String.valueOf(webOrderId));
                    addDataTextView(dataRow, customerPhone);
                    addDataTextView(dataRow, customerName);
                    addDataTextView(dataRow, String.valueOf(orderCost));
                    addDataTextView(dataRow, String.valueOf(payCost));
                    addDataTextView(dataRow, orderType);
                    addDataTextView(dataRow, order_status);
                    addDataTextView(dataRow, paymentType);
                    addDataTextView(dataRow, payment_status);
                    addDataTextView(dataRow, orderDate);
                    addDataTextView(dataRow, "--");
                    createActionButton(dataRow, "Edit", customerId);

                    // Set the OnClickListener for each data row
                    dataRow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Deselect any previously selected row
                            if (selectedRow != null) {
                                selectedRow.setBackgroundColor(Color.TRANSPARENT);
                            }

                            // Select the clicked row
                            dataRow.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.light_blue));
                            selectedRow = dataRow;

                            // Show the change status button
                            Button changeStatusButton = findViewById(R.id.changeStatusButton);
                            changeStatusButton.setVisibility(View.VISIBLE);
                        }
                    });
                    tableLayout.addView(dataRow);
                }

//                Log.d("cursor ---11 ", "displayTableData: "+cursor);

            } while (cursor.moveToNext());
//            Log.d("cursor --- ", "displayTableData: "+cursor.getCount());
            cursor.close();
            db.close();
        }
    }

    // Add the method to handle the change status button click
    private void onChangeStatusClicked(View view) {
        if (selectedRow != null) {
            // Get the order ID from the selected row (adjust the index based on your data)
            TextView orderIdTextView = (TextView) selectedRow.getChildAt(0);
            int orderId = Integer.parseInt(orderIdTextView.getText().toString());

            // Create the custom dialog
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.dialog_change_status);
            dialog.setTitle("Change Status");

            // Find the RadioGroup and OK button in the dialog layout
            RadioGroup statusRadioGroup = dialog.findViewById(R.id.statusRadioGroup);
            Button okButton = dialog.findViewById(R.id.statusChangeDialogOkBtn);

            // Set the OnClickListener for the OK button
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the selected status from the RadioGroup
                    int selectedRadioButtonId = statusRadioGroup.getCheckedRadioButtonId();
                    String newStatus = "Uncompleted"; // Default value
                    if (selectedRadioButtonId != -1) {
                        RadioButton selectedRadioButton = dialog.findViewById(selectedRadioButtonId);
                        newStatus = selectedRadioButton.getText().toString();
                    }

                    // Update the status in the order_data table using the orderId
                    SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("payment_status", newStatus);
                    db.update("order_data", contentValues, "order_id=?", new String[]{String.valueOf(orderId)});
                    db.close();

                    // Close the dialog after updating the status
                    dialog.dismiss();

                    // Update the table data to reflect the changes
                    displayTableData();

                    // Deselect the row and hide the change status button
                    selectedRow.setBackgroundColor(Color.TRANSPARENT);
                    selectedRow = null;

                    Button changeStatusButton = findViewById(R.id.changeStatusButton);
                    changeStatusButton.setVisibility(View.GONE);
                }
            });

            // Show the dialog
            dialog.show();
        }
    }

    private void createTableHeading() {
        TableRow headingRow = new TableRow(this);
        headingRow.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT
        ));
        headingRow.setBackgroundResource(R.drawable.table_header_border);

        addHeadingTextView(headingRow, "ORDER NO.");
        addHeadingTextView(headingRow, "WEB ORDER NO.");
        addHeadingTextView(headingRow, "PHONE");
        addHeadingTextView(headingRow, "NAME");
        addHeadingTextView(headingRow, "ORD AMT.");
        addHeadingTextView(headingRow, "PAY AMT.");
        addHeadingTextView(headingRow, "ORD TYPE");
        addHeadingTextView(headingRow, "ORD STATUS");
        addHeadingTextView(headingRow, "PAY TYPE");
        addHeadingTextView(headingRow, "COMPLETED");
        addHeadingTextView(headingRow, "ORD DATE");
        addHeadingTextView(headingRow, "DUE DATE");
        addHeadingTextView(headingRow, "ACTION");

        tableLayout.addView(headingRow);
    }
    private void addHeadingTextView (TableRow row, String text){
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(16);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(16, 16, 16, 16);
        textView.setTypeface(null, Typeface.BOLD);
        row.addView(textView);
    }
    private void addDataTextView (TableRow row, String text){
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setTextColor(Color.BLACK);
        textView.setTextSize(16);
        textView.setPadding(16, 16, 16, 16);
        row.addView(textView);
    }
    private void createActionButton (TableRow row, String text, long customerId){
        AppCompatButton editButton = new AppCompatButton(this, null);
        editButton.setText(text);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, OrderDetails.class);
                intent.putExtra("customerId", customerId);
                startActivity(intent);
            }
        });
        row.addView(editButton);
    }

    private void openManagerActivity() {
        Intent intent = new Intent(this, Manager.class);
        startActivity(intent);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred
        drawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle other menu items if needed
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            // Close the drawer when the back button is pressed
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}