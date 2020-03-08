package com.example.privatevanmanagement.Adapters;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.text.method.ScrollingMovementMethod;
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
import com.example.privatevanmanagement.activities.BaseActivity;
import com.example.privatevanmanagement.activities.NavDrawer;
import com.example.privatevanmanagement.models.ManageFee_Model;

import java.util.ArrayList;

public class Adapter_manageStudent extends RecyclerView.Adapter<Adapter_manageStudent.manageStudent_viewHolder> {


    ArrayList<ManageFee_Model> manageStudent_List;
    Context context;
    Button btn_block_unBLock;
    Button btn_edit;


    public Adapter_manageStudent(ArrayList<ManageFee_Model> manageStudent_List, Context context) {
        this.manageStudent_List = manageStudent_List;
        this.context = context;
    }

    @NonNull
    @Override
    public manageStudent_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_manage_student, parent, false);
        manageStudent_viewHolder viewHolder = new manageStudent_viewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull manageStudent_viewHolder holder, final int position) {
        holder.userName.setText(manageStudent_List.get(position).getName());
        holder.btn_block_unBLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgBlockuser(manageStudent_List.get(position).getName());
            }
        });
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("student_id", manageStudent_List.get(position).getId());
                bundle.putString("student_name", manageStudent_List.get(position).getName());
                NavDrawer activity=(NavDrawer) context;
                activity.replaceFragment(new AddStudent(), bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return manageStudent_List.size();
    }

    public static class manageStudent_viewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        Button btn_block_unBLock;
        Button btn_edit;

        public manageStudent_viewHolder(@NonNull View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.userName);
            btn_block_unBLock = (Button) itemView.findViewById(R.id.btn_block_unBLock);
            btn_edit = (Button) itemView.findViewById(R.id.btn_edit);
        }
    }

    public void msgBlockuser(String msg) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView text = (TextView) dialog.findViewById(R.id.successTextView);
        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        Button dialogcancel = (Button) dialog.findViewById(R.id.cancelButton);

        text.setText("Are you Sre you want to Block " + msg + " ?");

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                //block selected usere here
                if (btn_block_unBLock.getText().equals("Block")) {
                    Toast.makeText(context, "User Blocked Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "User Un-Blocked Successfully", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        dialogcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }
}