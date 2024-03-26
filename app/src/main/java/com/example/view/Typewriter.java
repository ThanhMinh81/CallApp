package com.example.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.TextView;

public class Typewriter extends androidx.appcompat.widget.AppCompatTextView {

    private CharSequence mText;
    private int mIndex;
    private long mDelay = 200; //Default 500ms delay
    private boolean mIsRunning = false;

    public Typewriter(Context context) {
        super(context);
    }

    public Typewriter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            setText(mText.subSequence(0, mIndex++));
            if(mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            } else {
                // Nếu hiển thị hoàn thành, thiết lập lại chỉ số và chạy lại từ đầu
                mIndex = 0;
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    public void animateText(CharSequence text) {
        if (!mIsRunning) {
            mText = text;
            mIndex = 0;
            mIsRunning = true;
            mHandler.post(characterAdder);
        }
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }

    // Hàm để dừng hiển thị văn bản
    public void stopAnimation() {
        mHandler.removeCallbacks(characterAdder);
        mIsRunning = false;
    }
}
