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
import com.example.privatevanmanagement.models.StudentDetail_Model;
import com.example.privatevanmanagement.utils.Objects;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter_manageFeeextends extends RecyclerView.Adapter<Adapter_manageFeeextends.manageFeeextends_viewHolder> {


    ArrayList<StudentDetail_Model> manageFee_List;
    Context context;
    String new_status;


    public Adapter_manageFeeextends(List<StudentDetail_Model> manageFee_List, Context context) {
        this.manageFee_List = (ArrayList<StudentDetail_Model>) manageFee_List;
        this.context = context;
    }

    @NonNull
    @Override
    public manageFeeextends_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_manage_fee, parent, false);
        manageFeeextends_viewHolder viewHolder = new manageFeeextends_viewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull manageFeeextends_viewHolder holder, final int position) {
        holder.userName.setText(manageFee_List.get(position).getStudent_name());
        holder.userAmmount.setText(manageFee_List.get(position).getAmmount());
        holder.userStatus.setText(manageFee_List.get(position).getFee_status());

        holder.shiftTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogManageFee(manageFee_List.get(position).getStudent_name(), manageFee_List.get(position).getFee_status(), manageFee_List.get(position).getStudent_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return manageFee_List.size();
    }

    public static class manageFeeextends_viewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView userAmmount;
        TextView userStatus;
        Button shiftTime;

        public manageFeeextends_viewHolder(@NonNull View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.userName);
            userAmmount = (TextView) itemView.findViewById(R.id.userAmmount);
            userStatus = (TextView) itemView.findViewById(R.id.userStatus);
            shiftTime = (Button) itemView.findViewById(R.id.shiftTime);
        }
    }


    public void showDialogManageFee(String name, String status, final String student_id) {

        new_status = status;
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_manage_fee);


        EditText et_fee_name = (EditText) dialog.findViewById(R.id.et_fee_name);
        final EditText et_fee_new = (EditText) dialog.findViewById(R.id.et_fee_new);
        Spinner spinner_fee_status = (Spinner) dialog.findViewById(R.id.spinner_fee_status);
        ImageButton btn_closeDialog = (ImageButton) dialog.findViewById(R.id.btn_closeDialog);


        et_fee_name.setText(name);

        Button btn_fee_managed = (Button) dialog.findViewById(R.id.btn_fee_managed);
        btn_fee_managed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //data update here then close dialog

                if (!et_fee_new.getText().toString().isEmpty() && !new_status.isEmpty()) // agr new fee ammont empty na ho to Update kare
                {
/*
                    DatabaseReference ref = Objects.getFirebaseInstance().getReference("StudentDetails")
                            .child(student_id);
*/

                    DatabaseReference ref = Objects.getFirebaseInstance().getReference().child("StudentDetails").child(student_id);
                    Map<String, Object> updates = new HashMap<String, Object>();

                    updates.put("fee_status", new_status);
                    updates.put("ammount", et_fee_new.getText().toString());
//etc

                    ref.updateChildren(updates);


                    Toast.makeText(context, "Fee Details Updated", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }


            }
        });


        ArrayAdapter<String> arrayAdapter;
        final ArrayList<String> status_Array = new ArrayList<>();
        status_Array.add("Paid");
        status_Array.add("Un-Paid");
        arrayAdapter = new ArrayAdapter<String>(
                context,
                android.R.layout.simple_list_item_1,
                status_Array
        );
        spinner_fee_status.setAdapter(arrayAdapter);
        spinner_fee_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new_status = status_Array.get(position);
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