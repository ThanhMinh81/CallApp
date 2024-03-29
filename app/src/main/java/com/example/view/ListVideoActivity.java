package com.example.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Adapter.CallAdapter;
import com.example.Fragment.CallFragment;
import com.example.Interface.IClickCall;
import com.example.Model.User;
import com.example.myappcall.R;

import java.util.ArrayList;

public class ListVideoActivity extends AppCompatActivity {


    ImageView icBack ;
    RecyclerView rcvListVideo ;
    CallAdapter callAdapter;

    ArrayList<User> userArrayList;
    IClickCall iClickCall ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_video);

        icBack = findViewById(R.id.icBack);
        icBack.setOnClickListener(v -> {
            finish();
        });

        rcvListVideo = findViewById(R.id.rcvPlayVideo);

        iClickCall = user -> {

            MainActivity.checkSoundAndVibarte();


            Intent intent = new Intent();
            intent.putExtra("Object",user);
            setResult(Activity.RESULT_OK,intent);
            finish();
        };

        userArrayList = new ArrayList<>();
        userArrayList.addAll(CallFragment.userArrayList);
        callAdapter = new CallAdapter(userArrayList, this , iClickCall);
        rcvListVideo.setAdapter(callAdapter);
        rcvListVideo.setLayoutManager(new GridLayoutManager(this, 2));

    }


}
