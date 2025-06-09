package com.example.myapplication0609;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class ExerciseHistoryActivity extends AppCompatActivity {

    private TextView tvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_history);

        tvHistory = findViewById(R.id.tvHistory);

        SharedPreferences sharedPreferences = getSharedPreferences("ExerciseRecords", MODE_PRIVATE);

        Map<String, ?> allEntries = sharedPreferences.getAll();

        StringBuilder historyBuilder = new StringBuilder();

        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().toString();

            String[] parts = key.split("_");
            if (parts.length == 2) {
                String date = parts[0];
                String type = parts[1];

                historyBuilder.append(date).append(" - ");

                switch (type) {
                    case "plan":
                        historyBuilder.append("训练计划:\n");
                        break;
                    case "height":
                        historyBuilder.append("身高: ").append(value).append(" cm\n");
                        continue;
                    case "weight":
                        historyBuilder.append("体重: ").append(value).append(" kg\n");
                        continue;
                    case "completed":
                        historyBuilder.append("已完成训练:\n");
                        break;
                    default:
                        continue;
                }

                historyBuilder.append(value).append("\n\n");
            }
        }

        if (historyBuilder.length() == 0) {
            tvHistory.setText("暂无历史记录");
        } else {
            tvHistory.setText(historyBuilder.toString());
        }
    }
}