package com.example.myapplication;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

public class NotificationHelper {

    public static final String CHANNEL_ID = "WAKEUP_BRAIN";

    public static void createChannel(Context context){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel =
                    new NotificationChannel(
                            CHANNEL_ID,
                            "WakeUp Brain",
                            NotificationManager.IMPORTANCE_HIGH
                    );

            channel.setDescription("Alarm Notification");

            NotificationManager manager =
                    context.getSystemService(NotificationManager.class);

            if(manager != null){
                manager.createNotificationChannel(channel);
            }

        }

    }

}