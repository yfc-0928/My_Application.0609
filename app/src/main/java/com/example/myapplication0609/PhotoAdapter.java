package com.example.myapplication0609;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private List<String> photoPaths;
    private Context context;
    private OnPhotoLongClickListener longClickListener;

    public interface OnPhotoLongClickListener {
        void onPhotoLongClick(int position);
    }

    public PhotoAdapter(List<String> photoPaths, Context context, OnPhotoLongClickListener listener) {
        this.photoPaths = photoPaths;
        this.context = context;
        this.longClickListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String photoPath = photoPaths.get(position);

        if (photoPath.startsWith("content://")) {
            Glide.with(context)
                    .load(Uri.parse(photoPath))
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.imageViewPhoto);
        } else {
            Glide.with(context)
                    .load(new File(photoPath))
                    .placeholder(R.drawable.ic_placeholder)
                    .into(holder.imageViewPhoto);
        }

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        holder.textPhotoDate.setText(currentDate);

        holder.itemView.setOnLongClickListener(v -> {
            longClickListener.onPhotoLongClick(position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return photoPaths.size();
    }

    public void addPhoto(String path) {
        photoPaths.add(path);
        notifyItemInserted(photoPaths.size() - 1);
    }

    public void removePhoto(int position) {
        photoPaths.remove(position);
        notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewPhoto;
        TextView textPhotoDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPhoto = itemView.findViewById(R.id.imageViewPhoto);
            textPhotoDate = itemView.findViewById(R.id.textPhotoDate);
        }
    }
}