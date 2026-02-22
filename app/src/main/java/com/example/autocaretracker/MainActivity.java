package com.example.autocaretracker;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.autocaretracker.adapter.MaintenanceAdapter;
import com.example.autocaretracker.data.AppDatabase;
import com.example.autocaretracker.data.TaskDao;
import com.example.autocaretracker.model.MaintenanceTask;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerTasks;
    private MaintenanceAdapter adapter;
    private final List<MaintenanceTask> tasks = new ArrayList<>();
    private FloatingActionButton fabAdd;

    private AppDatabase db;
    private TaskDao taskDao;

    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();
    private ActivityResultLauncher<Intent> addTaskLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerTasks = findViewById(R.id.recyclerTasks);
        fabAdd = findViewById(R.id.fabAdd);

        // Room
        db = AppDatabase.getInstance(this);
        taskDao = db.taskDao();

        // RecyclerView
        adapter = new MaintenanceAdapter(tasks);
        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerTasks.setAdapter(adapter);

        // ✅ Observe Room data (auto-refresh list)
        taskDao.getAllTasksLive().observe(this, dbTasks -> {
            tasks.clear();
            if (dbTasks != null) tasks.addAll(dbTasks);
            adapter.notifyDataSetChanged();
        });

        // Result launcher for AddEditTaskActivity
        addTaskLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();

                        String taskName = data.getStringExtra("taskName");
                        String vehicleType = data.getStringExtra("vehicleType");
                        String serviceDate = data.getStringExtra("serviceDate");
                        int mileage = data.getIntExtra("mileage", 0);
                        String notes = data.getStringExtra("notes");

                        MaintenanceTask newTask =
                                new MaintenanceTask(taskName, vehicleType, serviceDate, mileage, notes);

                        // ✅ Insert off the main thread
                        dbExecutor.execute(() -> taskDao.insert(newTask));
                        // No manual refresh needed; LiveData observer updates UI automatically.
                    }
                }
        );

        // FAB opens Add/Edit screen and expects a result
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            addTaskLauncher.launch(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbExecutor.shutdown();
    }
}