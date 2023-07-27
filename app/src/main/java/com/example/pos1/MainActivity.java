package com.example.pos1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    AppCompatButton mangerButton, eatInButton,pickupButton;
    Button changeStatusButton;
    private TableRow selectedRow;
    private List<String> selectedFilters = new ArrayList<>();
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DatabaseHelper(this);
        mangerButton = findViewById(R.id.mangerButton);
        eatInButton = findViewById(R.id.eatInButton);
        changeStatusButton = findViewById(R.id.changeStatusButton);
        tableLayout = findViewById(R.id.DashBoardOrdersTableLayout);
        pickupButton = findViewById(R.id.pickupButton);

        mangerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Manager.class);
                startActivity(intent);
            }
        });
        pickupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        // --- sample data add
//        eatInButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Add this code snippet to a method where you want to insert sample data
//
//// Sample data for order_data table
//                String[] orderDates = {"2023-07-18"};
//                long[] customerIds = {1};
//                String[] orderTypes = {"EatIn"};
//                double[] orderCosts = {50.00};
//                String[] orderStatuses = {"Pay Later"};
//                double[] webFrees = {5.00};
//                double[] gsts = {7.50};
//                double[] delCosts = {3.00};
//                double[] discounts = {0.00};
//                String[] cNotes = {"" };
//                String[] suburbs = {"New York"};
//                String[] payModes = { "Cash"};
//                String[] txnIds = { "987654321"};
//                String[] paymentStatuses = {"Completed"};
//                String[] paymentTypes = { "Cash"};
//                String[] payerStatuses = {"Verified"};
//                String[] payerEmails = {"john@example.com"};
//                double[] redeemAmts = {0.00};
//                String[] couponCodes = {""};
//                String[] dvDates = {"2023-07-19"};
//                String[] uentDts = {"2023-07-18 12:00:00"};
//                double[] cashAmts = {30.00};
//                double[] eftposAmts = {20.00};
//                String[] serverTypes = {"Regular"};
//                double[] refundAmts = {0.00};
//                String[] refundTypes = {""};
//                String[] refundNotes = {""};
//                String[] refundDates = {""};
//                String[] eDates = {"2023-07-19"};
//                boolean[] onAccounts = {true};
//                double[] surchargeCosts = {0.00};
//                double[] payCosts = {0.00};
//
//                // Open the database for writing
//                SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
//
//                // Loop through the sample data arrays and insert the data into the order_data table
//                for (int i = 0; i < orderDates.length; i++) {
//                    ContentValues values = new ContentValues();
//                    values.put("odate", orderDates[i]);
//                    values.put("custid", customerIds[i]);
//                    values.put("ordertype", orderTypes[i]);
//                    values.put("ordercost", orderCosts[i]);
//                    values.put("order_status", orderStatuses[i]);
//                    values.put("webfree", webFrees[i]);
//                    values.put("gst", gsts[i]);
//                    values.put("delcost", delCosts[i]);
//                    values.put("discount", discounts[i]);
//                    values.put("cnote", cNotes[i]);
//                    values.put("suburb", suburbs[i]);
//                    values.put("paymode", payModes[i]);
//                    values.put("txn_id", txnIds[i]);
//                    values.put("payment_status", paymentStatuses[i]);
//                    values.put("payment_type", paymentTypes[i]);
//                    values.put("payer_status", payerStatuses[i]);
//                    values.put("payer_email", payerEmails[i]);
//                    values.put("redeem_amt", redeemAmts[i]);
//                    values.put("coupon_code", couponCodes[i]);
//                    values.put("dvdate", dvDates[i]);
//                    values.put("uent_dt", uentDts[i]);
//                    values.put("cashamt", cashAmts[i]);
//                    values.put("eftpos", eftposAmts[i]);
//                    values.put("server_type", serverTypes[i]);
//                    values.put("refund_amt", refundAmts[i]);
//                    values.put("refund_type", refundTypes[i]);
//                    values.put("refund_note", refundNotes[i]);
//                    values.put("refund_date", refundDates[i]);
//                    values.put("edate", eDates[i]);
//                    values.put("onaccount", onAccounts[i]);
//                    values.put("surcharge_cost", surchargeCosts[i]);
//                    values.put("paycost", payCosts[i]);
//
//                    db.insert("order_data", null, values);
//                }
//
//                // Close the database after inserting data
//                db.close();
//                displayTableData("All Orders");
//            }
//        });

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
        // Show all orders by default
        toggleFilter("All Orders");

    }

    private void toggleFilter(String filterType) {
        if (filterType.equals("All Orders")) {
            // For "All Orders," unselect all other filters and display all data
            selectedFilters.clear();
            selectedFilters.add(filterType);
            selectedFilters.remove("All Orders");

        } else if (filterType.equals("Active Orders") || filterType.equals("Completed")) {
            // For "Active Orders" and "Completed," allow only single selection
//            selectedFilters.remove("All Orders");
//            selectedFilters.remove("Active Orders");
//            selectedFilters.remove("Completed");
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
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM order_data");
        if (!selectedFilters.isEmpty()&&!selectedFilters.contains("All Orders")) {
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
        if(selectedFilters.contains("All Orders")) {
            queryBuilder = new StringBuilder("SELECT * FROM order_data");
        }
        Cursor cursor = db.rawQuery(queryBuilder.toString(), null);
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

                if (customerCursor.moveToFirst()) {
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
                    createActionButton(dataRow, "Edit");

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
        textView.setPadding(16, 16, 16, 16);
        textView.setTypeface(null, Typeface.BOLD);
        row.addView(textView);
    }
    private void addDataTextView (TableRow row, String text){
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 16, 16, 16);
        row.addView(textView);
    }
    private void createActionButton (TableRow row, String text){
        AppCompatButton editButton = new AppCompatButton(this, null);
        editButton.setText(text);
        row.addView(editButton);
    }
}