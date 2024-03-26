package com.example.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.Model.User;
import com.example.myappcall.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.Timer;
import java.util.TimerTask;

public class CallMicActivity extends AppCompatActivity {


    Timer timer;
    TimerTask timerTask;
    Double time = 0.0;
    boolean timerStarted = false;

    TextView tvTime, tvName;

    private MediaPlayer mediaPlayer;

    ImageView pressEndCall;

    TextView tvEnded, pressTapClose;

    ShapeableImageView shapeableImageView;

    User user;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_call_mic);

        user = getIntent().getExtras().getParcelable("Object");
        tvTime = this.<TextView>findViewById(R.id.tvTimeCall);
        pressEndCall = this.<ImageView>findViewById(R.id.imgEndCallMic);
        tvName = findViewById(R.id.tvNameCall);
        tvName.setText(user.getPersonName());
        shapeableImageView = findViewById(R.id.circle);
        String s = user.getPersonAvt();
        int resourceId = getResources().getIdentifier(s, "drawable", this.getPackageName());
        shapeableImageView.setImageResource(resourceId);

        tvEnded = this.<TextView>findViewById(R.id.callMicEnded);
        pressTapClose = this.<TextView>findViewById(R.id.pressTapToClose);
        tvEnded.setVisibility(View.GONE);
        pressTapClose.setVisibility(View.GONE);

        timer = new Timer();

        mediaPlayer = new MediaPlayer();
        try {
            Log.d("4343434343", user.getUrlVideo());

            int resID = getResources().getIdentifier(user.getUrlVideo().trim(), "raw", getPackageName());
            String fileMp3 = "android.resource://" + getPackageName() + "/" + resID;

            mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(fileMp3));

            // chuanbi
            mediaPlayer.prepare();

            // start
            mediaPlayer.start();
            startTimer();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "looiio roi" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        pressEndCall.setOnClickListener(v -> {

            mediaPlayer.release();

            Intent intent = new Intent(CallMicActivity.this, MainActivity.class);

            startActivity(intent);

        });


        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                tvTime.setVisibility(View.GONE);
                pressEndCall.setVisibility(View.GONE);

                tvEnded.setVisibility(View.VISIBLE);
                pressTapClose.setVisibility(View.VISIBLE);

                startAnimation(pressTapClose);

                pressTapClose.setOnClickListener(v -> {
                    Intent intent = new Intent(CallMicActivity.this, MainActivity.class);
                    startActivity(intent);
                });
            }
        });

    }


    public void startStopTapped(View view) {
        if (timerStarted == false) {
            timerStarted = true;
            startTimer();

        } else {
            timerStarted = false;
            timerTask.cancel();
        }
    }

    private void startTimer() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        time++;
                        tvTime.setText(getTimerText());
                    }
                });
            }

        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private String getTimerText() {

        int rounded = (int) Math.round(time);
        int seconds = ((rounded % 86400) % 3600) % 60;
        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) / 3600);

        return formatTime(seconds, minutes, hours);

    }

    private String formatTime(int seconds, int minutes, int hours) {
        return String.format("%02d", hours) + " : " + String.format("%02d", minutes) + " : " + String.format("%02d", seconds);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

    }


    public void startAnimation(View view) {

        TextView textView = (TextView) view;

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoom_anim);
        textView.startAnimation(animation);
    }


}