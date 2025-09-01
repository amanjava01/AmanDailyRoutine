package com.umakant.dailyroutine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {
    private List<TodoHistory> historyList = new ArrayList<>();

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
        return new HistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        TodoHistory history = historyList.get(position);
        holder.bind(history);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public void setHistory(List<TodoHistory> historyList) {
        this.historyList = historyList;
        notifyDataSetChanged();
    }

    class HistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvDescription, tvCompletedDate, tvPriority;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvCompletedDate = itemView.findViewById(R.id.tv_completed_date);
            tvPriority = itemView.findViewById(R.id.tv_priority);
        }

        public void bind(TodoHistory history) {
            tvTitle.setText(history.title);
            tvDescription.setText(history.description);
            
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm a", Locale.getDefault());
            tvCompletedDate.setText("✅ Completed: " + sdf.format(new Date(history.completedAt)));

            String priorityText = "";
            int priorityColor = 0;
            switch (history.priority) {
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
        }
    }
}