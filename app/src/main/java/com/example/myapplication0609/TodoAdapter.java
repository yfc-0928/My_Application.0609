package com.example.myapplication0609;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {
    private List<TodoItem> todoItems;
    private Context context;
    private DatabaseHelper databaseHelper;

    public TodoAdapter(List<TodoItem> todoItems, Context context) {
        this.todoItems = todoItems;
        this.context = context;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_todo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TodoItem item = todoItems.get(position);

        holder.editTask.setText(item.getTask());

        updateItemUI(holder, item.isCompleted());

        holder.editTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String newTask = s.toString().trim();
                if (!newTask.isEmpty() && !newTask.equals(item.getTask())) {
                    item.setTask(newTask);
                    databaseHelper.updateTodoItem(item);
                }
            }
        });

        holder.buttonComplete.setOnClickListener(v -> {
            if (!holder.editTask.getText().toString().trim().isEmpty()) {
                item.setCompleted(true);
                databaseHelper.updateTodoItem(item);
                updateItemUI(holder, true);
                Toast.makeText(context, "任务标记为已完成", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "请输入任务内容", Toast.LENGTH_SHORT).show();
            }
        });

        holder.buttonCancel.setOnClickListener(v -> {
            item.setCompleted(false);
            databaseHelper.updateTodoItem(item);
            updateItemUI(holder, false);
            Toast.makeText(context, "任务标记为未完成", Toast.LENGTH_SHORT).show();
        });

        holder.itemView.setOnLongClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                databaseHelper.deleteTodoItem(item.getId());
                todoItems.remove(adapterPosition);
                notifyItemRemoved(adapterPosition);
                Toast.makeText(context, "任务已删除", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    public void addItem(TodoItem item) {
        if (!todoItems.isEmpty() && todoItems.get(todoItems.size() - 1).getTask().isEmpty()
                && !todoItems.get(todoItems.size() - 1).isCompleted()) {
            return;
        }

        long id = databaseHelper.addTodoItem(item);
        item.setId((int) id);

        todoItems.add(item);
        notifyItemInserted(todoItems.size() - 1);
    }

    public void updateItems(List<TodoItem> newItems) {
        todoItems.clear();
        todoItems.addAll(newItems);
        notifyDataSetChanged();
    }

    private void updateItemUI(ViewHolder holder, boolean isCompleted) {
        if (isCompleted) {
            holder.buttonComplete.setVisibility(View.GONE);
            holder.buttonCancel.setVisibility(View.VISIBLE);
            holder.editTask.setEnabled(false);
            holder.editTask.setAlpha(0.5f);
        } else {
            holder.buttonComplete.setVisibility(View.VISIBLE);
            holder.buttonCancel.setVisibility(View.GONE);
            holder.editTask.setEnabled(true);
            holder.editTask.setAlpha(1.0f);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        EditText editTask;
        Button buttonComplete;
        Button buttonCancel;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            editTask = itemView.findViewById(R.id.editTask);
            buttonComplete = itemView.findViewById(R.id.buttonComplete);
            buttonCancel = itemView.findViewById(R.id.buttonCancel);
        }
    }
}