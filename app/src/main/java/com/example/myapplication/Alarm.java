package com.example.myapplication;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "alarms") // Khai báo đây là một bảng trong database
public class Alarm {
    @PrimaryKey(autoGenerate = true) // Cần có ID tự tăng
    public int id;

    public int hour;
    public int minute;
    public String ringtoneUri;

    // Room yêu cầu constructor này
    public Alarm(int hour, int minute, String ringtoneUri) {
        this.hour = hour;
        this.minute = minute;
        this.ringtoneUri = ringtoneUri;
    }
}