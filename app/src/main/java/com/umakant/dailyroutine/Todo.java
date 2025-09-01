package com.umakant.dailyroutine;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todos")
public class Todo {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String description;
    public boolean completed;
    public int priority; // 1=High, 2=Medium, 3=Low
    public long createdAt;
    public long reminderTime; // Scheduled reminder time
    public boolean hasReminder;

    public Todo(String title, String description, int priority) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.completed = false;
        this.createdAt = System.currentTimeMillis();
        this.reminderTime = 0;
        this.hasReminder = false;
    }
}