package com.example.jaishankar.jdeapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jaishankar on 18/3/15.
 */
public class SKUListAdapter extends ArrayAdapter<SKU> {
    ArrayList<SKU> SKUList;
    Context context;
    int resource;

    public SKUListAdapter(Context context, int resource, List<SKU> objects) {
        super(context, resource, objects);
        SKUList = (ArrayList)objects;
        this.context = context;
        this.resource = resource;
    }



    @Override
    public View getView(int pos, View v, ViewGroup parent){
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = layoutInflater.inflate(resource, parent, false);
//        TextView title = (TextView)rowView.findViewById(R.id.rowTitle);
//        title.setText(SKUList.get(pos).getName());
        TextView amount = (TextView)rowView.findViewById(R.id.rowAmount);
        amount.setText(Double.toString(SKUList.get(pos).getAmount()));
        TextView curr = (TextView)rowView.findViewById(R.id.rowCurrency);
        curr.setText(SKUList.get(pos).getCurrency());
        return rowView;
    }
}
