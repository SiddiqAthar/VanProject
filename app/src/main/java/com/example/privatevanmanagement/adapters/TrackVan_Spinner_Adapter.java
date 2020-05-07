package com.example.privatevanmanagement.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.privatevanmanagement.R;
import com.example.privatevanmanagement.models.DriverDetail_Model;
import com.example.privatevanmanagement.models.Shift_Model;

import java.util.ArrayList;
import java.util.List;

public class TrackVan_Spinner_Adapter extends ArrayAdapter<DriverDetail_Model> {

    List<DriverDetail_Model> mylist;
    private Context mContext;

    public TrackVan_Spinner_Adapter(@NonNull Context context, int resource, List<DriverDetail_Model> arrayList) {
        super(context, resource, arrayList);
        this.mylist = arrayList;
        this.mContext = context;

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        @SuppressLint("ViewHolder")
        View view = mInflater.inflate(R.layout.spinner_item_layout, parent, false);
        TextView textView = view.findViewById(R.id.spinner_item_tv);
        if (mylist.get(position).getAssigned_status().equals(""))
            mylist.remove(position);
        else {
            textView.setText(mylist.get(position).getVan_number());
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item_layout, parent, false);
        }
        TextView txtVanNumber = (TextView) convertView.findViewById(R.id.spinner_item_tv);

        if (mylist.get(position).getAssigned_status().equals(""))
            mylist.remove(position);
        else {
            txtVanNumber.setText(mylist.get(position).getVan_number());
        }
        return convertView;

    }

}
