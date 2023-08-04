package com.example.pos1;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ToppingTradePrice extends AppCompatActivity {

    private TableLayout tnameTableLayout;
    private TableLayout sznameTableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topping_trade_price);

        tnameTableLayout = findViewById(R.id.toppingTableLayout);
        sznameTableLayout = findViewById(R.id.toppingSizeTableLayout);

        displayTnameData();
    }

    private void displayTnameData() {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT tid, tname FROM topping_mast", null);
        TableRow tnameRow2 = new TableRow(this);

        TextView tnameHeading = new TextView(this);
        tnameHeading.setText("Topping Name");
        tnameHeading.setPadding(16, 16, 16, 16);
        tnameHeading.setTypeface(null, Typeface.BOLD);

        tnameRow2.addView(tnameHeading);
        tnameTableLayout.addView(tnameRow2);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") int tid = cursor.getInt(cursor.getColumnIndex("tid"));
            @SuppressLint("Range") String tname = cursor.getString(cursor.getColumnIndex("tname"));

            TableRow tnameRow = new TableRow(this);
            tnameRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            TextView tnameTextView = new TextView(this);
            tnameTextView.setText(tname);
            tnameTextView.setPadding(16, 16, 16, 16);
            tnameTextView.setTypeface(null, Typeface.BOLD);

            tnameTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Clear previous selection
                    clearTnameSelection();

                    // Highlight the selected tname
                    tnameTextView.setBackgroundColor(Color.GRAY);

                    displaySznameData(tid);
                }
            });

            tnameRow.addView(tnameTextView);
            tnameTableLayout.addView(tnameRow);
        }

        cursor.close();
        db.close();
    }

    private void clearTnameSelection() {
        int rowCount = tnameTableLayout.getChildCount();
        for (int i = 0; i < rowCount; i++) {
            View view = tnameTableLayout.getChildAt(i);
            if (view instanceof TableRow) {
                TableRow row = (TableRow) view;
                int childCount = row.getChildCount();
                for (int j = 0; j < childCount; j++) {
                    View childView = row.getChildAt(j);
                    if (childView instanceof TextView) {
                        childView.setBackgroundColor(Color.TRANSPARENT);
                    }
                }
            }
        }
    }

    private void displaySznameData(int tid) {
        sznameTableLayout.removeAllViews();

        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT size_mast.szname, topping_info.p_plus, topping_info.p_double, topping_info.p_h_plus, topping_info.p_h_double " +
                "FROM size_mast " +
                "LEFT JOIN topping_info ON size_mast.szid = topping_info.szid AND topping_info.topp_id = ?", new String[]{String.valueOf(tid)});

        TableRow SizeRow2 = new TableRow(this);

        TextView snameHeading = new TextView(this);
        snameHeading.setText("Size");
        snameHeading.setPadding(16, 16, 16, 16);
        snameHeading.setTypeface(null, Typeface.BOLD);
        SizeRow2.addView(snameHeading);


        TextView pp = new TextView(this);
        pp.setText("Price Plue");
        pp.setPadding(16, 16, 16, 16);
        pp.setTypeface(null, Typeface.BOLD);
        SizeRow2.addView(pp);

        TextView pd = new TextView(this);
        pd.setText("Price Double");
        pd.setPadding(16, 16, 16, 16);
        pd.setTypeface(null, Typeface.BOLD);
        SizeRow2.addView(pd);

        TextView ph = new TextView(this);
        ph.setText("Price Half Plus");
        ph.setPadding(16, 16, 16, 16);
        ph.setTypeface(null, Typeface.BOLD);
        SizeRow2.addView(ph);

        TextView phd = new TextView(this);
        phd.setText("Price Half Plus");
        phd.setPadding(16, 16, 16, 16);
        phd.setTypeface(null, Typeface.BOLD);
        SizeRow2.addView(phd);

        sznameTableLayout.addView(SizeRow2);

        while (cursor.moveToNext()) {
            @SuppressLint("Range") String szname = cursor.getString(cursor.getColumnIndex("szname"));
            @SuppressLint("Range") String p_plus = cursor.getString(cursor.getColumnIndex("p_plus"));
            @SuppressLint("Range") String p_double = cursor.getString(cursor.getColumnIndex("p_double"));
            @SuppressLint("Range") String p_h_plus = cursor.getString(cursor.getColumnIndex("p_h_plus"));
            @SuppressLint("Range") String p_h_double = cursor.getString(cursor.getColumnIndex("p_h_double"));

            TableRow sznameRow = new TableRow(this);
            sznameRow.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT
            ));

            TextView sznameTextView = new TextView(this);
            sznameTextView.setText(szname);
            sznameTextView.setPadding(16, 16, 16, 16);

            EditText pPlusEditText = new EditText(this);
            pPlusEditText.setText(p_plus);
            pPlusEditText.setPadding(16, 16, 16, 16);
            pPlusEditText.setBackgroundResource(R.drawable.edittext_background);
            pPlusEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


            EditText pDoubleEditText = new EditText(this);
            pDoubleEditText.setText(p_double);
            pDoubleEditText.setPadding(16, 16, 16, 16);
            pDoubleEditText.setBackgroundResource(R.drawable.edittext_background);
            pDoubleEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//            pDoubleEditText.addTextChangedListener(new PriceTextWatcher(tid, szname, "p_double"));

            EditText pHPlusEditText = new EditText(this);
            pHPlusEditText.setText(p_h_plus);
            pHPlusEditText.setPadding(16, 16, 16, 16);
            pHPlusEditText.setBackgroundResource(R.drawable.edittext_background);
            pHPlusEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//            pHPlusEditText.addTextChangedListener(new PriceTextWatcher(tid, szname, "p_h_plus"));

            EditText pHDoubleEditText = new EditText(this);
            pHDoubleEditText.setText(p_h_double);
            pHDoubleEditText.setPadding(16, 16, 16, 16);
            pHDoubleEditText.setBackgroundResource(R.drawable.edittext_background);
            pHDoubleEditText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
//            pHDoubleEditText.addTextChangedListener(new PriceTextWatcher(tid, szname, "p_h_double"));

            pPlusEditText.addTextChangedListener(new PriceTextWatcher(tid, szname, "p_plus", pDoubleEditText, pHPlusEditText, pHDoubleEditText));
            pDoubleEditText.addTextChangedListener(new PriceTextWatcher(tid, szname, "p_double", null, null, null));
            pHPlusEditText.addTextChangedListener(new PriceTextWatcher(tid, szname, "p_h_plus", null, null, null));
            pHDoubleEditText.addTextChangedListener(new PriceTextWatcher(tid, szname, "p_h_double", null, null, null));

            sznameRow.addView(sznameTextView);
            sznameRow.addView(pPlusEditText);
            sznameRow.addView(pDoubleEditText);
            sznameRow.addView(pHPlusEditText);
            sznameRow.addView(pHDoubleEditText);

            sznameTableLayout.addView(sznameRow);
        }

        cursor.close();
        db.close();
    }

    private class PriceTextWatcher implements TextWatcher {
        private int toppingId;
        private String szname;
        private String column;

        private EditText pDoubleEditText;
        private EditText pHPlusEditText;
        private EditText pHDoubleEditText;

        public PriceTextWatcher(int toppingId, String szname, String column, EditText pDoubleEditText, EditText pHPlusEditText, EditText pHDoubleEditText) {
            this.toppingId = toppingId;
            this.szname = szname;
            this.column = column;
            this.pDoubleEditText = pDoubleEditText;
            this.pHPlusEditText = pHPlusEditText;
            this.pHDoubleEditText = pHDoubleEditText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Not needed, but must be implemented
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // Not needed, but must be implemented
        }

        @Override
        public void afterTextChanged(Editable s) {
            String newPrice = s.toString();
            updatePrice(toppingId, szname, column, newPrice);

            // Update p_double
            if (column.equals("p_plus")) {
                String pPlusValue = newPrice.trim();
                if (!pPlusValue.isEmpty()) {
                    double pDoubleValue = Double.parseDouble(pPlusValue) * 2;
                    pDoubleEditText.setText(String.valueOf(pDoubleValue));
                } else {
                    pDoubleEditText.setText("");
                }
            }

            // Update p_h_plus
            if (column.equals("p_plus")) {
                String pPlusValue = newPrice.trim();
                if (!pPlusValue.isEmpty()) {
                    double pHPlusValue = Double.parseDouble(pPlusValue) / 2;
                    pHPlusEditText.setText(String.valueOf(pHPlusValue));
                } else {
                    pHPlusEditText.setText("");
                }
            }

            // Update p_h_double
            if (column.equals("p_plus")) {
                String pPlusValue = newPrice.trim();
                if (!pPlusValue.isEmpty()) {
                    pHDoubleEditText.setText(pPlusValue);
                } else {
                    pHDoubleEditText.setText("");
                }
            }
        }
    }

    @SuppressLint("Range")
    private void updatePrice(int toppingId, String szname, String column, String newPrice) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        ContentValues values = new ContentValues();
        values.put(column, newPrice);

        // Get the szid based on szname
        Cursor cursor = db.rawQuery("SELECT szid FROM size_mast WHERE szname = ?", new String[]{szname});
        int szid = -1;
        if (cursor.moveToFirst()) {
            szid = cursor.getInt(cursor.getColumnIndex("szid"));
        }
        cursor.close();

        if (szid != -1) {
            // Check if the record already exists
            cursor = db.rawQuery("SELECT * FROM topping_info WHERE topp_id = ? AND szid = ?", new String[]{String.valueOf(toppingId), String.valueOf(szid)});
            boolean recordExists = cursor.moveToFirst();
            cursor.close();

            if (recordExists) {
                // Update the existing record
                int rowsAffected = db.update("topping_info", values, "topp_id = ? AND szid = ?", new String[]{String.valueOf(toppingId), String.valueOf(szid)});
                if (rowsAffected > 0) {
                    Toast.makeText(this, "Price updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to update price", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Create a new record
                values.put("topp_id", toppingId);
                values.put("szid", szid);
                long rowId = db.insert("topping_info", null, values);
                if (rowId != -1) {
                    Toast.makeText(this, "New record created successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to create new record", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Toast.makeText(this, "Invalid szid", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}
