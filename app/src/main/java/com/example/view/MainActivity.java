package com.example.view;

import static com.example.RoomDatabase.MessageDatabase.roomCallback;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    CheckBox cbSetting, cbSound, cbRing;
    RadioGroup radioGroup;
    MessageDatabase messageDatabase;
    MessageDao messageDao;
    RadioButton rbNavigationCall, rbNavigationDirect;
    ConstraintLayout linearLayout;
    NavigationBarView.OnItemSelectedListener onItemSelectedListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        cbSetting = findViewById(R.id.cbSetting);
        cbSound = findViewById(R.id.cbSound);
        cbRing = findViewById(R.id.cbRing);

        rbNavigationCall = this.<RadioButton>findViewById(R.id.rbNavigationCall);
        rbNavigationDirect = this.<RadioButton>findViewById(R.id.rbNavigationDirect);
        radioGroup = this.<RadioGroup>findViewById(R.id.rgGroupNavigation);

        rbNavigationCall.setChecked(true);
        rbNavigationDirect.setChecked(false);
        colorCheckbox(rbNavigationCall);
        colorCheckbox(rbNavigationDirect);

        messageDatabase = Room.databaseBuilder(this,
                        MessageDatabase.class, "message-database")
                .addCallback(roomCallback) // Thêm callback vào đây
                .build();


        messageDao = messageDatabase.messageDao();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.rbNavigationCall) {
                    replaceFragment(new CallFragment(messageDao));
                    Log.d("2345432", rbNavigationCall.isChecked() + " ");
                } else {
                    replaceFragment(new DirectFragment(messageDao));
                }
            }
        });


        linearLayout = this.<ConstraintLayout>findViewById(R.id.layoutCheckbox);

        cbSound.setVisibility(View.GONE);
        cbRing.setVisibility(View.GONE);

        replaceFragment(new CallFragment(messageDao));

        cbSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox1 = (CheckBox) v;

                if (checkBox1.isChecked()) {
//                    bottomNavigationView.getMenu().getItem(R.id.nav_calling).setChecked(true);
                    linearLayout.setBackground(getDrawable(R.drawable.bg_checkbox));
                    cbSound.setVisibility(View.VISIBLE);
                    cbRing.setVisibility(View.VISIBLE);
                } else {
//                    bottomNavigationView.getMenu().getItem(R.id.nav_direct).setChecked(true);
                    linearLayout.setBackground(getDrawable(R.drawable.bg_checkboxfalse));
                    cbSound.setVisibility(View.GONE);
                    cbRing.setVisibility(View.GONE);
                }

            }
        });


    }


    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment);
        fragmentTransaction.commit();

    }

    public void onCheckboxClicked(View view) {
        // Xử lý sự kiện khi CheckBox được click

        CheckBox checkBox = (CheckBox) view;

        Log.d("Fsdfassa", checkBox.getText().toString());

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