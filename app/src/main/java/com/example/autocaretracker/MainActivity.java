package com.example.autocaretracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autocaretracker.adapter.MaintenanceAdapter;
import com.example.autocaretracker.model.MaintenanceTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerTasks;
    private FloatingActionButton fabAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerTasks = findViewById(R.id.recyclerTasks);
        fabAdd = findViewById(R.id.fabAdd);

        List<MaintenanceTask> tasks = new ArrayList<>();
        tasks.add(new MaintenanceTask("Oil Change", "Car", "02/14/2026", 50500, "Synthetic oil"));
        tasks.add(new MaintenanceTask("Brake Inspection", "Motorcycle", "02/10/2026", 12500, "Front pads 60%"));

        MaintenanceAdapter adapter = new MaintenanceAdapter(tasks);
        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerTasks.setAdapter(adapter);

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            startActivity(intent);
        });
    }
}