package com.example.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

public class Test extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Typewriter writer = new Typewriter(this);
        setContentView(writer);



                   writer.setCharacterDelay(100);
                   writer.animateText("Sample String");



    }
}