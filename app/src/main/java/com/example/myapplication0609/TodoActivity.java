package com.example.myapplication0609;

import androidx.appcompat.app.AppCompatActivity;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication0609.R;
import com.example.myapplication0609.TodoAdapter;
import com.example.myapplication0609.DatabaseHelper;
import com.example.myapplication0609.TodoItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TodoActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TodoAdapter adapter;
    private List<TodoItem> todoItems;
    private DatabaseHelper databaseHelper;
    private TextView textDate;
    private Button buttonAddTask;
    private Button buttonShowCompleted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_todo);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = new DatabaseHelper(this);

        initViews();

        setCurrentDate();

        loadTodoItems(false);

        setupButtonListeners();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recyclerViewTodo);
        textDate = findViewById(R.id.textDate);
        buttonAddTask = findViewById(R.id.buttonAddTask);
        buttonShowCompleted = findViewById(R.id.buttonShowCompleted);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setCurrentDate() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd EEEE", Locale.getDefault()).format(new Date());
        textDate.setText("Today: " + currentDate);
    }

    private void loadTodoItems(boolean showCompleted) {
        if (showCompleted) {
            todoItems = databaseHelper.getCompletedTodoItems();
            buttonShowCompleted.setText("Show All Tasks");
        } else {
            List<TodoItem> allItems = databaseHelper.getAllTodoItems();
            todoItems = new ArrayList<>();
            for (TodoItem item : allItems) {
                if (!item.getTask().isEmpty() || item.isCompleted()) {
                    todoItems.add(item);
                }
            }
            buttonShowCompleted.setText("Show Completed");
        }

        adapter = new TodoAdapter(todoItems, this);
        recyclerView.setAdapter(adapter);
    }

    private void setupButtonListeners() {
        buttonAddTask.setOnClickListener(v -> {
            if (!todoItems.isEmpty()) {
                TodoItem lastItem = todoItems.get(todoItems.size() - 1);
                if (lastItem.getTask().isEmpty() && !lastItem.isCompleted()) {
                    Toast.makeText(this, "请先填写当前空任务", Toast.LENGTH_SHORT).show();
                    recyclerView.smoothScrollToPosition(todoItems.size() - 1);
                    return;
                }
            }

            TodoItem newItem = new TodoItem(0, "", false);
            long id = databaseHelper.addTodoItem(newItem);
            newItem.setId((int) id);

            adapter.addItem(newItem);
            recyclerView.smoothScrollToPosition(todoItems.size() - 1);
        });

        buttonShowCompleted.setOnClickListener(v -> {
            boolean currentlyShowingCompleted = buttonShowCompleted.getText().toString().equals("Show All Tasks");
            loadTodoItems(!currentlyShowingCompleted);
        });
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}