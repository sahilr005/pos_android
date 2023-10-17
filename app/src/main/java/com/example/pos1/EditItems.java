package com.example.pos1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.chip.Chip;
import com.google.android.flexbox.FlexboxLayout;

public class EditItems extends AppCompatActivity {
    private int selectedButton = 1;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_items);

        Button buttonSave = findViewById(R.id.buttonSave);
        EditText editTextCategory = findViewById(R.id.editTextCategory);
        EditText editTextName = findViewById(R.id.editTextItemName);
        EditText editTextStatus = findViewById(R.id.editTextStatus);
        EditText editTextPickupPrice = findViewById(R.id.editTextPickupPrice);
        EditText editTextDeliveryPrice = findViewById(R.id.editTextDeliveryPrice);
        EditText editTextEatInPrice = findViewById(R.id.editTextEatInPrice);
        FlexboxLayout chipToppingContainer = findViewById(R.id.chipContainer);
        FlexboxLayout chipToppingExtra = findViewById(R.id.chipToppingExtra);

        int itemId = getIntent().getIntExtra("itemId", -1);

        AppCompatButton buttonContains = findViewById(R.id.ContainsBtn);
        AppCompatButton buttonExtra = findViewById(R.id.ExtraBtn);
        AppCompatButton buttonSelectAny = findViewById(R.id.SelectAnyBtn);
        AppCompatButton buttonPrice = findViewById(R.id.PriceBtn);

        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        Cursor cursor = db.rawQuery("SELECT * FROM item_master WHERE tid = ?", new String[]{String.valueOf(itemId)});
        Cursor toppingCursor = db.rawQuery("SELECT * FROM topping_mast", null);
        while (toppingCursor.moveToNext()) {
            int toppingId = toppingCursor.getInt(toppingCursor.getColumnIndex("tid"));
            String toppingName = toppingCursor.getString(toppingCursor.getColumnIndex("tname"));

            Chip chip = new Chip(this);
            chip.setText(toppingName);
            chip.setClickable(true);
            chip.setCheckable(true);
            chip.setTag(toppingId);

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Chip selectedChip = (Chip) view;
                    boolean isSelected = selectedChip.isChecked();
                    if (isSelected) {
                        updateContain(view, itemId);
                    } else {
                        removeContainTopping(toppingId, itemId);
                    }
                }
            });
            chipToppingContainer.addView(chip);
        }   while (toppingCursor.moveToNext()) {
            int toppingId = toppingCursor.getInt(toppingCursor.getColumnIndex("tid"));
            String toppingName = toppingCursor.getString(toppingCursor.getColumnIndex("tname"));

            Chip chip = new Chip(this);
            chip.setText(toppingName);
            chip.setClickable(true);
            chip.setCheckable(true);
            chip.setTag(toppingId);

            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Chip selectedChip = (Chip) view;
                    boolean isSelected = selectedChip.isChecked();
                    if (isSelected) {
                        updateContain(view, itemId);
                    } else {
                        removeContainTopping(toppingId, itemId);
                    }
                }
            });

            chipToppingExtra.addView(chip);
        }

        buttonContains.setBackgroundColor(getResources().getColor(R.color.purple_500));
        buttonContains.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonContains.setBackgroundColor(getResources().getColor(R.color.purple_500));
                selectedButton = 1;
                buttonExtra.setBackgroundColor(Color.GRAY);
                buttonSelectAny.setBackgroundColor(Color.GRAY);
                buttonPrice.setBackgroundColor(Color.GRAY);
                chipToppingExtra.setVisibility(View.GONE);
                chipToppingContainer.setVisibility(View.VISIBLE);
            }
        });

        buttonExtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedButton = 2;
                buttonExtra.setBackgroundColor(getResources().getColor(R.color.purple_500));

                buttonContains.setBackgroundColor(Color.GRAY);
                buttonSelectAny.setBackgroundColor(Color.GRAY);
                buttonPrice.setBackgroundColor(Color.GRAY);
                chipToppingContainer.setVisibility(View.GONE);
                chipToppingExtra.setVisibility(View.VISIBLE);
            }
        });


        buttonSelectAny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedButton = 3;
                buttonSelectAny.setBackgroundColor(getResources().getColor(R.color.purple_500));

                buttonContains.setBackgroundColor(Color.GRAY);
                buttonExtra.setBackgroundColor(Color.GRAY);
                buttonPrice.setBackgroundColor(Color.GRAY);
            }
        });

        buttonPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonPrice.setBackgroundColor(getResources().getColor(R.color.purple_500));

                buttonContains.setBackgroundColor(Color.GRAY);
                buttonExtra.setBackgroundColor(Color.GRAY);
                buttonSelectAny.setBackgroundColor(Color.GRAY);
            }
        });

        if (cursor.moveToFirst()) {
            String category = cursor.getString(cursor.getColumnIndex("code"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            int status = cursor.getInt(cursor.getColumnIndex("status"));
            String pickupPrice = cursor.getString(cursor.getColumnIndex("pickup_price"));
            String deliveryPrice = cursor.getString(cursor.getColumnIndex("delivery_price"));
            String eatInPrice = cursor.getString(cursor.getColumnIndex("eat_in_price"));
            String containValue = cursor.getString(cursor.getColumnIndex("contain"));

            editTextCategory.setText(category);
            editTextName.setText(name);
            editTextStatus.setText(String.valueOf(status));
            editTextPickupPrice.setText(pickupPrice);
            editTextDeliveryPrice.setText(deliveryPrice);
            editTextEatInPrice.setText(eatInPrice);

            if (containValue != null) {
                String[] containArray = containValue.split(",");
                for (String contain : containArray) {
                    Log.d("containValue .. .", "onCreate: "+contain);
                    if (!contain.isEmpty()) {
                    int toppingId = Integer.parseInt(contain);
                    selectChipByTag(toppingId);
                    }
                }
            }

        }
        cursor.close();
        db.close();

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

                ContentValues values = new ContentValues();
                values.put("cat_id", editTextCategory.getText().toString());
                values.put("name", editTextName.getText().toString());
                values.put("status", Integer.parseInt(editTextStatus.getText().toString()));
                values.put("pickup_price", editTextPickupPrice.getText().toString());
                values.put("delivery_price", editTextDeliveryPrice.getText().toString());
                values.put("eat_in_price", editTextEatInPrice.getText().toString());

                int rowsAffected = db.update("item_master", values, "tid = ?", new String[]{String.valueOf(itemId)});

                if (rowsAffected > 0) {
                    Toast.makeText(EditItems.this, "Update successful", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditItems.this, "Update failed", Toast.LENGTH_SHORT).show();
                }

                db.close();
            }
        });
    }

    public void updateContain(View view, int itemId) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);
        FlexboxLayout chipContainer = findViewById(R.id.chipContainer);
        int childCount = chipContainer.getChildCount();
        StringBuilder containBuilder = new StringBuilder();
        StringBuilder extraBuilder = new StringBuilder();

        for (int i = 0; i < childCount; i++) {
            Chip chip = (Chip) chipContainer.getChildAt(i);
            if (chip.isChecked()) {
                int toppingId = (int) chip.getTag();
                if (selectedButton == 1) {
                    containBuilder.append(toppingId).append(",");
                } else if (selectedButton == 2) {
                    extraBuilder.append(toppingId).append(",");
                }
            }
        }

        if (containBuilder.length() > 0) {
            containBuilder.setLength(containBuilder.length() - 1);
        }
        if (extraBuilder.length() > 0) {
            extraBuilder.setLength(extraBuilder.length() - 1);
        }

        String containValue = containBuilder.toString();
        String extraValue = extraBuilder.toString();

        ContentValues values = new ContentValues();
        values.put("contain", containValue);

        if (selectedButton == 1) {
            values.put("extra", extraValue);
        }

        db.update("item_master", values, "tid=?", new String[]{String.valueOf(itemId)});
        db.close();

        Toast.makeText(this, "Contain value updated", Toast.LENGTH_SHORT).show();
    }

    private void selectChipByTag(int toppingId) {
        FlexboxLayout chipContainer = findViewById(R.id.chipContainer);
        int childCount = chipContainer.getChildCount();
        Log.i(".q.a.a.s.c.x.c.v..... ", " . "+toppingId +"selectChipByTag: "+childCount);
        for (int i = 0; i < childCount; i++) {
        Log.d("SELELELELELELLELEELLEL ..", "selectChipByTag: ");
            Chip chip = (Chip) chipContainer.getChildAt(i);
            int chipToppingId = (int) chip.getTag();
            Log.d("chipToppingId -- ", "selectChipByTag: "+Integer.toString(chipToppingId));
            Log.d("toppingId -- ", "toppingId : "+Integer.toString(toppingId));
            if (chipToppingId == toppingId) {
                chip.setChecked(true);
                chip.isChecked();
                break;
            }
        }
    }
    @SuppressLint("Range")
    private void removeContainTopping(int toppingId, int itemId) {
        SQLiteDatabase db = openOrCreateDatabase("mydatabase.db", MODE_PRIVATE, null);

        // Retrieve the current contain value from the item_master table
        Cursor cursor = db.rawQuery("SELECT contain FROM item_master WHERE tid = ?", new String[]{String.valueOf(itemId)});
        if (cursor.moveToFirst()) {
            String containValue = cursor.getString(cursor.getColumnIndex("contain"));

            // Remove the toppingId from the containValue string
            containValue = containValue.replace(toppingId + ",", "");
            containValue = containValue.replace("," + toppingId, "");
            containValue = containValue.replace(String.valueOf(toppingId), "");

            // Update the contain column in the item_master table
            ContentValues values = new ContentValues();
            values.put("contain", containValue);

            int rowsAffected = db.update("item_master", values, "tid = ?", new String[]{String.valueOf(itemId)});

            if (rowsAffected > 0) {
                Toast.makeText(EditItems.this, "Contain topping removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditItems.this, "Failed to remove contain topping", Toast.LENGTH_SHORT).show();
            }
        }

        cursor.close();
        db.close();
    }
}
