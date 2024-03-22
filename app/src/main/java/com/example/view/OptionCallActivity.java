package com.example.view;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myappcall.R;

public class OptionCallActivity extends AppCompatActivity {

    ImageView imgCallVideo, imgCallMic;


    Boolean checkCallVideo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_option_call);


        checkCallVideo = (Boolean) getIntent().getExtras().get("checkCallVideo");

        imgCallVideo = this.<ImageView>findViewById(R.id.imgCallVideo);
        imgCallMic = this.<ImageView>findViewById(R.id.imgCallMicro);

        if (checkCallVideo) {
            imgCallMic.setVisibility(View.GONE);

            startAnimation(imgCallVideo);

            imgCallVideo.setOnClickListener(v -> {
                Intent intent = new Intent(OptionCallActivity.this, CallVideoActivity.class);
                startActivity(intent);
            });
        } else {
            imgCallVideo.setVisibility(View.GONE);
            startAnimation(imgCallMic);

            imgCallMic.setOnClickListener(v -> {
                Intent intent = new Intent(OptionCallActivity.this, CallMicActivity.class);
                startActivity(intent);
            });
        }

    }

    public void startAnimation(View view) {

        ImageView imageView = (ImageView) view;

        Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoom_anim);
        imageView.startAnimation(animation);
    }


}