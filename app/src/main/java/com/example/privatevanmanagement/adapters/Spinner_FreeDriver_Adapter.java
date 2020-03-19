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
import com.example.privatevanmanagement.models.VanDetail_Model;

import java.util.ArrayList;

public class Spinner_FreeDriver_Adapter extends ArrayAdapter<DriverDetail_Model> {

    ArrayList<DriverDetail_Model> arrayList;
    private Context mContext;

    public Spinner_FreeDriver_Adapter(@NonNull Context context, int resource, int resource2, ArrayList<DriverDetail_Model> arrayList) {
        super(context, resource, resource2, arrayList);
        this.arrayList = arrayList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater mInflater = LayoutInflater.from(parent.getContext());
        @SuppressLint("ViewHolder")
        View view = mInflater.inflate(R.layout.spinner_item_layout, parent, false);
        TextView textView = view.findViewById(R.id.spinner_item_tv);
        textView.setText(arrayList.get(position).getDriver_name());
         return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.spinner_item_layout, parent, false);
        }
        TextView txtShiftName = (TextView) convertView.findViewById(R.id.spinner_item_tv);
         txtShiftName.setText(arrayList.get(position).getDriver_name());

        return convertView;

    }
}
