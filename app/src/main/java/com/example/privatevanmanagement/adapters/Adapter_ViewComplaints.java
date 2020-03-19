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
import com.example.privatevanmanagement.models.Complaint_Model;
import com.example.privatevanmanagement.models.StudentDetail_Model;
import com.example.privatevanmanagement.utils.Objects;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter_ViewComplaints extends RecyclerView.Adapter<Adapter_ViewComplaints.Complaints_viewHolder> {
    ArrayList<Complaint_Model> complaint_list;
    Context context;
    public Adapter_ViewComplaints(List<Complaint_Model> complaint_list, Context context) {
        this.complaint_list = (ArrayList<Complaint_Model>) complaint_list;
        this.context = context;
    }
    @NonNull
    @Override
    public Complaints_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_complaint_view, parent, false);
        Complaints_viewHolder viewHolder = new Complaints_viewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Complaints_viewHolder holder, final int position) {
        holder.name.setText(complaint_list.get(position).getName());
        holder.complaint.setText(complaint_list.get(position).getComplaint_text());

    }

    @Override
    public int getItemCount() {
        return complaint_list.size();
    }

    public static class  Complaints_viewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView complaint;

        public Complaints_viewHolder(@NonNull View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.complainer_name);
            complaint = (TextView) itemView.findViewById(R.id.complainer_description);
        }
    }


}