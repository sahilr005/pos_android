package com.example.pos1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

public class ItemAdapter extends BaseAdapter {
    private Context context;
    private List<ItemModel> itemList;
    private AlertDialog dialog;

    public ItemAdapter(Context context, List<ItemModel> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemList.get(position).getTid();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView textView = new TextView(context);
        final ItemModel item = itemList.get(position);

        textView.setBackgroundResource(R.drawable.button_shape);
        textView.setPadding(26, 36, 26, 36);
        textView.setTextColor(Color.WHITE);

        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(item.getName());
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showItemNameDialog(item);
            }
        });

        return textView;
    }
    private void showItemNameDialog(ItemModel item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = View.inflate(context, R.layout.counter_item_dialog, null);

        // Find views in the custom layout
        TextView itemNameTextView = dialogView.findViewById(R.id.item_name);
        TextView itemContainTextView = dialogView.findViewById(R.id.item_contain);
        TextView itemOptionsTextView = dialogView.findViewById(R.id.item_options);
        Button selectButton = dialogView.findViewById(R.id.select_button);

        // Set item name and additional data
        itemNameTextView.setText(item.getName());
        itemContainTextView.setText("Item Contains: " + item.getContain());
        itemOptionsTextView.setText("Extra: " + item.getExtra());

        // Handle the "Select" button click
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the selection logic here
                // You can perform any actions or send data back as needed
                // For example, you can close the dialog and perform an action based on the selection
                dialog.dismiss();
                // Implement your logic here
            }
        });

        builder.setView(dialogView);
        builder.setTitle("Item Details");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog = builder.create();
        dialog.show();
    }

}


