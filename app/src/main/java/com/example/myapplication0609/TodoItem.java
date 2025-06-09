package com.example.myapplication0609;

public class TodoItem {
    private int id;
    private String task;
    private boolean isCompleted;

    public TodoItem() {
    }

    public TodoItem(int id, String task, boolean isCompleted) {
        this.id = id;
        this.task = task;
        this.isCompleted = isCompleted;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}
