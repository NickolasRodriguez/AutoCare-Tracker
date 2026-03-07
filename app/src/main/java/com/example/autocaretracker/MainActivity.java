package com.example.autocaretracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
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
    private TextView textEmptyState;
    private MaintenanceAdapter adapter;
    private final List<MaintenanceTask> tasks = new ArrayList<>();
    private FloatingActionButton fabAdd;

    private TaskDao taskDao;
    private final ExecutorService dbExecutor = Executors.newSingleThreadExecutor();

    private ActivityResultLauncher<Intent> addEditLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textEmptyState = findViewById(R.id.textEmptyState);

        recyclerTasks = findViewById(R.id.recyclerTasks);
        fabAdd = findViewById(R.id.fabAdd);

        // Room
        AppDatabase db = AppDatabase.getInstance(this);
        taskDao = db.taskDao();

        //  Register launcher FIRST
        addEditLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();

                        int id = data.getIntExtra("id", -1);
                        String taskName = data.getStringExtra("taskName");
                        String vehicleType = data.getStringExtra("vehicleType");
                        String serviceDate = data.getStringExtra("serviceDate");
                        int mileage = data.getIntExtra("mileage", 0);
                        String notes = data.getStringExtra("notes");

                        MaintenanceTask task = new MaintenanceTask(taskName, vehicleType, serviceDate, mileage, notes);

                        if (id == -1) {
                            dbExecutor.execute(() -> taskDao.insert(task));
                        } else {
                            task.id = id;
                            dbExecutor.execute(() -> taskDao.update(task));
                        }
                    }
                }
        );

        // RecyclerView + click to edit (uses the launcher that now exists)
        adapter = new MaintenanceAdapter(tasks, new MaintenanceAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MaintenanceTask task) {
                Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);

                intent.putExtra("id", task.id);
                intent.putExtra("taskName", task.taskName);
                intent.putExtra("vehicleType", task.vehicleType);
                intent.putExtra("serviceDate", task.serviceDate);
                intent.putExtra("mileage", task.mileage);
                intent.putExtra("notes", task.notes);

                addEditLauncher.launch(intent);
            }

            @Override
            public void onDeleteClick(MaintenanceTask task) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Task")
                        .setMessage("Are you sure you want to delete this maintenance task?")
                        .setPositiveButton("Delete", (dialog, which) ->
                                dbExecutor.execute(() -> taskDao.delete(task)))
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });

        recyclerTasks.setLayoutManager(new LinearLayoutManager(this));
        recyclerTasks.setAdapter(adapter);

        // Observe Room database changes
        taskDao.getAllTasksLive().observe(this, dbTasks -> {

            tasks.clear();

            if (dbTasks != null) {
                tasks.addAll(dbTasks);
            }

            adapter.notifyDataSetChanged();

            // Show or hide empty message
            if (tasks.isEmpty()) {
                textEmptyState.setVisibility(TextView.VISIBLE);
                recyclerTasks.setVisibility(RecyclerView.GONE);
            } else {
                textEmptyState.setVisibility(TextView.GONE);
                recyclerTasks.setVisibility(RecyclerView.VISIBLE);
            }
        });

        // FAB = Add new task
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEditTaskActivity.class);
            addEditLauncher.launch(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbExecutor.shutdown();
    }
}