package com.example.privatevanmanagement.adapters;


import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
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

import com.example.privatevanmanagement.Fragments.admin.AddDriver;
import com.example.privatevanmanagement.Fragments.admin.AddStudent;
import com.example.privatevanmanagement.R;
import com.example.privatevanmanagement.activities.AdminNav_Activity;
import com.example.privatevanmanagement.models.DriverDetail_Model;
import com.example.privatevanmanagement.models.StudentDetail_Model;
import com.example.privatevanmanagement.utils.Objects;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter_manageDriver extends RecyclerView.Adapter<Adapter_manageDriver.manageDriver_viewHolder> {


    ArrayList<DriverDetail_Model> manageDriver_List;
    Context context;
    ProgressDialog pd = null;
    String what = "";

    public Adapter_manageDriver(List<DriverDetail_Model> manageDriver_List, Context context) {
        this.manageDriver_List = (ArrayList<DriverDetail_Model>) manageDriver_List;
        this.context = context;
    }

    @NonNull
    @Override
    public manageDriver_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_manage_student, parent, false);
        manageDriver_viewHolder viewHolder = new manageDriver_viewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final manageDriver_viewHolder holder, final int position) {
        holder.userName.setText(manageDriver_List.get(position).getDriver_name());
        holder.btn_block_unBLock.setText(manageDriver_List.get(position).getStatus());
        holder.btn_block_unBLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgBlockuser(manageDriver_List.get(position).getDriver_name(), manageDriver_List.get(position).getStatus(),
                        manageDriver_List.get(position).getDriver_id(),holder.getAdapterPosition());
            }
        });
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                bundle.putString("driver_id", manageDriver_List.get(position).getDriver_id());
                bundle.putString("driver_name", manageDriver_List.get(position).getDriver_name());
                bundle.putString("driver_cnic", manageDriver_List.get(position).getDriver_cnic());
                bundle.putString("driver_contact", manageDriver_List.get(position).getDriver_contact());
                bundle.putString("driver_address", manageDriver_List.get(position).getDriver_address());

                AdminNav_Activity activity = (AdminNav_Activity) context;
                activity.replaceFragment(new AddDriver(), bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return manageDriver_List.size();
    }

    public static class manageDriver_viewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        Button btn_block_unBLock;
        Button btn_edit;

        public manageDriver_viewHolder(@NonNull View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.userName);
            btn_block_unBLock = (Button) itemView.findViewById(R.id.btn_block_unBLock);
            btn_edit = (Button) itemView.findViewById(R.id.btn_edit);
        }
    }

    public void msgBlockuser(String driver_name, final String driver_status, final String driver_id, final int position) {

        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView text = (TextView) dialog.findViewById(R.id.successTextView);
        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        Button dialogcancel = (Button) dialog.findViewById(R.id.cancelButton);

        text.setText("Are you Sure you want to change status of " + driver_name + " ?");

        final DatabaseReference ref = Objects.getFirebaseInstance().getReference().child("DriverDetails").child(driver_id);
        final Map<String, Object> updates = new HashMap<String, Object>();

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                //block selected usere here
                if (driver_status.equalsIgnoreCase("UnBlocked")) {
                    what = "Blocked";
                } else if (driver_status.equalsIgnoreCase("Blocked")) {
                    what = "UnBlocked";
                }
                updates.put("status", what);
                ref.updateChildren(updates);
                manageDriver_List.get(position).setStatus(what);
                Toast.makeText(context, "User " + what + " Successfully", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                notifyItemChanged(position);
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