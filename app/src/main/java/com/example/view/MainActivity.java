package com.example.view;

import static com.example.RoomDatabase.MessageDatabase.roomCallback;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.example.DAO.MessageDao;
import com.example.Fragment.CallFragment;
import com.example.Fragment.DirectFragment;
import com.example.Model.SearchViewModel;
import com.example.Model.UserWithChat;
import com.example.RoomDatabase.MessageDatabase;
import com.example.myappcall.R;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity   {

    CheckBox cbSetting, cbSound, cbVibrator;
    RadioGroup radioGroup;
    MessageDatabase messageDatabase;
    MessageDao messageDao;
    RadioButton rbNavigationCall, rbNavigationDirect;
    ConstraintLayout layoutCheckBoxSetting;
    NavigationBarView.OnItemSelectedListener onItemSelectedListener;

    ImageView imgPlayVideo;

    static SharedPreferences pref;

    // am thanh cua nut cai dat
    static MediaPlayer mediaSetting;

    static Vibrator vibrate;

    TextView tvCallWith;

    ConstraintLayout constraintLayoutToolbar;

    LinearLayout layoutSearchView;

    SearchViewModel searchViewModel ;

    EditText edSearchView ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        hideStatusBar();


        setContentView(R.layout.activity_main2);



        pref = MainActivity.this.getSharedPreferences("mode_setting", 0);

        vibrate = (Vibrator) getSystemService(MainActivity.this.VIBRATOR_SERVICE);

        messageDatabase = Room.databaseBuilder(this, MessageDatabase.class, "message-database").addCallback(roomCallback) // goi den static trong db
                .build();

        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);

        initWidget();


        // cai nay de an thanh status bar
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        messageDao = messageDatabase.messageDao();

        layoutCheckBoxSetting = this.<ConstraintLayout>findViewById(R.id.layoutCheckbox);

        cbSound.setVisibility(View.GONE);
        cbVibrator.setVisibility(View.GONE);
        replaceFragment(new CallFragment(messageDao));
        changeSizeToolBar(tvCallWith, layoutSearchView, true);


        handleEventClick();






    }

    private void changeSizeToolBar(TextView tvCall, LinearLayout layoutSearchView, boolean call) {

        if (call) {
            tvCall.setVisibility(View.VISIBLE);
            layoutSearchView.setVisibility(View.GONE);
            float dpValue = 90f;
            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
            ConstraintLayout myView = findViewById(R.id.constraintLayoutToolbar);
            myView.getLayoutParams().height = (int) pixels;
            myView.requestLayout();
        } else {
            tvCall.setVisibility(View.GONE);
            layoutSearchView.setVisibility(View.VISIBLE);
            float dpValue = 145f;
            float pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
            ConstraintLayout myView = findViewById(R.id.constraintLayoutToolbar);
            myView.getLayoutParams().height = (int) pixels;
            myView.requestLayout();
        }

    }


    private void handleEventClick() {



        rbNavigationCall.setOnCheckedChangeListener((buttonView, isChecked) -> {
            checkSoundAndVibarte();

        });

        radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            if (i == R.id.rbNavigationCall) {

                changeSizeToolBar(tvCallWith, layoutSearchView, true);

                changeColorNavigatonBottom(true);


                replaceFragment(new CallFragment(messageDao));

            } else {
                changeSizeToolBar(tvCallWith, layoutSearchView, false);

                changeColorNavigatonBottom(false);

                replaceFragment(new DirectFragment(messageDao));
            }
        });

        cbSetting.setOnClickListener(v -> {
            CheckBox checkBox1 = (CheckBox) v;

            checkSoundAndVibarte();

            if (checkBox1.isChecked()) {
                layoutCheckBoxSetting.setBackground(getDrawable(R.drawable.bg_checkbox));
                cbSound.setVisibility(View.VISIBLE);
                cbVibrator.setVisibility(View.VISIBLE);
            } else {
                layoutCheckBoxSetting.setBackground(getDrawable(R.drawable.bg_checkboxfalse));
                cbSound.setVisibility(View.GONE);
                cbVibrator.setVisibility(View.GONE);
            }
        });


        imgPlayVideo.setOnClickListener(v -> {

            MainActivity.checkSoundAndVibarte();

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

        cbVibrator.setOnClickListener(v -> {

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

    public static void checkSoundAndVibarte() {

        boolean soundClick = pref.getBoolean("sound", false);
        boolean vibarteClick = pref.getBoolean("ring", false);
        Log.d("soundClick", soundClick + " ");
        Log.d("vibarateClick", vibarteClick + " ");


        if (soundClick && vibarteClick) {

            // ca rung va am thanh

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrate.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                //deprecated in API 26
                vibrate.vibrate(100);
            }

            mediaSetting.start();

        } else if (vibarteClick) {
            // rung thoi
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrate.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
            } else {
                vibrate.vibrate(100);
            }
        } else if (soundClick) {

            mediaSetting.start();

        }

    }


    private void changeColorNavigatonBottom(boolean checkCall) {
        // xu ly khi ma calling dang duoc check
        if (checkCall) {
            RadioButton rbCall = findViewById(R.id.rbNavigationCall);
            Drawable drawableRBCall = rbCall.getCompoundDrawables()[1]; // Lấy drawableTop của RadioButton (index 1)
            if (drawableRBCall != null) {
                drawableRBCall.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            }
            rbCall.setCompoundDrawables(null, drawableRBCall, null, null); // Cập nhật drawableTop mới vào RadioButton

            RadioButton rbDir = findViewById(R.id.rbNavigationDirect);
            Drawable drawableRBDir = rbDir.getCompoundDrawables()[1]; // Lấy drawableTop của RadioButton (index 1)
            if (drawableRBDir != null) {
                drawableRBDir.setColorFilter(ContextCompat.getColor(this, R.color.colorIcNav), PorterDuff.Mode.SRC_IN);
            }
            rbDir.setCompoundDrawables(null, drawableRBDir, null, null);

            changeColorTitleNavigatonBottom(true);


        } else {

            // xu ly khi calling chua dc checkk
            RadioButton radioButton = findViewById(R.id.rbNavigationCall);
            Drawable drawable = radioButton.getCompoundDrawables()[1]; // Lấy drawableTop của RadioButton (index 1)
            if (drawable != null) {
                drawable.setColorFilter(ContextCompat.getColor(this, R.color.colorIcNav), PorterDuff.Mode.SRC_IN);
            }
            radioButton.setCompoundDrawables(null, drawable, null, null);

            RadioButton rbDir = findViewById(R.id.rbNavigationDirect);
            Drawable drawableRBDir = rbDir.getCompoundDrawables()[1]; // Lấy drawableTop của RadioButton (index 1)
            if (drawableRBDir != null) {
                drawableRBDir.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
            }

            rbDir.setCompoundDrawables(null, drawableRBDir, null, null); // Cập nhật drawableTop mới vào RadioButton
            changeColorTitleNavigatonBottom(false);

        }

    }

    private void changeColorTitleNavigatonBottom(boolean checkCall) {

        if (checkCall) {
            RadioButton rbTextCall = findViewById(R.id.rbNavigationCall);
            CharSequence currentTextCall = rbTextCall.getText();
            SpannableString spannableStringCall = new SpannableString(currentTextCall);
            int colorCall = ContextCompat.getColor(this, R.color.white); // Màu bạn muốn thiết lập
            ForegroundColorSpan colorSpanCall = new ForegroundColorSpan(colorCall);
            spannableStringCall.setSpan(colorSpanCall, 0, spannableStringCall.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            rbTextCall.setText(spannableStringCall);
            RadioButton rbTextMess = findViewById(R.id.rbNavigationDirect);
            CharSequence currentTextMess = rbTextMess.getText();
            SpannableString spannableStringMess = new SpannableString(currentTextMess);
            int colorMess = ContextCompat.getColor(this, R.color.colorIcNav); // Màu bạn muốn thiết lập
            ForegroundColorSpan colorSpanMess = new ForegroundColorSpan(colorMess);
            spannableStringMess.setSpan(colorSpanMess, 0, spannableStringMess.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            rbTextMess.setText(spannableStringMess);

        } else {
            RadioButton rbTextCall = findViewById(R.id.rbNavigationCall);
            CharSequence currentTextCall = rbTextCall.getText();
            SpannableString spannableStringCall = new SpannableString(currentTextCall);
            int colorCall = ContextCompat.getColor(this, R.color.colorIcNav); // Màu bạn muốn thiết lập
            ForegroundColorSpan colorSpanCall = new ForegroundColorSpan(colorCall);
            spannableStringCall.setSpan(colorSpanCall, 0, spannableStringCall.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            rbTextCall.setText(spannableStringCall);
            RadioButton rbTextMess = findViewById(R.id.rbNavigationDirect);
            CharSequence currentTextMess = rbTextMess.getText();
            SpannableString spannableStringMess = new SpannableString(currentTextMess);
            int colorMess = ContextCompat.getColor(this, R.color.white); // Màu bạn muốn thiết lập
            ForegroundColorSpan colorSpanMess = new ForegroundColorSpan(colorMess);
            spannableStringMess.setSpan(colorSpanMess, 0, spannableStringMess.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            rbTextMess.setText(spannableStringMess);
        }
    }


    void hideStatusBar() {
        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);

        } else {
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        }
    }

    private void initWidget() {


        boolean soundClick = pref.getBoolean("sound", false);
        boolean vibarteClick = pref.getBoolean("ring", false);



        cbSetting = findViewById(R.id.cbSetting);
        tvCallWith = findViewById(R.id.tvCallWith);
        cbSound = findViewById(R.id.cbSound);
        cbVibrator = findViewById(R.id.cbRing);
        imgPlayVideo = findViewById(R.id.imgPlayVideo);
        constraintLayoutToolbar = findViewById(R.id.constraintLayoutToolbar);
        layoutSearchView = findViewById(R.id.layoutSearchView);
        edSearchView = findViewById(R.id.edSearchView);

        mediaSetting = MediaPlayer.create(MainActivity.this, R.raw.sound_click);

        rbNavigationCall = this.<RadioButton>findViewById(R.id.rbNavigationCall);
        rbNavigationDirect = this.<RadioButton>findViewById(R.id.rbNavigationDirect);
        radioGroup = this.<RadioGroup>findViewById(R.id.rgGroupNavigation);

        edSearchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 searchViewModel.setKeyWordSearch(String.valueOf(s));
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });



//        BitmapDrawable bd = (BitmapDrawable) this.getResources().getDrawable(R.drawable.rotate_drawable_uncheck_call);
//
//        int iconWidth = bd.getBitmap().getWidth();
//        float density = getResources().getDisplayMetrics().density; // Lấy tỉ lệ mật độ của thiết bị
//        int leftPaddingDP = 16; // Giả sử bạn muốn đặt padding là 16dp
//        int leftPaddingPX = (int) (leftPaddingDP * density); // Chuyển đổi dp sang px
//
//        int leftPadding = (rbNavigationCall.getWidth() / 2) - (iconWidth / 2) - leftPaddingPX;
//
//        rbNavigationCall.setPadding(leftPadding, 0, 0, 0);


        if(soundClick)
        {
            cbSound.setChecked(true);
        }
        if (vibarteClick)
        {
            cbVibrator.setChecked(true);
        }

        //check permission and request
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 10);
        } else {
        }

        rbNavigationCall.setChecked(true);
        changeColorNavigatonBottom(true);
        rbNavigationDirect.setChecked(false);
        colorCheckbox(rbNavigationCall);
        colorCheckbox(rbNavigationDirect);

        RadioButton radioButton = findViewById(R.id.rbNavigationCall);
        Drawable drawable = radioButton.getCompoundDrawables()[1]; // Lấy drawableTop của RadioButton (index 1)
        if (drawable != null) {
            drawable.setColorFilter(ContextCompat.getColor(this, R.color.white), PorterDuff.Mode.SRC_IN);
        }
        radioButton.setCompoundDrawables(null, drawable, null, null); // Cập nhật drawableTop mới vào RadioButton

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