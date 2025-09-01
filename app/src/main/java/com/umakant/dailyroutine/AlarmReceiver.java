package com.umakant.dailyroutine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.media.Ringtone;
import android.os.Vibrator;

public class AlarmReceiver extends BroadcastReceiver {
    private static Ringtone currentRingtone;
    private static Vibrator currentVibrator;
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");
        
        // Start vibration and ringtone immediately
        startVibration(context);
        startAlarmSound(context);
        
        // Show popup confirmation dialog
        Intent popupIntent = new Intent(context, AlarmPopupActivity.class);
        popupIntent.putExtra("title", title);
        popupIntent.putExtra("description", description);
        popupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(popupIntent);
    }
    
    public static void startVibration(Context context) {
        currentVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (currentVibrator != null && currentVibrator.hasVibrator()) {
            long[] pattern = {0, 1000, 500, 1000, 500, 1000};
            currentVibrator.vibrate(pattern, 0); // Repeat indefinitely
        }
    }
    
    public static void startAlarmSound(Context context) {
        try {
            Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            }
            currentRingtone = RingtoneManager.getRingtone(context, alarmUri);
            if (currentRingtone != null) {
                currentRingtone.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void stopAlarm(Context context) {
        // Stop ringtone
        if (currentRingtone != null && currentRingtone.isPlaying()) {
            currentRingtone.stop();
            currentRingtone = null;
        }
        
        // Stop vibration using static reference
        if (currentVibrator != null) {
            currentVibrator.cancel();
            currentVibrator = null;
        }
        
        // Fallback: get vibrator service and cancel
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.cancel();
        }
    }
}