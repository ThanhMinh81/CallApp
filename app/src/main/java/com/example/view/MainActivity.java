package com.example.view;

import static com.example.RoomDatabase.MessageDatabase.roomCallback;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;

import com.example.DAO.MessageDao;
import com.example.Fragment.CallFragment;
import com.example.Fragment.DirectFragment;
import com.example.RoomDatabase.MessageDatabase;
import com.example.myappcall.R;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {


    //    SharedPreferences pref =  MainActivity.this.getSharedPreferences("mode_setting", 0); // 0 - for private mode
//    SharedPreferences.Editor editor = pref.edit();

    CheckBox cbSetting, cbSound, cbRing;
    RadioGroup radioGroup;
    MessageDatabase messageDatabase;
    MessageDao messageDao;
    RadioButton rbNavigationCall, rbNavigationDirect;
    ConstraintLayout linearLayout;
    NavigationBarView.OnItemSelectedListener onItemSelectedListener;

    ImageView imgPlayVideo;

    SharedPreferences pref;


    // am thanh cua nut cai dat
      MediaPlayer mediaSetting;

    Vibrator vibrate ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        initWidget();

        pref = MainActivity.this.getSharedPreferences("mode_setting", 0);

         vibrate = (Vibrator) getSystemService(MainActivity.this.VIBRATOR_SERVICE);

        messageDatabase = Room.databaseBuilder(this,
                        MessageDatabase.class, "message-database")
                .addCallback(roomCallback) // goi den static trong db
                .build();

        messageDao = messageDatabase.messageDao();

        linearLayout = this.<ConstraintLayout>findViewById(R.id.layoutCheckbox);

        cbSound.setVisibility(View.GONE);
        cbRing.setVisibility(View.GONE);
        replaceFragment(new CallFragment(messageDao));

        // su ly su kien click

        handleEventClick();

    }


    private void handleEventClick() {
        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rbNavigationCall) {
                replaceFragment(new CallFragment(messageDao));
            } else {
                replaceFragment(new DirectFragment(messageDao));
            }
        });

        cbSetting.setOnClickListener(v -> {
            CheckBox checkBox1 = (CheckBox) v;

            boolean soundClick = pref.getBoolean("sound", false);
            if (soundClick) {

                Log.d("2345432", "23043902" + " ");

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrate.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                } else {
                    //deprecated in API 26
                    vibrate.vibrate(100);
                }
                
                mediaSetting.start();

            }

            if (checkBox1.isChecked()) {
                linearLayout.setBackground(getDrawable(R.drawable.bg_checkbox));
                cbSound.setVisibility(View.VISIBLE);
                cbRing.setVisibility(View.VISIBLE);
            } else {
                linearLayout.setBackground(getDrawable(R.drawable.bg_checkboxfalse));
                cbSound.setVisibility(View.GONE);
                cbRing.setVisibility(View.GONE);
            }
        });


        imgPlayVideo.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PlayVideoActivity.class);
            startActivity(intent);
        });

        cbSound.setOnClickListener(v -> {

            CheckBox cb = (CheckBox) v;

            if (cb.isChecked()) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("sound", true);
                editor.apply();
            } else {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("sound", false);
                editor.apply();
            }
        });

        cbRing.setOnClickListener(v -> {

            CheckBox cb = (CheckBox) v;

            if (cb.isChecked()) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("ring", true);
                editor.apply();
            } else {
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("ring", false);
                editor.apply();
            }

        });
    }

    private void initWidget() {

        cbSetting = findViewById(R.id.cbSetting);
        cbSound = findViewById(R.id.cbSound);
        cbRing = findViewById(R.id.cbRing);
        imgPlayVideo = findViewById(R.id.imgPlayVideo);

        mediaSetting = MediaPlayer.create(MainActivity.this,R.raw.sound_click);

        rbNavigationCall = this.<RadioButton>findViewById(R.id.rbNavigationCall);
        rbNavigationDirect = this.<RadioButton>findViewById(R.id.rbNavigationDirect);
        radioGroup = this.<RadioGroup>findViewById(R.id.rgGroupNavigation);

        //check permission and request
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    10);
        } else {
        }

        rbNavigationCall.setChecked(true);
        rbNavigationDirect.setChecked(false);
        colorCheckbox(rbNavigationCall);
        colorCheckbox(rbNavigationDirect);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 10) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
            }
        }
    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();

    }

    private void colorCheckbox(RadioButton checkBox) {
        if (checkBox.isChecked()) {

            checkBox.setTextColor(getResources().getColor(R.color.white));
            Drawable[] drawables = checkBox.getCompoundDrawablesRelative();
            Drawable drawableTop = drawables[1];
            drawableTop.setTint(getResources().getColor(R.color.white));

        } else {

            checkBox.setTextColor(getResources().getColor(R.color.colorForCbNotCheck));
            Drawable[] drawables = checkBox.getCompoundDrawablesRelative();
            Drawable drawableTop = drawables[1];
            drawableTop.setTint(getResources().getColor(R.color.colorForCbNotCheck));

        }
    }


}