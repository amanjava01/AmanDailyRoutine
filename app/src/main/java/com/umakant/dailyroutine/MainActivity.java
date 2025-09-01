package com.umakant.dailyroutine;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Calendar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TodoViewModel todoViewModel;
    private TodoAdapter adapter;
    private HistoryAdapter historyAdapter;
    private TextView tvTime, tvDate, tvTotal, tvCompleted, tvPending, tvProgress, tvMotivation;
    private ProgressBar progressBar;
    private Button btnTasks, btnHistory;
    private RecyclerView recyclerView;
    private Handler timeHandler;
    private Runnable timeRunnable;
    private int lastPendingCount = 0;
    private boolean showingHistory = false;

    private String[] motivationalMessages = {
        "Let's start your productive day!",
        "Every task completed is progress!",
        "You're doing great, keep going!",
        "Focus and finish strong!",
        "Almost there, don't give up!",
        "Amazing progress today!",
        "Congratulations! All done! 🎉"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        setupViewModel();
        setupFab();
        setupNavigation();
        setupRealTimeUpdates();
        NotificationHelper.createNotificationChannel(this);
    }

    private void initViews() {
        tvTime = findViewById(R.id.tv_time);
        tvDate = findViewById(R.id.tv_date);
        tvTotal = findViewById(R.id.tv_total);
        tvCompleted = findViewById(R.id.tv_completed);
        tvPending = findViewById(R.id.tv_pending);
        tvProgress = findViewById(R.id.tv_progress);
        tvMotivation = findViewById(R.id.tv_motivation);
        progressBar = findViewById(R.id.progress_bar);
        btnTasks = findViewById(R.id.btn_tasks);
        btnHistory = findViewById(R.id.btn_history);
        recyclerView = findViewById(R.id.recycler_view);
    }

    private void setupRecyclerView() {
        adapter = new TodoAdapter();
        historyAdapter = new HistoryAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Todo todo) {
                editTodo(todo);
            }

            @Override
            public void onDeleteClick(Todo todo) {
                deleteTodo(todo);
            }

            @Override
            public void onCheckboxClick(Todo todo) {
                if (!todo.completed) {
                    // Moving to completed - add to history and delete from tasks
                    TodoHistory history = new TodoHistory(todo.title, todo.description, todo.priority, todo.createdAt);
                    todoViewModel.insertHistory(history);
                    todoViewModel.delete(todo);
                } else {
                    todo.completed = !todo.completed;
                    todoViewModel.update(todo);
                }
            }
        });
    }

    private void setupViewModel() {
        todoViewModel = new ViewModelProvider(this).get(TodoViewModel.class);
        todoViewModel.getAllTodos().observe(this, todos -> {
            if (!showingHistory) {
                adapter.setTodos(todos);
                updateStats(todos);
            }
        });
        
        todoViewModel.getAllHistory().observe(this, history -> {
            if (showingHistory) {
                historyAdapter.setHistory(history);
            }
        });
    }

    private void setupFab() {
        FloatingActionButton fabAdd = findViewById(R.id.fab_add);
        fabAdd.setOnClickListener(v -> addNewTodo());
    }

    private void setupRealTimeUpdates() {
        timeHandler = new Handler();
        timeRunnable = new Runnable() {
            @Override
            public void run() {
                updateDateTime();
                timeHandler.postDelayed(this, 1000);
            }
        };
        timeHandler.post(timeRunnable);
    }

    private void updateDateTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        Date now = new Date();
        tvTime.setText(timeFormat.format(now));
        tvDate.setText(dateFormat.format(now));
    }

    private void updateStats(List<Todo> todos) {
        if (todos == null) return;

        int total = todos.size();
        int completed = 0;
        for (Todo todo : todos) {
            if (todo.completed) completed++;
        }
        int pending = total - completed;

        tvTotal.setText(String.valueOf(total));
        tvCompleted.setText(String.valueOf(completed));
        tvPending.setText(String.valueOf(pending));

        int progress = total > 0 ? (completed * 100) / total : 0;
        progressBar.setProgress(progress);
        tvProgress.setText("Progress: " + progress + "%");

        updateMotivationalMessage(progress);

        if (pending > lastPendingCount && pending > 0) {
            NotificationHelper.showTaskReminder(this, pending);
        }
        lastPendingCount = pending;
    }

    private void updateMotivationalMessage(int progress) {
        String message;
        if (progress == 100) {
            message = motivationalMessages[6];
        } else if (progress >= 80) {
            message = motivationalMessages[5];
        } else if (progress >= 60) {
            message = motivationalMessages[4];
        } else if (progress >= 40) {
            message = motivationalMessages[3];
        } else if (progress >= 20) {
            message = motivationalMessages[2];
        } else if (progress > 0) {
            message = motivationalMessages[1];
        } else {
            message = motivationalMessages[0];
        }
        tvMotivation.setText(message);
    }

    private void addNewTodo() {
        showTodoDialog(null);
    }

    private void editTodo(Todo todo) {
        showTodoDialog(todo);
    }

    private void showTodoDialog(Todo todo) {
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_todo, null);
        EditText etTitle = dialogView.findViewById(R.id.et_title);
        EditText etDescription = dialogView.findViewById(R.id.et_description);
        RadioGroup rgPriority = dialogView.findViewById(R.id.rg_priority);
        CheckBox cbReminder = dialogView.findViewById(R.id.cb_reminder);
        Button btnDateTime = dialogView.findViewById(R.id.btn_date_time);
        
        final long[] selectedReminderTime = {0};

        boolean isEdit = todo != null;
        if (isEdit) {
            etTitle.setText(todo.title);
            etDescription.setText(todo.description);
            switch (todo.priority) {
                case 1: rgPriority.check(R.id.rb_high); break;
                case 2: rgPriority.check(R.id.rb_medium); break;
                case 3: rgPriority.check(R.id.rb_low); break;
            }
            if (todo.hasReminder) {
                cbReminder.setChecked(true);
                btnDateTime.setEnabled(true);
                selectedReminderTime[0] = todo.reminderTime;
                updateDateTimeButton(btnDateTime, todo.reminderTime);
            }
        }
        
        cbReminder.setOnCheckedChangeListener((buttonView, isChecked) -> {
            btnDateTime.setEnabled(isChecked);
            if (!isChecked) {
                selectedReminderTime[0] = 0;
                btnDateTime.setText("Select Date & Time");
            }
        });
        
        btnDateTime.setOnClickListener(v -> showDateTimePicker(btnDateTime, selectedReminderTime));

        new AlertDialog.Builder(this)
                .setTitle(isEdit ? "Edit Todo" : "Add New Todo")
                .setView(dialogView)
                .setPositiveButton(isEdit ? "Update" : "Add", (d, which) -> {
                    String title = etTitle.getText().toString().trim();
                    String description = etDescription.getText().toString().trim();

                    if (title.isEmpty()) {
                        Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int priority = 2;
                    int checkedId = rgPriority.getCheckedRadioButtonId();
                    if (checkedId == R.id.rb_high) priority = 1;
                    else if (checkedId == R.id.rb_medium) priority = 2;
                    else if (checkedId == R.id.rb_low) priority = 3;

                    if (isEdit) {
                        // Cancel old alarm if exists
                        if (todo.hasReminder) {
                            AlarmManager.cancelAlarm(MainActivity.this, todo.id);
                        }
                        
                        todo.title = title;
                        todo.description = description;
                        todo.priority = priority;
                        todo.hasReminder = cbReminder.isChecked();
                        todo.reminderTime = selectedReminderTime[0];
                        
                        todoViewModel.update(todo);
                        
                        // Set new alarm if reminder is enabled
                        if (todo.hasReminder && selectedReminderTime[0] > System.currentTimeMillis()) {
                            AlarmManager.setAlarm(MainActivity.this, todo);
                        }
                        
                        Toast.makeText(this, "Todo updated", Toast.LENGTH_SHORT).show();
                    } else {
                        Todo newTodo = new Todo(title, description, priority);
                        newTodo.hasReminder = cbReminder.isChecked();
                        newTodo.reminderTime = selectedReminderTime[0];
                        
                        todoViewModel.insert(newTodo);
                        
                        // Set alarm if reminder is enabled
                        if (newTodo.hasReminder && selectedReminderTime[0] > System.currentTimeMillis()) {
                            // We'll set alarm after getting the ID from database
                            new Handler().postDelayed(() -> {
                                // This is a workaround - in real app you'd get the ID from insert callback
                                AlarmManager.setAlarm(MainActivity.this, newTodo);
                            }, 100);
                        }
                        
                        Toast.makeText(this, "Todo added", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void deleteTodo(Todo todo) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Todo")
                .setMessage("Are you sure?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    todoViewModel.delete(todo);
                    Toast.makeText(this, "Todo deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void showDateTimePicker(Button btnDateTime, long[] selectedReminderTime) {
        Calendar calendar = Calendar.getInstance();
        
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    
                    TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                            (timeView, hourOfDay, minute) -> {
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, 0);
                                
                                selectedReminderTime[0] = calendar.getTimeInMillis();
                                updateDateTimeButton(btnDateTime, selectedReminderTime[0]);
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false);
                    timePickerDialog.show();
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }
    
    private void updateDateTimeButton(Button btnDateTime, long timeInMillis) {
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
        btnDateTime.setText(sdf.format(new Date(timeInMillis)));
    }
    
    private void setupNavigation() {
        btnTasks.setOnClickListener(v -> {
            showingHistory = false;
            recyclerView.setAdapter(adapter);
            btnTasks.setEnabled(false);
            btnHistory.setEnabled(true);
        });
        
        btnHistory.setOnClickListener(v -> {
            showingHistory = true;
            recyclerView.setAdapter(historyAdapter);
            todoViewModel.getAllHistory().observe(this, history -> historyAdapter.setHistory(history));
            btnTasks.setEnabled(true);
            btnHistory.setEnabled(false);
        });
        
        // Default to tasks view
        btnTasks.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timeHandler != null && timeRunnable != null) {
            timeHandler.removeCallbacks(timeRunnable);
        }
    }
}