package com.example.myapplication0609;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PhotoAlbumActivity extends AppCompatActivity implements PhotoAdapter.OnPhotoLongClickListener {

    private RecyclerView recyclerView;
    private PhotoAdapter adapter;
    private List<String> photoPaths = new ArrayList<>();
    private TextView textPhotoCount;
    private ActivityResultLauncher<Intent> galleryLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_album);

        textPhotoCount = findViewById(R.id.textPhotoCount);
        updatePhotoCount();

        recyclerView = findViewById(R.id.recyclerViewPhotos);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new PhotoAdapter(photoPaths, this, this);
        recyclerView.setAdapter(adapter);

        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri selectedImage = result.getData().getData();
                        addPhotoToAlbum(selectedImage.toString());
                    }
                });

        Button buttonAddPhoto = findViewById(R.id.buttonAddPhoto);
        buttonAddPhoto.setOnClickListener(v -> showAddPhotoDialog());
    }

    private void showAddPhotoDialog() {
        if (photoPaths.size() >= 100) {
            Toast.makeText(this, "Maximum 100 photos reached", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Add Photo")
                .setMessage("Choose photo from gallery")
                .setPositiveButton("Choose", (dialog, which) -> {
                    choosePhotoFromGallery();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void choosePhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(intent);
    }

    private void addPhotoToAlbum(String path) {
        adapter.addPhoto(path);
        updatePhotoCount();
        Toast.makeText(this, "Photo added", Toast.LENGTH_SHORT).show();
    }

    private void updatePhotoCount() {
        textPhotoCount.setText(photoPaths.size() + "/100 photos");
    }

    @Override
    public void onPhotoLongClick(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Photo")
                .setMessage("Are you sure you want to delete this photo?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    photoPaths.remove(position);
                    adapter.removePhoto(position);
                    updatePhotoCount();
                    Toast.makeText(this, "Photo removed", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}