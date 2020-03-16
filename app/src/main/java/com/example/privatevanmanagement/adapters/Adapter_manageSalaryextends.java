package com.example.privatevanmanagement.adapters;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.privatevanmanagement.R;
import com.example.privatevanmanagement.models.ManageFee_Model;
import com.example.privatevanmanagement.models.ManageSalary_Model;

import java.util.ArrayList;

public class Adapter_manageSalaryextends extends RecyclerView.Adapter<Adapter_manageSalaryextends.manageSalaryextends_viewHolder> {


    ArrayList<ManageSalary_Model> manageSalary_List;
Context context;
    public Adapter_manageSalaryextends(ArrayList<ManageSalary_Model> manageSalary_List, Context context) {
        this.manageSalary_List = manageSalary_List;
        this.context=context;
    }

    @NonNull
    @Override
    public manageSalaryextends_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_manage_salary, parent, false);
        manageSalaryextends_viewHolder viewHolder= new manageSalaryextends_viewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull manageSalaryextends_viewHolder holder, final int position) {
        holder.driverName.setText(manageSalary_List.get(position).getName());
        holder.driverStatus.setText(manageSalary_List.get(position).getStatus());
        holder.edit_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogManageFee(manageSalary_List.get(position).getName());
            }
        });
    }

    @Override
    public int getItemCount() {
        return manageSalary_List.size();
    }

    public static class manageSalaryextends_viewHolder extends RecyclerView.ViewHolder {

        TextView driverName;
        TextView driverStatus;
        Button edit_driver;


        public manageSalaryextends_viewHolder(@NonNull View itemView) {
            super(itemView);
            driverName = (TextView) itemView.findViewById(R.id.driverName);
            driverStatus = (TextView) itemView.findViewById(R.id.driverStatus);
            edit_driver = (Button) itemView.findViewById(R.id.edit_driver);
        }
    }


    public void showDialogManageFee(String name) {

        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_manage_salary);


        EditText et_driver_name = (EditText) dialog.findViewById(R.id.et_driver_name);
        EditText et_driver_new = (EditText) dialog.findViewById(R.id.et_driver_new);
        Spinner spinner_driver_status = (Spinner) dialog.findViewById(R.id.spinner_driver_status);
        ImageButton btn_closeDialog = (ImageButton) dialog.findViewById(R.id.btn_closeDialog);


        et_driver_name.setText(name);

        Button btn_driver_managed = (Button) dialog.findViewById(R.id.btn_driver_managed);
        btn_driver_managed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //data update here then close dialog
                Toast.makeText(context, "Salary Details Updated", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });


        ArrayAdapter<String> arrayAdapter;
        ArrayList<String> status_Array = new ArrayList<>();
        status_Array.add("Paid");
        status_Array.add("Un-Paid");
        arrayAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_list_item_1,
                status_Array
        );
        spinner_driver_status.setAdapter(arrayAdapter);
        spinner_driver_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (dialog.isShowing()) {
            dialog.dismiss();
        } else {
            dialog.show();
            dialog.getWindow().setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
        }
    }


}