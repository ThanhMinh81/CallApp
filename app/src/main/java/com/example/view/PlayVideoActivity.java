package com.example.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Fragment.CallFragment;
import com.example.Model.User;
import com.example.myappcall.R;

import java.util.ArrayList;
import java.util.List;

public class PlayVideoActivity extends AppCompatActivity {
    private VideoView videoViewPlay;
    private ImageView imgHomePlayVideo, imgMenuPlayVideo, imgRepeatVideo, imgPauseVideo, imgNextVideo;
    ProgressBar progressBar;

    private List<String> videoList = new ArrayList<>();

    int totalVideo, currentVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.playvideo_activity);

        initWidget();
        handleClick();

    }

    private void handleClick() {
        // pause
        imgPauseVideo.setOnClickListener(view -> {
            if (videoViewPlay.isPlaying()) {
                videoViewPlay.pause();
                imgPauseVideo.setImageResource(R.drawable.ic_play2);
            } else {
                videoViewPlay.start();
                imgPauseVideo.setImageResource(R.drawable.ic_pause);
            }
        });

        // repeat
        imgRepeatVideo.setOnClickListener(view -> {
            videoViewPlay.seekTo(0);
            videoViewPlay.start();
        });

        imgMenuPlayVideo.setOnClickListener(v -> {
            finish();
        });


        imgNextVideo.setOnClickListener(view -> {
            if (!videoList.isEmpty()) {

                videoViewPlay.pause();
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if (currentVideo < totalVideo) {
                            currentVideo++;
                            Log.d("ext121212", currentVideo + " ");
                            String nextVideoUri = videoList.get(currentVideo);
                            videoViewPlay.setVideoURI(Uri.parse(nextVideoUri));
                            videoViewPlay.start();
                            progressBar.setVisibility(View.GONE);
                        } else {
                            Log.d("extS1212", currentVideo + " ");
                            String nextVideoUri = videoList.remove((totalVideo - 1));
                            videoViewPlay.setVideoURI(Uri.parse(nextVideoUri));
                            videoViewPlay.start();
                            progressBar.setVisibility(View.GONE);
                        }

                    }
                }, 500);
            }
        });

        imgHomePlayVideo.setOnClickListener(v -> {
            try {

                Intent intent = new Intent(PlayVideoActivity.this, ListVideoActivity.class);
                startActivityForResult(intent, 10);

            } catch (Exception e) {
                Log.d("|#@32323", e.toString());
            }

        });
    }

    private void initWidget() {
        videoViewPlay = findViewById(R.id.videoViewPlay);
        imgHomePlayVideo = findViewById(R.id.imgHomePlayVideo);
        imgMenuPlayVideo = findViewById(R.id.imgMenuPlayVideo);
        imgRepeatVideo = findViewById(R.id.imgRepeatVideo);
        imgPauseVideo = findViewById(R.id.imgPauseVideo);
        imgNextVideo = findViewById(R.id.imgNextVideo);
        progressBar = findViewById(R.id.processBar);
        progressBar.setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);


        // value for listvideo
        for (User user : CallFragment.userArrayList) {
//            int resID = getResources().getIdentifier(user.getUrlVideo().trim(), "raw", getPackageName());
//            String videoPath = "android.resource://" + getPackageName() + "/" + resID;
            videoList.add(user.getUrlVideo());
        }

        totalVideo = videoList.size();
        currentVideo = 0;

//        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video1;

//        videoViewPlay.setVideoURI(Uri.parse(videoPath));
        videoViewPlay.setVideoPath(videoList.get(0));

        videoViewPlay.start();

        videoViewPlay.setOnPreparedListener(mediaPlayer -> videoViewPlay.start());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10) {
            if (data != null) {
                User user = data.getParcelableExtra("Object");

                for (int i = 0; i < videoList.size(); i++) {
                    if (videoList.get(i).contains(user.getUrlVideo())) {
                        currentVideo = i;
                        Log.d("23045235", currentVideo + " ");
                    }
                }
                videoViewPlay.setVideoPath(user.getUrlVideo());
                videoViewPlay.start();
            }
        }

    }
}
