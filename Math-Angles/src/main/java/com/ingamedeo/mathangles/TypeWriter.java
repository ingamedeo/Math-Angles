package com.ingamedeo.mathangles;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;

/**
 * Created by ingamedeo on 17/03/14.
 */
public class TypeWriter extends TextView {

    private CharSequence mText;
    private int mIndex;
    private long mDelay = 150; //Default 500ms delay
    private TextView textView;


    public TypeWriter(Context context, TextView textViewpass) {
        super(context);
        textView = textViewpass;
    }

    private Handler mHandler = new Handler();
    private Runnable characterAdder = new Runnable() {
        @Override
        public void run() {
            textView.setText(mText.subSequence(0, mIndex++));
            if(mIndex <= mText.length()) {
                mHandler.postDelayed(characterAdder, mDelay);
            }
        }
    };

    public void animateText(CharSequence text) {
        mText = text;
        mIndex = 0;

        textView.setText("");
        //textView.setTextColor(TextColor);
        mHandler.removeCallbacks(characterAdder);
        mHandler.postDelayed(characterAdder, mDelay);
    }

    public void setCharacterDelay(long millis) {
        mDelay = millis;
    }
}