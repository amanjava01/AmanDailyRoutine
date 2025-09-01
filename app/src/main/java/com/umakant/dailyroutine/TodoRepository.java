package com.umakant.dailyroutine;

import android.app.Application;
import androidx.lifecycle.LiveData;
import java.util.List;

public class TodoRepository {
    private TodoDao todoDao;
    private LiveData<List<Todo>> allTodos;

    public TodoRepository(Application application) {
        TodoDatabase db = TodoDatabase.getDatabase(application);
        todoDao = db.todoDao();
        allTodos = todoDao.getAllTodos();
    }

    public LiveData<List<Todo>> getAllTodos() {
        return allTodos;
    }

    public void insert(Todo todo) {
        TodoDatabase.databaseWriteExecutor.execute(() -> todoDao.insert(todo));
    }

    public void update(Todo todo) {
        TodoDatabase.databaseWriteExecutor.execute(() -> todoDao.update(todo));
    }

    public void delete(Todo todo) {
        TodoDatabase.databaseWriteExecutor.execute(() -> todoDao.delete(todo));
    }

    public void deleteAll() {
        TodoDatabase.databaseWriteExecutor.execute(() -> todoDao.deleteAll());
    }
    
    public void insertHistory(TodoHistory history) {
        TodoDatabase.databaseWriteExecutor.execute(() -> todoDao.insertHistory(history));
    }
    
    public LiveData<List<TodoHistory>> getAllHistory() {
        return todoDao.getAllHistory();
    }
}