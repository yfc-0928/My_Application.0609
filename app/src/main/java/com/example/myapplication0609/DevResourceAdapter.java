package com.example.myapplication0609;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DevResourceAdapter extends RecyclerView.Adapter<DevResourceAdapter.ViewHolder> {

    private List<DevResource> devResources;
    private Context context;
    private DatabaseHelper databaseHelper;

    public DevResourceAdapter(List<DevResource> devResources, Context context) {
        this.devResources = devResources;
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dev_resource, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DevResource resource = devResources.get(position);

        holder.editUrl.setText(resource.getUrl());

        holder.editUrl.setOnClickListener(v -> {
            if (resource.isSaved() && resource.getUrl() != null && !resource.getUrl().isEmpty()) {
                try {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(resource.getUrl()));
                    context.startActivity(browserIntent);
                } catch (Exception e) {
                    Toast.makeText(context, "Invalid URL", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.buttonSave.setOnClickListener(v -> {
            String url = holder.editUrl.getText().toString().trim();
            if (url.isEmpty()) {
                Toast.makeText(context, "URL cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                url = "http://" + url;
            }

            resource.setUrl(url);
            resource.setSaved(true);

            // Update in database
            if (resource.getId() == 0) { // New item
                long id = databaseHelper.addDevResource(resource);
                resource.setId((int) id);
            } else { // Existing item
                databaseHelper.updateDevResource(resource);
            }

            notifyItemChanged(position);
            Toast.makeText(context, "URL saved", Toast.LENGTH_SHORT).show();
        });

        holder.itemView.setOnLongClickListener(v -> {
            if (resource.getId() != 0) {
                databaseHelper.deleteDevResource(resource.getId());
            }
            devResources.remove(position);
            notifyItemRemoved(position);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return devResources.size();
    }

    public void addItem(DevResource item) {
        devResources.add(item);
        notifyItemInserted(devResources.size() - 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText editUrl;
        Button buttonSave;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            editUrl = itemView.findViewById(R.id.editUrl);
            buttonSave = itemView.findViewById(R.id.buttonSave);
        }
    }
}