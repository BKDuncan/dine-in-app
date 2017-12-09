package com.example.bailey.dine_in_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anil Sood on 12/8/2017.
 */

public class OrderListAdapter extends ArrayAdapter<OrderListInfo> {
    private Context mContext;
    private int mResource;

    public OrderListAdapter(Context context, int resource, ArrayList<OrderListInfo> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int oNum = getItem(position).getOrderNum();
        String date = getItem(position).getDate();
        String time = getItem(position).getTime();
        String price = getItem(position).getTotal();

        OrderListInfo orderListInfo = new OrderListInfo(oNum, date, time, price);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView oN = (TextView) convertView.findViewById(R.id.number_adapter);
        TextView oD = (TextView) convertView.findViewById(R.id.date_adapter);
        TextView oT = (TextView) convertView.findViewById(R.id.time_adapter);
        TextView oTO = (TextView) convertView.findViewById(R.id.total_adapter);

        oN.setText(String.valueOf(oNum));
        oD.setText(date);
        oT.setText(time);
        oTO.setText(price);

        return convertView;
    }
}
