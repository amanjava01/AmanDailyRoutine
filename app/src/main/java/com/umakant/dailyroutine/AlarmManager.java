package com.umakant.dailyroutine;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmManager {
    
    public static void setAlarm(Context context, Todo todo) {
        if (!todo.hasReminder || todo.reminderTime <= System.currentTimeMillis()) {
            return;
        }
        
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("title", todo.title);
        intent.putExtra("description", todo.description);
        
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context, 
            todo.id, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        alarmManager.setExactAndAllowWhileIdle(
            android.app.AlarmManager.RTC_WAKEUP,
            todo.reminderTime,
            pendingIntent
        );
    }
    
    public static void cancelAlarm(Context context, int todoId) {
        android.app.AlarmManager alarmManager = (android.app.AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
            context, 
            todoId, 
            intent, 
            PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        
        alarmManager.cancel(pendingIntent);
    }
}