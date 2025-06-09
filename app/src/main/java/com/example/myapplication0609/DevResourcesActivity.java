package com.example.myapplication0609;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DevResourcesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DevResourceAdapter adapter;
    private List<DevResource> devResources = new ArrayList<>();
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dev_resources);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        databaseHelper = new DatabaseHelper(this);

        devResources = databaseHelper.getAllDevResources();

        recyclerView = findViewById(R.id.recyclerViewDevResources);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new DevResourceAdapter(devResources, this);
        recyclerView.setAdapter(adapter);

        Button buttonAddResource = findViewById(R.id.buttonAddResource);
        buttonAddResource.setOnClickListener(v -> {
            DevResource newResource = new DevResource(0, "", false);
            adapter.addItem(newResource);
            recyclerView.smoothScrollToPosition(devResources.size() - 1);
        });
    }

    @Override
    protected void onDestroy() {
        databaseHelper.close();
        super.onDestroy();
    }
}