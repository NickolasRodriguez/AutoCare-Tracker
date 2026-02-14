package com.example.autocaretracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autocaretracker.R;
import com.example.autocaretracker.model.MaintenanceTask;

import java.util.List;

public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceAdapter.TaskViewHolder> {

    private List<MaintenanceTask> tasks;

    public MaintenanceAdapter(List<MaintenanceTask> tasks) {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        MaintenanceTask task = tasks.get(position);

        holder.taskName.setText(task.taskName);
        holder.vehicle.setText(task.vehicleType);
        holder.dateMileage.setText(task.serviceDate + " - " + task.mileage + " miles");
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView taskName, vehicle, dateMileage;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);

            taskName = itemView.findViewById(R.id.textTaskName);
            vehicle = itemView.findViewById(R.id.textVehicle);
            dateMileage = itemView.findViewById(R.id.textDateMileage);
        }
    }
}