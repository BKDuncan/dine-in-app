package com.example.bailey.dine_in_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Anil Sood on 12/8/2017.
 */

public class ReservationListAdapter extends ArrayAdapter<ReservationListInfo> {

    private Context mContext;
    private int mResource;

    public ReservationListAdapter(Context context, int resource, ArrayList<ReservationListInfo> objects)
    {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        int resId = getItem(position).getResId();
        String name = getItem(position).getName();
        String date = getItem(position).getDate();
        String time = getItem(position).getTime();

        ReservationListInfo reservation = new ReservationListInfo(resId, name, date, time);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView n = (TextView) convertView.findViewById(R.id.restaurant_name);
        n.setText(name);
        TextView d = (TextView) convertView.findViewById(R.id.res_date);
        d.setText(date);
        TextView t = (TextView) convertView.findViewById(R.id.res_time);
        t.setText(time);

        return convertView;
    }

}
