package com.example.view;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.Model.User;
import com.example.myappcall.R;
import com.google.android.material.imageview.ShapeableImageView;

public class OptionCallActivity extends AppCompatActivity {

    ImageView imgCallMic, imgEndCall, imgAnimation;

    Boolean checkCallVideo = false;
    ShapeableImageView shapeableImageView;
    TextView tvName;
    User user;
    MediaPlayer mediaPlayer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_option_call);

        mediaPlayer = MediaPlayer.create(this, R.raw.sound_receiver);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);



        initWidget();

        if (checkCallVideo) {

            imgCallMic.setImageResource(R.drawable.ic_callvideo);

            startAnimation(imgAnimation);

            imgCallMic.setOnClickListener(v -> {

                MainActivity.checkSoundAndVibarte();

                mediaPlayer.stop();

                Intent intent = new Intent(OptionCallActivity.this, CallVideoActivity.class);
                intent.putExtra("Object", user);
                startActivity(intent);
            });

        } else {
            mediaPlayer.stop();

            imgCallMic.setImageResource(R.drawable.ic_call);

            startAnimation(imgAnimation);

            imgCallMic.setOnClickListener(v -> {

                MainActivity.checkSoundAndVibarte();


                Intent intent = new Intent(OptionCallActivity.this, CallMicActivity.class);
                intent.putExtra("Object", user);
                startActivity(intent);
            });
        }

        imgEndCall.setOnClickListener(v -> {

            MainActivity.checkSoundAndVibarte();

            finish();

        });

    }

    private void initWidget() {
        checkCallVideo = (Boolean) getIntent().getExtras().get("checkCallVideo");
        user = getIntent().getExtras().getParcelable("Object");
//        imgCallVideo = this.<ImageView>findViewById(R.id.imgCallVideo);
        imgCallMic = this.<ImageView>findViewById(R.id.imgCallMicro);
        shapeableImageView = findViewById(R.id.circle);
        imgEndCall = findViewById(R.id.imgEndCall);
        imgAnimation = findViewById(R.id.imgAnimation);
        tvName = findViewById(R.id.tvNameCall);
        tvName.setText(user.getPersonName());

        Glide.with(this).load(user.getPersonAvt()).into(shapeableImageView);

    }

    public void startAnimation(View view) {
        ImageView imageView = (ImageView) view;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoom_anim);
        imageView.startAnimation(animation);
    }


}