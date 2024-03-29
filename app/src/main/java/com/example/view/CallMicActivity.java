package com.example.view;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
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

import com.bumptech.glide.Glide;
import com.example.Model.User;
import com.example.myappcall.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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

    @SuppressLint({"WrongViewCast", "CheckResult"})
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

        Glide.with(this).load(user.getPersonAvt()).into(shapeableImageView);

        tvEnded = this.<TextView>findViewById(R.id.callMicEnded);
        pressTapClose = this.<TextView>findViewById(R.id.pressTapToClose);
        tvEnded.setVisibility(View.GONE);
        pressTapClose.setVisibility(View.GONE);

        timer = new Timer();

        mediaPlayer = new MediaPlayer();

          try {
                mediaPlayer.setDataSource(user.getUrlVideo());
                // chuanbi
                mediaPlayer.prepare();
                // start
                mediaPlayer.start();
                startTimer();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "looiio roi" + e.toString(), Toast.LENGTH_SHORT).show();
            }



          handleClickEvent();

    }

    @SuppressLint("CheckResult")
    private void playMedia() {
        Observable.fromCallable(() -> {
                    String audioUrl = user.getUrlVideo();
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(audioUrl)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        }
                        return response.body().byteStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                        throw e; // Throw exception để onError xử lý
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(inputStream -> {
                    try {
                        File tempFile = File.createTempFile("temp_audio", ".mp3", getCacheDir());
                        FileOutputStream outputStream = new FileOutputStream(tempFile);
                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = inputStream.read(buffer)) != -1) {
                            outputStream.write(buffer, 0, bytesRead);
                        }
                        inputStream.close();
                        outputStream.close();

                        // Phát âm thanh từ tệp tin MP3 đã tải xuống
                        MediaPlayer mediaPlayer = new MediaPlayer();
                        mediaPlayer.setDataSource(tempFile.getAbsolutePath());
                        mediaPlayer.prepare();
                        mediaPlayer.start();

                        mediaPlayer.setOnCompletionListener(mp -> {
                            mediaPlayer.release();
                        });

                        mediaPlayer.setOnErrorListener((mp, what, extra) -> {
                            mediaPlayer.release();
                            return false;
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }, throwable -> {
                    Log.e("RxJava", "Error occurred: " + throwable.getMessage());
                    // Xử lý lỗi mạng ở đây, ví dụ: hiển thị thông báo cho người dùng
                });

    }

    private void handleClickEvent() {
        // su ly button end call
        pressEndCall.setOnClickListener(v -> {

            MainActivity.checkSoundAndVibarte();

            mediaPlayer.release();
            Intent intent = new Intent(CallMicActivity.this, MainActivity.class);
            startActivity(intent);
        });

        // su ly button mediaplay
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