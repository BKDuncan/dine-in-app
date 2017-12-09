package com.example.bailey.dine_in_app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * Created by Anil Sood on 12/8/2017.
 */

public class RestaurantListAdapter extends ArrayAdapter<RestaurantListInfo> {

    private Context mContext;
    private int mResource;

    public RestaurantListAdapter(Context context, int resource, ArrayList<RestaurantListInfo> objects) {
        super(context, resource, objects);
        this.mContext = context;
        mResource = resource;
    }


    @Override
    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        int resId = getItem(position).getRes_Id();
        String name = getItem(position).getName();
        String hours = getItem(position).getHours();
        String phone = getItem(position).getPhone();

        RestaurantListInfo restaurant = new RestaurantListInfo(resId, name, hours, phone);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView resname = convertView.findViewById(R.id.restaurant_name_adapter_text);
        resname.setText(name);
        TextView reshours = convertView.findViewById(R.id.restaurant_hours_adapter_text);
        reshours.setText(hours);
        TextView resphone = convertView.findViewById(R.id.restaurant_phone_adapter_text);
        resphone.setText(phone);

        return convertView;
    }
}
