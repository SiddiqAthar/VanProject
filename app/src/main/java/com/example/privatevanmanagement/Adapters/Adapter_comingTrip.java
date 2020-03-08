package com.example.privatevanmanagement.Adapters;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.privatevanmanagement.Fragments.admin.AddStudent;
import com.example.privatevanmanagement.R;
import com.example.privatevanmanagement.activities.NavDrawer;
import com.example.privatevanmanagement.models.ManageFee_Model;

import java.util.ArrayList;

public class Adapter_comingTrip extends RecyclerView.Adapter<Adapter_comingTrip.tripStudent_viewHolder> {


    ArrayList<ManageFee_Model> manageStudent_List;
    Context context;
    Button btn_block_unBLock;
    Button btn_edit;


    public Adapter_comingTrip(ArrayList<ManageFee_Model> manageStudent_List, Context context) {
        this.manageStudent_List = manageStudent_List;
        this.context = context;
    }

    @NonNull
    @Override
    public tripStudent_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_trip_student, parent, false);
        tripStudent_viewHolder viewHolder = new tripStudent_viewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull tripStudent_viewHolder holder, final int position) {
        holder.studentName.setText(manageStudent_List.get(position).getName());
        holder.studentAddress.setText(manageStudent_List.get(position).getId());
     }

    @Override
    public int getItemCount() {
        return manageStudent_List.size();
    }

    public static class tripStudent_viewHolder extends RecyclerView.ViewHolder {

        TextView studentName;
        TextView studentAddress;

        public tripStudent_viewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = (TextView) itemView.findViewById(R.id.studentName);
            studentAddress = (TextView) itemView.findViewById(R.id.studentAddress);
         }
    }

  }