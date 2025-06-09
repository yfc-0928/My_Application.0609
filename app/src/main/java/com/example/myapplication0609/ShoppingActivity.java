package com.example.myapplication0609;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.util.List;

public class ShoppingActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ShoppingAdapter adapter;
    private List<ShoppingItem> shoppingItems;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);

        databaseHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerViewShopping);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        shoppingItems = databaseHelper.getAllShoppingItems();

        adapter = new ShoppingAdapter(shoppingItems, this, databaseHelper);
        recyclerView.setAdapter(adapter);

        Button buttonAddItem = findViewById(R.id.buttonAddItem);
        buttonAddItem.setOnClickListener(v -> {
            ShoppingItem newItem = new ShoppingItem(0, "", 1);
            long id = databaseHelper.addShoppingItem(newItem);
            if (id != -1) {
                newItem.setId((int) id);
                adapter.addItem(newItem);
                recyclerView.smoothScrollToPosition(shoppingItems.size() - 1);
            } else {
                Toast.makeText(this, "添加项目失败", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        super.onDestroy();
    }
}