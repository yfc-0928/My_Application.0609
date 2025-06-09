package com.example.myapplication0609;

import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ViewHolder> {
    private List<ShoppingItem> shoppingItems;
    private Context context;
    private DatabaseHelper databaseHelper;

    public ShoppingAdapter(List<ShoppingItem> shoppingItems, Context context, DatabaseHelper dbHelper) {
        this.shoppingItems = shoppingItems;
        this.context = context;
        this.databaseHelper = dbHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shopping, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShoppingItem item = shoppingItems.get(position);

        holder.editItemName.setText(item.getName());
        holder.textQuantity.setText(String.valueOf(item.getQuantity()));

        holder.editItemName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                item.setName(s.toString());
                if (databaseHelper.updateShoppingItem(item) <= 0) {
                    Toast.makeText(context, "更新失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.buttonIncrease.setOnClickListener(v -> {
            int quantity = item.getQuantity();
            quantity++;
            item.setQuantity(quantity);
            holder.textQuantity.setText(String.valueOf(quantity));
            if (databaseHelper.updateShoppingItem(item) <= 0) {
                Toast.makeText(context, "更新失败", Toast.LENGTH_SHORT).show();
            }
        });

        holder.buttonDecrease.setOnClickListener(v -> {
            int quantity = item.getQuantity();
            if (quantity > 1) {
                quantity--;
                item.setQuantity(quantity);
                holder.textQuantity.setText(String.valueOf(quantity));
                if (databaseHelper.updateShoppingItem(item) <= 0) {
                    Toast.makeText(context, "更新失败", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.itemView.setOnLongClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("删除项目")
                    .setMessage("确定要删除这个项目吗?")
                    .setPositiveButton("确定", (dialog, which) -> {
                        if (databaseHelper.deleteShoppingItem(item.getId()) > 0) {
                            shoppingItems.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, shoppingItems.size());
                            Toast.makeText(context, "已删除", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "删除失败", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return shoppingItems.size();
    }

    public void addItem(ShoppingItem item) {
        shoppingItems.add(item);
        notifyItemInserted(shoppingItems.size() - 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText editItemName;
        TextView textQuantity;
        Button buttonIncrease;
        Button buttonDecrease;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            editItemName = itemView.findViewById(R.id.editItemName);
            textQuantity = itemView.findViewById(R.id.textQuantity);
            buttonIncrease = itemView.findViewById(R.id.buttonIncrease);
            buttonDecrease = itemView.findViewById(R.id.buttonDecrease);
        }
    }
}