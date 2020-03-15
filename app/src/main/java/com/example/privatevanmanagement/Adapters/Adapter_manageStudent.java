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
import com.example.privatevanmanagement.models.StudentDetail_Model;
import com.example.privatevanmanagement.utils.Objects;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter_manageStudent extends RecyclerView.Adapter<Adapter_manageStudent.manageStudent_viewHolder> {


    ArrayList<StudentDetail_Model> manageStudent_List;
    Context context;
    Button btn_block_unBLock;
    Button btn_edit;
    String what = null;


    public Adapter_manageStudent(List<StudentDetail_Model> manageStudent_List, Context context) {
        this.manageStudent_List = (ArrayList<StudentDetail_Model>) manageStudent_List;
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
        holder.userName.setText(manageStudent_List.get(position).getStudent_name());
        holder.btn_block_unBLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgBlockuser(manageStudent_List.get(position).getStudent_name(), manageStudent_List.get(position).getStatus(), manageStudent_List.get(position).getStudent_id());
            }
        });
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("student_id", manageStudent_List.get(position).getStudent_id());
                bundle.putString("student_name", manageStudent_List.get(position).getStudent_name());
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

    public void msgBlockuser(String student_name, final String student_status, final String student_id) {


        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView text = (TextView) dialog.findViewById(R.id.successTextView);
        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        Button dialogcancel = (Button) dialog.findViewById(R.id.cancelButton);

        text.setText("Are you Sure you want to change status of" + student_name + " ?");

        final DatabaseReference ref = Objects.getFirebaseInstance().getReference().child("StudentDetails").child(student_id);
        final Map<String, Object> updates = new HashMap<String, Object>();

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                //block selected usere here
                if (student_status.equalsIgnoreCase("UnBlocked")) {
                    what = "Blocked";
                } else if (student_status.equalsIgnoreCase("Blocked")) {
                    what = "UnBlocked";
                }
                updates.put("status", what);
                ref.updateChildren(updates);
                Toast.makeText(context, "User " + what + " Successfully", Toast.LENGTH_SHORT).show();
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