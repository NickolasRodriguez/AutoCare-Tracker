package com.example.autocaretracker.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tasks")
public class MaintenanceTask {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String taskName;
    public String vehicleType;
    public String serviceDate;
    public int mileage;
    public String notes;
    public boolean completed;

    public MaintenanceTask(String taskName, String vehicleType,
                           String serviceDate, int mileage, String notes) {
        this.taskName = taskName;
        this.vehicleType = vehicleType;
        this.serviceDate = serviceDate;
        this.mileage = mileage;
        this.notes = notes;
        this.completed = false;
    }
}