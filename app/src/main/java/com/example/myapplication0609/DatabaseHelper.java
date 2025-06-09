package com.example.myapplication0609;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.myapplication0609.ShoppingItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DailyRecorder.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_SHOPPING = "shopping";
    private static final String COLUMN_SHOPPING_ID = "id";
    private static final String COLUMN_SHOPPING_NAME = "name";
    private static final String COLUMN_SHOPPING_QUANTITY = "quantity";

    private static final String TABLE_TODO = "todo";
    private static final String COLUMN_TODO_ID = "id";
    private static final String COLUMN_TODO_TASK = "task";
    private static final String COLUMN_TODO_COMPLETED = "completed";

    private static final String TABLE_DEV_RESOURCES = "dev_resources";
    private static final String COLUMN_DEV_RESOURCE_ID = "id";
    private static final String COLUMN_DEV_RESOURCE_URL = "url";
    private static final String COLUMN_DEV_RESOURCE_SAVED = "saved";

    private static final String CREATE_SHOPPING_TABLE = "CREATE TABLE " + TABLE_SHOPPING + "("
            + COLUMN_SHOPPING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_SHOPPING_NAME + " TEXT,"
            + COLUMN_SHOPPING_QUANTITY + " INTEGER)";

    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TABLE_TODO + "("
            + COLUMN_TODO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_TODO_TASK + " TEXT,"
            + COLUMN_TODO_COMPLETED + " INTEGER)";

    private static final String CREATE_DEV_RESOURCES_TABLE = "CREATE TABLE " + TABLE_DEV_RESOURCES + "("
            + COLUMN_DEV_RESOURCE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_DEV_RESOURCE_URL + " TEXT,"
            + COLUMN_DEV_RESOURCE_SAVED + " INTEGER)";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SHOPPING_TABLE);
        db.execSQL(CREATE_TODO_TABLE);
        db.execSQL(CREATE_DEV_RESOURCES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SHOPPING);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DEV_RESOURCES);
        onCreate(db);
    }


    public long addShoppingItem(ShoppingItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SHOPPING_NAME, item.getName());
        values.put(COLUMN_SHOPPING_QUANTITY, item.getQuantity());

        long id = db.insert(TABLE_SHOPPING, null, values);
        db.close();
        return id;
    }

    @SuppressLint("Range")
    public ShoppingItem getShoppingItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SHOPPING,
                new String[]{COLUMN_SHOPPING_ID, COLUMN_SHOPPING_NAME, COLUMN_SHOPPING_QUANTITY},
                COLUMN_SHOPPING_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        ShoppingItem item = null;
        if (cursor != null && cursor.moveToFirst()) {
            item = new ShoppingItem();
            item.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_SHOPPING_ID)));
            item.setName(cursor.getString(cursor.getColumnIndex(COLUMN_SHOPPING_NAME)));
            item.setQuantity(cursor.getInt(cursor.getColumnIndex(COLUMN_SHOPPING_QUANTITY)));
            cursor.close();
        }
        db.close();
        return item;
    }

    @SuppressLint("Range")
    public List<ShoppingItem> getAllShoppingItems() {
        List<ShoppingItem> itemList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SHOPPING + " ORDER BY " + COLUMN_SHOPPING_ID + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ShoppingItem item = new ShoppingItem();
                item.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_SHOPPING_ID)));
                item.setName(cursor.getString(cursor.getColumnIndex(COLUMN_SHOPPING_NAME)));
                item.setQuantity(cursor.getInt(cursor.getColumnIndex(COLUMN_SHOPPING_QUANTITY)));
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemList;
    }

    public int updateShoppingItem(ShoppingItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SHOPPING_NAME, item.getName());
        values.put(COLUMN_SHOPPING_QUANTITY, item.getQuantity());

        int rowsAffected = db.update(TABLE_SHOPPING, values,
                COLUMN_SHOPPING_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
        db.close();
        return rowsAffected;
    }

    public int deleteShoppingItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_SHOPPING,
                COLUMN_SHOPPING_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
        return rowsAffected;
    }


    public void deleteAllShoppingItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SHOPPING, null, null);
        db.close();
    }

    public int getShoppingItemsCount() {
        String countQuery = "SELECT * FROM " + TABLE_SHOPPING;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }


    public long addTodoItem(TodoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO_TASK, item.getTask());
        values.put(COLUMN_TODO_COMPLETED, item.isCompleted() ? 1 : 0);

        long id = db.insert(TABLE_TODO, null, values);
        db.close();
        return id;
    }

    @SuppressLint("Range")
    public TodoItem getTodoItem(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_TODO,
                new String[]{COLUMN_TODO_ID, COLUMN_TODO_TASK, COLUMN_TODO_COMPLETED},
                COLUMN_TODO_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        TodoItem item = null;
        if (cursor != null && cursor.moveToFirst()) {
            item = new TodoItem();
            item.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TODO_ID)));
            item.setTask(cursor.getString(cursor.getColumnIndex(COLUMN_TODO_TASK)));
            item.setCompleted(cursor.getInt(cursor.getColumnIndex(COLUMN_TODO_COMPLETED)) == 1);
            cursor.close();
        }
        db.close();
        return item;
    }

    @SuppressLint("Range")
    public List<TodoItem> getAllTodoItems() {
        List<TodoItem> itemList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TODO + " WHERE " +
                COLUMN_TODO_TASK + " != '' OR " + COLUMN_TODO_COMPLETED + " = 1" +
                " ORDER BY " + COLUMN_TODO_ID + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TodoItem item = new TodoItem();
                item.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TODO_ID)));
                item.setTask(cursor.getString(cursor.getColumnIndex(COLUMN_TODO_TASK)));
                item.setCompleted(cursor.getInt(cursor.getColumnIndex(COLUMN_TODO_COMPLETED)) == 1);
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemList;
    }

    @SuppressLint("Range")
    public List<TodoItem> getCompletedTodoItems() {
        List<TodoItem> itemList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_TODO + " WHERE " + COLUMN_TODO_COMPLETED + " = 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TodoItem item = new TodoItem();
                item.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_TODO_ID)));
                item.setTask(cursor.getString(cursor.getColumnIndex(COLUMN_TODO_TASK)));
                item.setCompleted(true);
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return itemList;
    }

    public int updateTodoItem(TodoItem item) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TODO_TASK, item.getTask());
        values.put(COLUMN_TODO_COMPLETED, item.isCompleted() ? 1 : 0);

        int rowsAffected = db.update(TABLE_TODO, values,
                COLUMN_TODO_ID + " = ?",
                new String[]{String.valueOf(item.getId())});
        db.close();
        return rowsAffected;
    }

    public void deleteTodoItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO,
                COLUMN_TODO_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAllTodoItems() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TODO, null, null);
        db.close();
    }

    public int getTodoItemsCount() {
        String countQuery = "SELECT * FROM " + TABLE_TODO;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }


    public long addDevResource(DevResource resource) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DEV_RESOURCE_URL, resource.getUrl());
        values.put(COLUMN_DEV_RESOURCE_SAVED, resource.isSaved() ? 1 : 0);

        long id = db.insert(TABLE_DEV_RESOURCES, null, values);
        db.close();
        return id;
    }

    @SuppressLint("Range")
    public DevResource getDevResource(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DEV_RESOURCES,
                new String[]{COLUMN_DEV_RESOURCE_ID, COLUMN_DEV_RESOURCE_URL, COLUMN_DEV_RESOURCE_SAVED},
                COLUMN_DEV_RESOURCE_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        DevResource resource = null;
        if (cursor != null && cursor.moveToFirst()) {
            resource = new DevResource();
            resource.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_DEV_RESOURCE_ID)));
            resource.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_DEV_RESOURCE_URL)));
            resource.setSaved(cursor.getInt(cursor.getColumnIndex(COLUMN_DEV_RESOURCE_SAVED)) == 1);
            cursor.close();
        }
        db.close();
        return resource;
    }

    @SuppressLint("Range")
    public List<DevResource> getAllDevResources() {
        List<DevResource> resourceList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_DEV_RESOURCES + " ORDER BY " + COLUMN_DEV_RESOURCE_ID + " ASC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DevResource resource = new DevResource();
                resource.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_DEV_RESOURCE_ID)));
                resource.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_DEV_RESOURCE_URL)));
                resource.setSaved(cursor.getInt(cursor.getColumnIndex(COLUMN_DEV_RESOURCE_SAVED)) == 1);
                resourceList.add(resource);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return resourceList;
    }

    @SuppressLint("Range")
    public List<DevResource> getSavedDevResources() {
        List<DevResource> resourceList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_DEV_RESOURCES + " WHERE " + COLUMN_DEV_RESOURCE_SAVED + " = 1";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                DevResource resource = new DevResource();
                resource.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_DEV_RESOURCE_ID)));
                resource.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_DEV_RESOURCE_URL)));
                resource.setSaved(true);
                resourceList.add(resource);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return resourceList;
    }

    public int updateDevResource(DevResource resource) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DEV_RESOURCE_URL, resource.getUrl());
        values.put(COLUMN_DEV_RESOURCE_SAVED, resource.isSaved() ? 1 : 0);

        int rowsAffected = db.update(TABLE_DEV_RESOURCES, values,
                COLUMN_DEV_RESOURCE_ID + " = ?",
                new String[]{String.valueOf(resource.getId())});
        db.close();
        return rowsAffected;
    }

    public void deleteDevResource(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DEV_RESOURCES,
                COLUMN_DEV_RESOURCE_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteAllDevResources() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DEV_RESOURCES, null, null);
        db.close();
    }

    public int getDevResourcesCount() {
        String countQuery = "SELECT * FROM " + TABLE_DEV_RESOURCES;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }
}