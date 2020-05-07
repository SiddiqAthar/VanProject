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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.privatevanmanagement.Fragments.admin.AdminAddVan;
import com.example.privatevanmanagement.R;
import com.example.privatevanmanagement.activities.AdminNav_Activity;
import com.example.privatevanmanagement.models.VanDetail_Model;
import com.example.privatevanmanagement.utils.Objects;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class Adapter_manageGroups extends RecyclerView.Adapter<Adapter_manageGroups.manageVan_viewHolder> {

    ArrayList<String> manageGroup_List;
    Context context;
    ProgressDialog pd = null;

    public Adapter_manageGroups(ArrayList<String> manageGroup_List, Context context) {
        this.manageGroup_List = manageGroup_List;
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
        holder.userName.setText(manageGroup_List.get(position));
        holder.btn_block_unBLock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteGroup(manageGroup_List.get(position), holder.getAdapterPosition());

            }
        });
        holder.btn_edit.setVisibility(View.GONE);
/*
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogAddGroup(manageGroup_List.get(position), holder.getAdapterPosition());
            }
        });
*/
    }

    @Override
    public int getItemCount() {
        return manageGroup_List.size();
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

    public void DeleteGroup(String groupName, final int positon) {
        final DatabaseReference ref = Objects.getFirebaseInstance().getReference().child("Groups").child(groupName);

        ref.removeValue();
        Toast.makeText(context, "Group Removed Successfully", Toast.LENGTH_SHORT).show();

        manageGroup_List.remove(positon);
        notifyItemRemoved(positon);
    }

/*
    public void showDialogAddGroup(final String bundle, final int positon) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        dialogBuilder.setCancelable(false);
        View dialogView = inflater.inflate(R.layout.dialog_add_group, null);
        dialogBuilder.setView(dialogView);


        final AlertDialog alertDialog = dialogBuilder.create();
        final EditText et_add_Group = dialogView.findViewById(R.id.et_add_Group);
        Button btn_addGroup = dialogView.findViewById(R.id.btn_addGroup);
        ImageButton btn_closeDialog = dialogView.findViewById(R.id.btn_closeDialog);

        et_add_Group.setText(bundle);

        btn_addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!et_add_Group.getText().equals("")) {
                    DatabaseReference newPost =
                            Objects.getFirebaseInstance().getReference().child("Groups").child(bundle);
                    newPost.removeValue();
                    manageGroup_List.remove(positon);
//                    notifyItemRemoved(positon);


                    DatabaseReference newPost2 =
                            Objects.getFirebaseInstance().getReference().child("Groups");
                    newPost2.child(et_add_Group.getText().toString()).setValue(et_add_Group.getText().toString());
                    manageGroup_List.add(et_add_Group.getText().toString());
                    notifyItemChanged(positon);

                    alertDialog.dismiss();
                } else {
                    Toast.makeText(context, "Group field is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btn_closeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        if (alertDialog.isShowing()) {
            alertDialog.dismiss();
        } else {
            alertDialog.show();
            alertDialog.getWindow().setLayout(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
        }
    }
*/

}