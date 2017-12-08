package com.example.bailey.dine_in_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anil Sood on 12/7/2017.
 */

public class TableListAdapter extends ArrayAdapter<TableListInfo> {

    private Context mContext;
    private int mResource;
    public TableListAdapter(Context context, int resource, ArrayList<TableListInfo> tables)
    {
        super(context, resource, tables);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String table_number = getItem(position).getTableNumber();
        String seats = getItem(position).getSeats();
        String available = getItem(position).getAvailable();

        TableListInfo tableInfo = new TableListInfo(table_number, seats, available);
        LayoutInflater inflater = LayoutInflater.from(mContext);

        convertView = inflater.inflate(mResource, parent, false);

        TextView table_number_view = (TextView) convertView.findViewById(R.id.table_number_text);
        TextView seats_view = (TextView) convertView.findViewById(R.id.seats_text);
        TextView available_view = (TextView) convertView.findViewById(R.id.available_text);

        table_number_view.setText(table_number);
        seats_view.setText(seats);
        available_view.setText(available);

        return convertView;
    }

}
