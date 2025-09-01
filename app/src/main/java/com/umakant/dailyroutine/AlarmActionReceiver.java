package com.umakant.dailyroutine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.app.NotificationManager;

public class AlarmActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        
        if ("STOP_ALARM".equals(action)) {
            // Stop alarm sound and vibration
            AlarmReceiver.stopAlarm(context);
            
            // Cancel specific notification
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(NotificationHelper.ALARM_NOTIFICATION_ID);
        }
    }
}