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

public class TransactionListAdapter extends ArrayAdapter<TransactionListInfo> {
    private Context mContext;
    private int mResource;

    public TransactionListAdapter(Context context, int resource, ArrayList<TransactionListInfo> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int tNum = getItem(position).getTransaction_id();
        String payT = getItem(position).getPayment_type();
        String tip = getItem(position).getTip();
        String price = getItem(position).getTotal();

        OrderListInfo orderListInfo = new OrderListInfo(tNum, payT, tip, price);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView oN = (TextView) convertView.findViewById(R.id.t_id_adapter);
        TextView oD = (TextView) convertView.findViewById(R.id.t_payment_type_adapter);
        TextView oT = (TextView) convertView.findViewById(R.id.t_tip_adapter);
        TextView oTO = (TextView) convertView.findViewById(R.id.t_total_adapter);

        oN.setText(String.valueOf(tNum));
        oD.setText(payT);
        oT.setText(tip);
        oTO.setText(price);

        return convertView;
    }
}
