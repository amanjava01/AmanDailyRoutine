package com.umakant.dailyroutine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

public class AlarmPopupActivity extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Show on lock screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        
        showConfirmationDialog(title, description);
    }
    
    private void showConfirmationDialog(String title, String description) {
        new AlertDialog.Builder(this)
                .setTitle("⏰ Task Reminder")
                .setMessage("Time for: " + title + "\n\n" + description + "\n\nAcknowledge reminder?")
                .setPositiveButton("YES", (dialog, which) -> {
                    // Stop vibration and ringtone
                    AlarmReceiver.stopAlarm(this);
                    
                    finish();
                })
                .setNegativeButton("NO", (dialog, which) -> {
                    // Keep alarm running, just close dialog
                    // Show stop notification for user to stop later
                    NotificationHelper.showAlarmNotification(this, title, description);
                    
                    finish();
                })
                .setCancelable(false)
                .show();
    }
}