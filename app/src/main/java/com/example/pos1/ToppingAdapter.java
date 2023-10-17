package com.example.pos1;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class ToppingAdapter extends BaseAdapter {

    private Context context;
    private List<String> toppingNames;

    public ToppingAdapter(Context context, List<String> toppingNames) {
        this.context = context;
        this.toppingNames = toppingNames;
    }

    @Override
    public int getCount() {
        return toppingNames.size();
    }

    @Override
    public Object getItem(int position) {
        return toppingNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.topping_item_layout, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.toppingNameTextView = convertView.findViewById(R.id.toppingNameTextView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String toppingName = toppingNames.get(position);
        viewHolder.toppingNameTextView.setText(toppingName);

        return convertView;
    }

    static class ViewHolder {
        TextView toppingNameTextView;
    }
}
