package com.example.view;

import android.content.Intent;
import android.media.Image;
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

import com.example.Model.User;
import com.example.myappcall.R;
import com.google.android.material.imageview.ShapeableImageView;

public class OptionCallActivity extends AppCompatActivity {

    ImageView imgCallVideo, imgCallMic, imgEndCall;

    Boolean checkCallVideo = false;
    ShapeableImageView shapeableImageView;
    TextView tvName;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_option_call);

        initWidget();
        if (checkCallVideo) {
            imgCallMic.setVisibility(View.GONE);

            startAnimation(imgCallVideo);

            imgCallVideo.setOnClickListener(v -> {
                Intent intent = new Intent(OptionCallActivity.this, CallVideoActivity.class);
                intent.putExtra("Object", user);
                startActivity(intent);
            });
        } else {
            imgCallVideo.setVisibility(View.GONE);
            startAnimation(imgCallMic);

            imgCallMic.setOnClickListener(v -> {
                Intent intent = new Intent(OptionCallActivity.this, CallMicActivity.class);
                intent.putExtra("Object", user);
                startActivity(intent);
            });
        }

        imgEndCall.setOnClickListener(v -> {
            finish();
        });

    }

    private void initWidget() {
        checkCallVideo = (Boolean) getIntent().getExtras().get("checkCallVideo");
        user = getIntent().getExtras().getParcelable("Object");
        imgCallVideo = this.<ImageView>findViewById(R.id.imgCallVideo);
        imgCallMic = this.<ImageView>findViewById(R.id.imgCallMicro);
        shapeableImageView = findViewById(R.id.circle);
        imgEndCall = findViewById(R.id.imgEndCall);
        tvName = findViewById(R.id.tvNameCall);
        tvName.setText(user.getPersonName());
        String s = user.getPersonAvt();
        int resourceId = getResources().getIdentifier(s, "drawable", this.getPackageName());
        shapeableImageView.setImageResource(resourceId);
    }

    public void startAnimation(View view) {
        ImageView imageView = (ImageView) view;
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.zoom_anim);
        imageView.startAnimation(animation);
    }


}