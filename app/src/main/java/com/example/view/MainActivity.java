package com.example.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import com.example.Fragment.CallFragment;
import com.example.Fragment.DirectFragment;
import com.example.myappcall.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    CheckBox cbSetting, cbSound, cbRing;

    CheckBox cbNavigationCall, cbNavigationDirect;

    ConstraintLayout linearLayout;
//    BottomNavigationView bottomNavigationView;

    NavigationBarView.OnItemSelectedListener onItemSelectedListener;

//    BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        cbSetting = findViewById(R.id.cbSetting);
        cbSound = findViewById(R.id.cbSound);
        cbRing = findViewById(R.id.cbRing);

        cbNavigationCall = this.<CheckBox>findViewById(R.id.cbNavigationCall);
        cbNavigationDirect = this.<CheckBox>findViewById(R.id.cbNavigationDirect);

        cbNavigationCall.setChecked(true);


        linearLayout = this.<ConstraintLayout>findViewById(R.id.layoutCheckbox);

        cbSound.setVisibility(View.GONE);
        cbRing.setVisibility(View.GONE);

//        bottomNavigationView = this.<BottomNavigationView>findViewById(R.id.bottomNavigationView);

//        bottomNavigationView.setItemIconTintList(null);

        replaceFragment(new CallFragment());
        cbNavigationCall.setChecked(true);
        colorCheckbox(cbNavigationCall);
        cbNavigationDirect.setChecked(false);
        colorCheckbox(cbNavigationDirect);


//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                int id = menuItem.getItemId();
//
//                if (id == R.id.nav_calling) {
//                    replaceFragment(new CallFragment());
//                } else if (id == R.id.nav_direct) {
//                    replaceFragment(new DirectFragment());
//                }
//                return true;
//            }
//        });

//
//        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
//          mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//
//                int id = item.getItemId();
//
//                if(id == R.id.nav_calling)
//                {
//                    bottomNavigationView.getMenu().getItem(R.id.nav_calling).setChecked(true);
//                    replaceFragment(new CallFragment());
//                }else if(id == R.id.nav_direct) {
//                    bottomNavigationView.getMenu().getItem(R.id.nav_direct).setChecked(true);
//                    replaceFragment(new DirectFragment());
//                }
//                return false;
//            }
//        };


        // check : textView  , Draable : white
        // uncheck : itemBackgroundIcon

        cbNavigationCall.setOnClickListener(v -> {
            CheckBox cbCall = (CheckBox) v;
            colorCheckbox(cbCall);
            if(cbCall.isChecked())
            {
                cbNavigationDirect.setEnabled(true);
                cbNavigationDirect.setEnabled(true);
                cbNavigationDirect.setChecked(false);
                colorCheckbox(cbNavigationDirect);
            }else {
                cbNavigationCall.setEnabled(false);
//                cbNavigationDirect.setChecked(true);
//                colorCheckbox(cbNavigationDirect);
            }
        });

        cbNavigationDirect.setOnClickListener(v -> {
            CheckBox cbDirect = (CheckBox) v;
            colorCheckbox(cbDirect);
            Log.d("Rưeqrqwer",cbDirect.isChecked() + " ");
            if(cbDirect.isChecked())
            {
                cbNavigationCall.setEnabled(true);

                cbNavigationCall.setChecked(false);
                colorCheckbox(cbNavigationCall);
            }else {
                cbNavigationDirect.setEnabled(false);
//                cbNavigationCall.setChecked(true);
//                colorCheckbox(cbNavigationCall);

            }


        });

//        cbNavigationCall.setOnCheckedChangeListener((buttonView, isChecked) -> {
//
//            CheckBox checkBox = (CheckBox) buttonView;
//
//             if(checkBox.getId() == R.id.cbNavigationCall)
//             {
//                 if(checkBox.isChecked())
//                 {
//                     cbNavigationDirect.setChecked(false);
//                     colorCheckbox(cbNavigationDirect);
//                 }else {
//                     checkBox.setChecked(true);
//                     colorCheckbox(checkBox);
//                 }
//             }else if(checkBox.getId() ==  R.id.cbNavigationDirect) {
//                 if(checkBox.isChecked())
//                 {
//                     cbNavigationCall.setChecked(false);
//                     colorCheckbox(cbNavigationCall);
//                 }else {
//                     checkBox.setChecked(true);
//                     colorCheckbox(checkBox);
//                 }
//             }
//
//
//        });


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


    private void colorCheckbox(CheckBox checkBox) {
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