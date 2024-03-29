package com.example.view;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
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

            MainActivity.checkSoundAndVibarte();

            if (videoViewPlay.isPlaying()) {
                videoViewPlay.pause();
                imgPauseVideo.setImageResource(R.drawable.ic_play2);
            } else {
                videoViewPlay.start();
                imgPauseVideo.setImageResource(R.drawable.ic_pause);
            }

        });

        videoViewPlay.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    Log.d("fwetffiaf","2323480923480");
                    imgPauseVideo.setImageResource(R.drawable.ic_pause);
                    return true;
                }
                return false;
            }
        });

        videoViewPlay.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // phat xong thi pause video do
                imgPauseVideo.setImageResource(R.drawable.ic_play2);



            }
        });


        // repeat
        imgRepeatVideo.setOnClickListener(view -> {

            MainActivity.checkSoundAndVibarte();

            videoViewPlay.seekTo(0);
            videoViewPlay.start();
            imgPauseVideo.setImageResource(R.drawable.ic_pause);

        });

        imgMenuPlayVideo.setOnClickListener(v -> {

            MainActivity.checkSoundAndVibarte();


            finish();
        });


        imgNextVideo.setOnClickListener(view -> {

            MainActivity.checkSoundAndVibarte();

            if (!videoList.isEmpty()) {

                videoViewPlay.pause();
                progressBar.setVisibility(View.VISIBLE);


                if (currentVideo < totalVideo) {
                    currentVideo++;
                    Log.d("ext121212", currentVideo + " ");
                    String nextVideoUri = videoList.get(currentVideo);
                    videoViewPlay.setVideoURI(Uri.parse(nextVideoUri));
                } else {
                    Log.d("extS1212", currentVideo + " ");
                    String nextVideoUri = videoList.remove((totalVideo - 1));
                    videoViewPlay.setVideoURI(Uri.parse(nextVideoUri));
                }


                videoViewPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        progressBar.setVisibility(View.GONE);
                        videoViewPlay.start();
                    }
                });


            }
        });

        imgHomePlayVideo.setOnClickListener(v -> {

            MainActivity.checkSoundAndVibarte();


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
//        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);


        // value for listvideo
        for (User user : CallFragment.userArrayList) {
            videoList.add(user.getUrlVideo());
        }

        totalVideo = videoList.size();
        currentVideo = 0;


        videoViewPlay.setVideoPath(videoList.get(0));

        progressBar.setVisibility(View.VISIBLE);

        videoViewPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progressBar.setVisibility(View.GONE);
                videoViewPlay.start();
            }
        });


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
