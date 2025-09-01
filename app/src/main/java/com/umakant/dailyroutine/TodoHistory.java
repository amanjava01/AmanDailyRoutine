package com.umakant.dailyroutine;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "todo_history")
public class TodoHistory {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String title;
    public String description;
    public int priority;
    public long completedAt;
    public long originalCreatedAt;

    public TodoHistory(String title, String description, int priority, long originalCreatedAt) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.originalCreatedAt = originalCreatedAt;
        this.completedAt = System.currentTimeMillis();
    }
}