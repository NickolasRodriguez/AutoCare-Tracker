package com.example.autocaretracker.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.autocaretracker.model.MaintenanceTask;

import java.util.List;

@Dao
public interface TaskDao {

    @Insert
    void insert(MaintenanceTask task);

    @Update
    void update(MaintenanceTask task);

    @Delete
    void delete(MaintenanceTask task);

    @Query("SELECT * FROM tasks ORDER BY id DESC")
    LiveData<List<MaintenanceTask>> getAllTasksLive();
}