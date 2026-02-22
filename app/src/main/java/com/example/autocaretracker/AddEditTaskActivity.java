package com.example.autocaretracker;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditTaskActivity extends AppCompatActivity {

    private EditText editTaskName, editVehicleType, editServiceDate, editMileage, editNotes;
    private Button btnSave, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        // Bind views
        editTaskName = findViewById(R.id.editTaskName);
        editVehicleType = findViewById(R.id.editVehicleType);
        editServiceDate = findViewById(R.id.editServiceDate);
        editMileage = findViewById(R.id.editMileage);
        editNotes = findViewById(R.id.editNotes);

        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);

        // Save button: validate + return data to MainActivity
        btnSave.setOnClickListener(v -> {
            String taskName = editTaskName.getText().toString().trim();
            String vehicleType = editVehicleType.getText().toString().trim();
            String serviceDate = editServiceDate.getText().toString().trim();
            String mileageStr = editMileage.getText().toString().trim();
            String notes = editNotes.getText().toString().trim();

            boolean hasError = false;

            if (taskName.isEmpty()) {
                editTaskName.setError("Required");
                hasError = true;
            }
            if (vehicleType.isEmpty()) {
                editVehicleType.setError("Required");
                hasError = true;
            }
            if (serviceDate.isEmpty()) {
                editServiceDate.setError("Required");
                hasError = true;
            }
            if (mileageStr.isEmpty()) {
                editMileage.setError("Required");
                hasError = true;
            }

            if (hasError) return;

            int mileage;
            try {
                mileage = Integer.parseInt(mileageStr);
            } catch (NumberFormatException e) {
                editMileage.setError("Enter a valid number");
                return;
            }

            Intent resultIntent = new Intent();
            resultIntent.putExtra("taskName", taskName);
            resultIntent.putExtra("vehicleType", vehicleType);
            resultIntent.putExtra("serviceDate", serviceDate);
            resultIntent.putExtra("mileage", mileage);
            resultIntent.putExtra("notes", notes);

            setResult(RESULT_OK, resultIntent);
            finish();
        });

        // Cancel button: just close
        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }
}