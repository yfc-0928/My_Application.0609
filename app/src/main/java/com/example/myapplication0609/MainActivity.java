package com.example.myapplication0609;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CardView cardShopping = findViewById(R.id.card_shopping);
        CardView cardTodo = findViewById(R.id.card_todo);
        CardView cardDevResources = findViewById(R.id.card_dev_resources);
        CardView cardPhoto = findViewById(R.id.card_photo);
        CardView cardExercise = findViewById(R.id.card_exercise);

        cardShopping.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ShoppingActivity.class);
            startActivity(intent);
        });

        cardTodo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, TodoActivity.class);
            startActivity(intent);
        });

        cardDevResources.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DevResourcesActivity.class);
            startActivity(intent);
        });

        cardPhoto.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PhotoAlbumActivity.class);
            startActivity(intent);
        });

        cardExercise.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ExerciseRecordActivity.class);
            startActivity(intent);
        });
    }
}