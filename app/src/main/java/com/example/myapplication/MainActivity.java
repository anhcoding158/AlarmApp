package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int PICK_AUDIO = 100;

    private TimePicker timePicker;
    private Spinner spDifficulty;

    private Button btnPickMusic;
    private Button btnSetAlarm;

    private TextView tvMusicName;

    private String selectedRingtoneUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NotificationHelper.createChannel(this);

        initView();

        initSpinner();

        loadSetting();

        btnPickMusic.setOnClickListener(v -> openMusicPicker());

        btnSetAlarm.setOnClickListener(v -> saveAlarm());
    }

    private void initView() {

        timePicker = findViewById(R.id.timePicker);

        btnPickMusic = findViewById(R.id.btnPickMusic);

        btnSetAlarm = findViewById(R.id.btnSetAlarm);

        spDifficulty = findViewById(R.id.spDifficulty);

        tvMusicName = findViewById(R.id.tvMusicName);

        timePicker.setIs24HourView(true);
    }

    private void initSpinner() {

        String[] level = {
                "Easy",
                "Medium",
                "Hard"
        };

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        level
                );

        spDifficulty.setAdapter(adapter);
    }

    private void openMusicPicker() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("audio/*");

        startActivityForResult(intent, PICK_AUDIO);

    }

    private void saveAlarm() {

        Alarm alarm = new Alarm(
                timePicker.getHour(),
                timePicker.getMinute(),
                selectedRingtoneUri
        );

        AlarmScheduler.scheduleAlarm(this, alarm);

        SharedPreferences.Editor editor =
                getSharedPreferences("setting", MODE_PRIVATE).edit();

        editor.putInt("difficulty", spDifficulty.getSelectedItemPosition());

        editor.apply();

        Toast.makeText(
                this,
                "Alarm Saved Successfully!",
                Toast.LENGTH_SHORT
        ).show();

    }

    private void loadSetting() {

        SharedPreferences pref =
                getSharedPreferences("setting", MODE_PRIVATE);

        spDifficulty.setSelection(
                pref.getInt("difficulty", 0)
        );

    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_AUDIO
                && resultCode == RESULT_OK
                && data != null) {

            Uri uri = data.getData();

            if (uri != null) {

                getContentResolver()
                        .takePersistableUriPermission(
                                uri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );

                selectedRingtoneUri = uri.toString();

                tvMusicName.setText("Selected ✔");

                Toast.makeText(
                        this,
                        "Music Selected",
                        Toast.LENGTH_SHORT
                ).show();
            }
        }

    }

}