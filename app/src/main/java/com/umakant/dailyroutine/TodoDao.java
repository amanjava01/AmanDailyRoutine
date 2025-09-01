package com.umakant.dailyroutine;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

@Dao
public interface TodoDao {
    @Insert
    void insert(Todo todo);

    @Update
    void update(Todo todo);

    @Delete
    void delete(Todo todo);

    @Query("DELETE FROM todos")
    void deleteAll();

    @Query("SELECT * FROM todos WHERE completed = 0 ORDER BY priority ASC, createdAt DESC")
    LiveData<List<Todo>> getAllTodos();
    
    @Insert
    void insertHistory(TodoHistory history);
    
    @Query("SELECT * FROM todo_history ORDER BY completedAt DESC")
    LiveData<List<TodoHistory>> getAllHistory();
}