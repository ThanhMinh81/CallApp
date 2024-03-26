package com.example.view;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myappcall.R;

import java.util.ArrayList;
import java.util.List;

public class PlayVideoActivity extends AppCompatActivity {
    private VideoView videoViewPlay;
    private ImageView imgHomePlayVideo,  imgMenuPlayVideo, imgRepeatVideo, imgPauseVideo,  imgNextVideo;
    ProgressBar progressBar ;

    private List<String> videoList = new ArrayList<>();


//    rogressBar spinner = new android.widget.ProgressBar(
//            context,
//            null,
//            android.R.attr.progressBarStyle);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.playvideo_activity);


        videoViewPlay = findViewById(R.id.videoViewPlay);
        imgHomePlayVideo = findViewById(R.id.imgHomePlayVideo);
        imgMenuPlayVideo = findViewById(R.id.imgMenuPlayVideo);
        imgRepeatVideo = findViewById(R.id.imgRepeatVideo);
        imgPauseVideo = findViewById(R.id.imgPauseVideo);
        imgNextVideo = findViewById(R.id.imgNextVideo);
        progressBar = findViewById(R.id.processBar);
        progressBar.setVisibility(View.GONE);
        progressBar.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);



        videoList.add("android.resource://" + getPackageName() + "/" + R.raw.video1);
        videoList.add("android.resource://" + getPackageName() + "/" + R.raw.video_5);
        videoList.add("android.resource://" + getPackageName() + "/" + R.raw.video_3);
        videoList.add("android.resource://" + getPackageName() + "/" + R.raw.video_4);



        // Đặt video muốn phát trong VideoView
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.video1;
        videoViewPlay.setVideoURI(Uri.parse(videoPath));


        videoViewPlay.start();

        // Bắt sự kiện khi video đã sẵn sàng phát
        videoViewPlay.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                // Phát video khi đã sẵn sàng
                videoViewPlay.start();
            }
        });



        // Sự kiện khi click vào nút tạm dừng
        imgPauseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (videoViewPlay.isPlaying()) {
                    videoViewPlay.pause();
                } else {
                    videoViewPlay.start();
                }
            }
        });

        // Sự kiện khi click vào nút phát lại từ đầu
        imgRepeatVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoViewPlay.seekTo(0);
                videoViewPlay.start();
            }
        });


        // Sự kiện khi click vào nút Next
//        imgNextVideo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                if (!videoList.isEmpty()) {
//                    String nextVideoUri = videoList.remove(0);
//                    videoViewPlay.setVideoURI(Uri.parse(nextVideoUri));
//                    videoViewPlay.start();
//                }
//
//            }
//        });

        imgNextVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kiểm tra xem danh sách video có trống hay không
                if (!videoList.isEmpty()) {
                    // Tạm dừng video hiện tại
                    videoViewPlay.pause();
                    // Hiển thị thanh tiến trình
                    progressBar.setVisibility(View.VISIBLE);
                    // Tạo một Handler để chờ 0.5 giây trước khi chuyển sang video mới
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // Lấy video tiếp theo trong danh sách
                            String nextVideoUri = videoList.remove(0);
                            // Đặt video muốn phát trong VideoView
                            videoViewPlay.setVideoURI(Uri.parse(nextVideoUri));
                            // Bắt đầu phát video mới
                            videoViewPlay.start();
                            // Ẩn thanh tiến trình
                            progressBar.setVisibility(View.GONE);
                        }
                    }, 500); // 0.5 giây
                }
            }
        });




    }

}
