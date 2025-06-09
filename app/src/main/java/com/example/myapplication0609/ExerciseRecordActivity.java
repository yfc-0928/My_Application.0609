package com.example.myapplication0609;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExerciseRecordActivity extends AppCompatActivity {

    private EditText etTrainingPlan, etHeight, etWeight, etCompletedTraining;
    private Button btnSavePlan, btnSaveBodyData, btnSaveCompleted, btnAddContent, btnViewHistory, btnSelectDate;
    private TextView tvSelectedDate;
    private SharedPreferences sharedPreferences;
    private String currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_record);

        initViews();

        sharedPreferences = getSharedPreferences("ExerciseRecords", MODE_PRIVATE);

        setCurrentDate();

        loadSavedData();

        setupListeners();
    }

    private void initViews() {
        etTrainingPlan = findViewById(R.id.etTrainingPlan);
        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etCompletedTraining = findViewById(R.id.etCompletedTraining);
        btnSavePlan = findViewById(R.id.btnSavePlan);
        btnSaveBodyData = findViewById(R.id.btnSaveBodyData);
        btnSaveCompleted = findViewById(R.id.btnSaveCompleted);
        btnAddContent = findViewById(R.id.btnAddContent);
        btnViewHistory = findViewById(R.id.btnViewHistory);
        btnSelectDate = findViewById(R.id.btnSelectDate);
        tvSelectedDate = findViewById(R.id.tvSelectedDate);
    }

    private void setCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        currentDate = sdf.format(new Date());
        tvSelectedDate.setText("今天 (" + currentDate + ")");
    }

    private void loadSavedData() {
        String plan = sharedPreferences.getString(currentDate + "_plan", "");
        etTrainingPlan.setText(plan);

        String height = sharedPreferences.getString(currentDate + "_height", "");
        String weight = sharedPreferences.getString(currentDate + "_weight", "");
        etHeight.setText(height);
        etWeight.setText(weight);

        String completed = sharedPreferences.getString(currentDate + "_completed", "");
        etCompletedTraining.setText(completed);
    }

    private void setupListeners() {
        btnSavePlan.setOnClickListener(v -> {
            String plan = etTrainingPlan.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(currentDate + "_plan", plan);
            editor.apply();
            Toast.makeText(this, "训练计划已保存", Toast.LENGTH_SHORT).show();
        });

        btnSaveBodyData.setOnClickListener(v -> {
            String height = etHeight.getText().toString();
            String weight = etWeight.getText().toString();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(currentDate + "_height", height);
            editor.putString(currentDate + "_weight", weight);
            editor.apply();
            Toast.makeText(this, "身体数据已保存", Toast.LENGTH_SHORT).show();
        });

        btnSaveCompleted.setOnClickListener(v -> {
            String completed = etCompletedTraining.getText().toString();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(currentDate + "_completed", completed);
            editor.apply();
            Toast.makeText(this, "已完成训练内容已保存", Toast.LENGTH_SHORT).show();
        });

        btnAddContent.setOnClickListener(v -> {
            etCompletedTraining.append("\n- ");
            etCompletedTraining.requestFocus();
            etCompletedTraining.setSelection(etCompletedTraining.getText().length());
        });

        btnViewHistory.setOnClickListener(v -> {
            Intent intent = new Intent(ExerciseRecordActivity.this, ExerciseHistoryActivity.class);
            startActivity(intent);
        });

        btnSelectDate.setOnClickListener(v -> showDatePickerDialog());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    currentDate = String.format(Locale.getDefault(), "%04d-%02d-%02d",
                            selectedYear, selectedMonth + 1, selectedDay);

                    Calendar today = Calendar.getInstance();
                    Calendar selectedDate = Calendar.getInstance();
                    selectedDate.set(selectedYear, selectedMonth, selectedDay);

                    if (selectedDate.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                            selectedDate.get(Calendar.MONTH) == today.get(Calendar.MONTH) &&
                            selectedDate.get(Calendar.DAY_OF_MONTH) == today.get(Calendar.DAY_OF_MONTH)) {
                        tvSelectedDate.setText("今天 (" + currentDate + ")");
                    } else {
                        tvSelectedDate.setText(currentDate);
                    }

                    loadSavedData();
                }, year, month, day);

        datePickerDialog.show();
    }
}