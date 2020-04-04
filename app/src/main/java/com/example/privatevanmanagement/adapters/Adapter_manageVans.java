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

import com.example.privatevanmanagement.Fragments.admin.AddStudent;
import com.example.privatevanmanagement.Fragments.admin.AdminAddVan;
import com.example.privatevanmanagement.R;
import com.example.privatevanmanagement.activities.AdminNav_Activity;
import com.example.privatevanmanagement.models.StudentDetail_Model;
import com.example.privatevanmanagement.models.VanDetail_Model;
import com.example.privatevanmanagement.utils.Objects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter_manageVans extends RecyclerView.Adapter<Adapter_manageVans.manageVan_viewHolder> {

    ArrayList<VanDetail_Model> manageVan_List;
    Context context;
    ProgressDialog pd = null;

    public Adapter_manageVans(ArrayList<VanDetail_Model> manageVan_List, Context context) {
        this.manageVan_List = manageVan_List;
        this.context = context;
    }

    @NonNull
    @Override
    public manageVan_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_manage_student, parent, false);
        manageVan_viewHolder viewHolder = new manageVan_viewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final manageVan_viewHolder holder, final int position) {
        holder.userName.setText(manageVan_List.get(position).getVanRegisteration());
        holder.btn_block_unBLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgDeleteVan(manageVan_List.get(position).getVanRegisteration(), manageVan_List.get(position).getVanId(), holder.getAdapterPosition());

            }
        });
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                bundle.putString("vanId", manageVan_List.get(position).getVanId());
                bundle.putString("vanRegisteration", manageVan_List.get(position).getVanRegisteration());
                bundle.putString("vanModel", manageVan_List.get(position).getVanModel());
                bundle.putString("vanMake", manageVan_List.get(position).getVanMake());
                bundle.putString("vanColor", manageVan_List.get(position).getVanColor());
                bundle.putString("vanType", manageVan_List.get(position).getVanType());
                bundle.putString("vanCapacity", manageVan_List.get(position).getVanCapacity());

                AdminNav_Activity activity = (AdminNav_Activity) context;
                activity.replaceFragment(new AdminAddVan(), bundle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return manageVan_List.size();
    }

    public static class manageVan_viewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        Button btn_block_unBLock;
        Button btn_edit;

        public manageVan_viewHolder(@NonNull View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.userName);
            btn_block_unBLock = (Button) itemView.findViewById(R.id.btn_block_unBLock);
            btn_block_unBLock.setText("Delete");
            btn_edit = (Button) itemView.findViewById(R.id.btn_edit);
        }
    }

    public void msgDeleteVan(String van_reg, final String van_id, final int positon) {


        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog);

        TextView text = (TextView) dialog.findViewById(R.id.successTextView);
        Button dialogButton = (Button) dialog.findViewById(R.id.okButton);
        Button dialogcancel = (Button) dialog.findViewById(R.id.cancelButton);

        text.setText("Are you Sure you want to delete Van " + van_reg + " ?");

        final DatabaseReference ref = Objects.getFirebaseInstance().getReference().child("AddVan").child(van_id);
        final Map<String, Object> updates = new HashMap<String, Object>();

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(context);
                pd.setMessage("Removing Van Data");
                pd.setCancelable(false);
                pd.show();

                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ref.removeValue();
                        Toast.makeText(context, "Van Removed Successfully", Toast.LENGTH_SHORT).show();
                        manageVan_List.remove(positon);
                        notifyItemRemoved(positon);
                        dialog.dismiss();
                        pd.dismiss();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(context, "Error while Removing Van", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        pd.dismiss();
                    }
                });
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