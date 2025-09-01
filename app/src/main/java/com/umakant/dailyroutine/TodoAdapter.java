package com.umakant.dailyroutine;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.TodoViewHolder> {
    private List<Todo> todos = new ArrayList<>();
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Todo todo);
        void onDeleteClick(Todo todo);
        void onCheckboxClick(Todo todo);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public TodoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_todo, parent, false);
        return new TodoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoViewHolder holder, int position) {
        Todo todo = todos.get(position);
        holder.bind(todo);
    }

    @Override
    public int getItemCount() {
        return todos.size();
    }

    public void setTodos(List<Todo> todos) {
        this.todos = todos;
        notifyDataSetChanged();
    }

    class TodoViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDescription, tvDate, tvPriority, tvReminder;
        private CheckBox cbCompleted;
        private ImageButton btnDelete;

        public TodoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvPriority = itemView.findViewById(R.id.tv_priority);
            tvReminder = itemView.findViewById(R.id.tv_reminder);
            cbCompleted = itemView.findViewById(R.id.cb_completed);
            btnDelete = itemView.findViewById(R.id.btn_delete);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(todos.get(position));
                }
            });

            btnDelete.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onDeleteClick(todos.get(position));
                }
            });

            cbCompleted.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onCheckboxClick(todos.get(position));
                }
            });
        }

        public void bind(Todo todo) {
            tvTitle.setText(todo.title);
            tvDescription.setText(todo.description);
            
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
            tvDate.setText(sdf.format(new Date(todo.createdAt)));

            String priorityText = "";
            int priorityColor = 0;
            switch (todo.priority) {
                case 1:
                    priorityText = "HIGH";
                    priorityColor = itemView.getContext().getColor(R.color.high_priority);
                    break;
                case 2:
                    priorityText = "MED";
                    priorityColor = itemView.getContext().getColor(R.color.medium_priority);
                    break;
                case 3:
                    priorityText = "LOW";
                    priorityColor = itemView.getContext().getColor(R.color.low_priority);
                    break;
            }
            tvPriority.setText(priorityText);
            tvPriority.setBackgroundColor(priorityColor);

            cbCompleted.setChecked(todo.completed);
            
            // Show reminder info
            if (todo.hasReminder && todo.reminderTime > 0) {
                SimpleDateFormat reminderFormat = new SimpleDateFormat("MMM dd, hh:mm a", Locale.getDefault());
                tvReminder.setText("⏰ " + reminderFormat.format(new Date(todo.reminderTime)));
                tvReminder.setVisibility(View.VISIBLE);
            } else {
                tvReminder.setVisibility(View.GONE);
            }

            if (todo.completed) {
                tvTitle.setPaintFlags(tvTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                itemView.setAlpha(0.6f);
            } else {
                tvTitle.setPaintFlags(tvTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                itemView.setAlpha(1.0f);
            }
        }
    }
}