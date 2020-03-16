package com.example.privatevanmanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.privatevanmanagement.R;
import com.example.privatevanmanagement.models.Schedule_Student_Model;

import java.util.ArrayList;

public class Adapter_scheduleVan extends RecyclerView.Adapter<Adapter_scheduleVan.Adapter_scheduleVan_viewHolder> {


    ArrayList<Schedule_Student_Model> schedule_student_modelArrayList;
    Context context;

    public Adapter_scheduleVan(ArrayList<Schedule_Student_Model> schedule_student_modelArrayList, Context context) {
        this.schedule_student_modelArrayList = schedule_student_modelArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public Adapter_scheduleVan.Adapter_scheduleVan_viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_schedulevan_view, parent, false);
        Adapter_scheduleVan.Adapter_scheduleVan_viewHolder viewHolder = new Adapter_scheduleVan.Adapter_scheduleVan_viewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_scheduleVan.Adapter_scheduleVan_viewHolder holder, int position) {
        holder.userName.setText(schedule_student_modelArrayList.get(position).getStudend_name());
        holder.userShift.setText(schedule_student_modelArrayList.get(position).getShift_time());
    }

    @Override
    public int getItemCount() {
        return schedule_student_modelArrayList.size();
    }

    public static class Adapter_scheduleVan_viewHolder extends RecyclerView.ViewHolder {

        TextView userName;
        TextView userShift;

        public Adapter_scheduleVan_viewHolder(@NonNull View itemView) {
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.name);
            userShift = (TextView) itemView.findViewById(R.id.shiftTime);
        }
    }

}