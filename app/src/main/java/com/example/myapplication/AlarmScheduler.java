package com.example.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.util.Calendar;

public class AlarmScheduler {

    private static final String TAG = "WakeUpBrain";
    private static final int REQUEST_CODE = 1001;

    public static void scheduleAlarm(Context context, Alarm alarm) {

        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager == null) {
            Log.e(TAG, "AlarmManager is null");
            return;
        }

        Intent intent = new Intent(context, AlarmReceiver.class);

        // Truyền dữ liệu sang AlarmReceiver
        intent.putExtra("RINGTONE_URI", alarm.ringtoneUri);
        intent.putExtra("HOUR", alarm.hour);
        intent.putExtra("MINUTE", alarm.minute);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context,
                REQUEST_CODE,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, alarm.hour);
        calendar.set(Calendar.MINUTE, alarm.minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Nếu giờ đã qua thì đặt sang ngày mai
        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Log.d(TAG,
                "Alarm Time : "
                        + calendar.getTime().toString());

        try {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {

                if (!alarmManager.canScheduleExactAlarms()) {

                    Log.w(TAG, "Exact Alarm Permission Not Granted");

                    Intent permissionIntent =
                            new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);

                    permissionIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                    context.startActivity(permissionIntent);

                    return;
                }
            }

            alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    pendingIntent
            );

            Log.d(TAG, "Alarm Scheduled Successfully");

        } catch (Exception e) {

            Log.e(TAG, "Schedule Alarm Failed", e);

        }

    }

    /**
     * Hủy báo thức
     */
    public static void cancelAlarm(Context context) {

        AlarmManager alarmManager =
                (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent =
                new Intent(context, AlarmReceiver.class);

        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(
                        context,
                        REQUEST_CODE,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                                | PendingIntent.FLAG_IMMUTABLE
                );

        if (alarmManager != null) {

            alarmManager.cancel(pendingIntent);

            Log.d(TAG, "Alarm Cancelled");

        }

    }

}