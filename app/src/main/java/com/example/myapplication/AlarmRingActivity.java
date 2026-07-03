package com.example.myapplication;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AlarmRingActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;

    private TextView tvClock;
    private TextView tvProgress;
    private TextView tvQuestion;

    private EditText etAnswer;

    private Button btnSubmit;

    private ProgressBar progressBar;

    private MathQuestion currentQuestion;

    private int currentStep = 1;

    private final int MAX_STEPS = 5;

    private final Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);

        initView();

        updateClock();

        playAlarm();

        loadNewQuestion();

        btnSubmit.setOnClickListener(v -> checkAnswer());
    }

    private void initView() {

        tvClock = findViewById(R.id.tvClock);
        tvProgress = findViewById(R.id.tvProgress);
        tvQuestion = findViewById(R.id.tvQuestion);

        etAnswer = findViewById(R.id.etAnswer);

        btnSubmit = findViewById(R.id.btnSubmit);

        progressBar = findViewById(R.id.progressBar);

    }

    /**
     * Đồng hồ thời gian thực
     */
    private void updateClock() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                String time = new SimpleDateFormat(
                        "HH:mm",
                        Locale.getDefault()
                ).format(new Date());

                tvClock.setText(time);

                handler.postDelayed(this,1000);
            }
        };

        handler.post(runnable);

    }

    /**
     * Phát nhạc chuông
     */
    private void playAlarm() {

        String uriString = getIntent().getStringExtra("RINGTONE_URI");

        try {

            if (uriString != null && !uriString.isEmpty()) {

                Uri uri = Uri.parse(uriString);

                mediaPlayer = MediaPlayer.create(this, uri);

            } else {

                mediaPlayer = MediaPlayer.create(
                        this,
                        android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI
                );

            }

            if (mediaPlayer != null) {

                mediaPlayer.setLooping(true);

                mediaPlayer.start();

            }

        } catch (Exception e) {

            mediaPlayer = MediaPlayer.create(
                    this,
                    android.provider.Settings.System.DEFAULT_ALARM_ALERT_URI
            );

            if(mediaPlayer!=null){

                mediaPlayer.setLooping(true);

                mediaPlayer.start();

            }

        }

    }

    /**
     * Sinh câu hỏi mới
     */
    private void loadNewQuestion() {

        currentQuestion = new MathQuestion();

        tvProgress.setText("Mission " + currentStep + " / " + MAX_STEPS);

        progressBar.setProgress(currentStep);

        tvQuestion.setText(currentQuestion.getQuestionText());

        etAnswer.setText("");

    }

    /**
     * Kiểm tra đáp án
     */
    private void checkAnswer() {

        String input = etAnswer.getText().toString().trim();

        if(input.isEmpty()){

            Toast.makeText(this,"Nhập đáp án!",Toast.LENGTH_SHORT).show();

            return;

        }

        int answer;

        try{

            answer = Integer.parseInt(input);

        }catch (Exception e){

            Toast.makeText(this,"Sai định dạng!",Toast.LENGTH_SHORT).show();

            return;

        }

        if(currentQuestion.isCorrect(answer)){

            tvQuestion.animate()
                    .alpha(0f)
                    .setDuration(200)
                    .withEndAction(() -> {

                        if(currentStep<MAX_STEPS){

                            currentStep++;

                            loadNewQuestion();

                            tvQuestion.setAlpha(1f);

                        }else{

                            finishMission();

                        }

                    });

        }else{

            shakeView(etAnswer);

            etAnswer.setText("");

            Toast.makeText(this,"❌ Sai rồi!",Toast.LENGTH_SHORT).show();

        }

    }

    /**
     * Rung EditText khi nhập sai
     */
    private void shakeView(View view){

        Animation shake = new TranslateAnimation(-15,15,0,0);

        shake.setDuration(60);

        shake.setRepeatMode(Animation.REVERSE);

        shake.setRepeatCount(5);

        view.startAnimation(shake);

    }

    /**
     * Hoàn thành nhiệm vụ
     */
    private void finishMission(){

        Toast.makeText(
                this,
                "🎉 Alarm Dismissed!",
                Toast.LENGTH_LONG
        ).show();

        stopAlarm();

        finish();

    }

    /**
     * Dừng chuông
     */
    private void stopAlarm(){

        if(mediaPlayer!=null){

            if(mediaPlayer.isPlaying()){

                mediaPlayer.stop();

            }

            mediaPlayer.release();

            mediaPlayer=null;

        }

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Toast.makeText(
                this,
                "Giải hết bài mới được thoát!",
                Toast.LENGTH_SHORT
        ).show();

    }

    @Override
    protected void onDestroy() {

        super.onDestroy();

        handler.removeCallbacksAndMessages(null);

        stopAlarm();

    }

}