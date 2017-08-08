package com.eason.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import com.eason.util.R;

public class AutoRepeatButton extends ImageButton {
    private static final int DEFAULT_INITIAL_DELAY = 500;
    private static final int DEFAULT_REPEAT_INTERVAL = 200;
    private long initialRepeatDelay = 500;
    private long repeatIntervalInMilliseconds = 200;
    private boolean wasLongClick = false;

    private Runnable repeatClickWhileButtonHeldRunnable = new Runnable() {
        @Override
        public void run() {
            wasLongClick = true;

            performClick();

            // Schedule the next repetitions of the click action, using a faster
            // repeat
            // interval than the initial repeat delay interval.
            postDelayed(repeatClickWhileButtonHeldRunnable,
                repeatIntervalInMilliseconds);
        }
    };

    public AutoRepeatButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public AutoRepeatButton(Context context) {
        super(context);
        init();
    }

    public AutoRepeatButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(attrs,
            R.styleable.AutoRepeatButton);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);

            switch (attr) {
                case R.styleable.AutoRepeatButton_initial_delay:
                    initialRepeatDelay = a.getInt(attr, DEFAULT_INITIAL_DELAY);
                    break;
                case R.styleable.AutoRepeatButton_repeat_interval:
                    repeatIntervalInMilliseconds = a.getInt(attr,
                        DEFAULT_REPEAT_INTERVAL);
                    break;
            }
        }
        init();
    }

    private void init() {
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    // Just to be sure that we removed all callbacks,
                    // which should have occurred in the ACTION_UP
                    removeCallbacks(repeatClickWhileButtonHeldRunnable);

                    // Schedule the start of repetitions after a one half second
                    // delay.
                    postDelayed(repeatClickWhileButtonHeldRunnable,
                        initialRepeatDelay);
                    setPressed(true);
                } else if (action == MotionEvent.ACTION_UP) {
                    if (!wasLongClick)
                        performClick();
                    // Cancel any repetition in progress.
                    removeCallbacks(repeatClickWhileButtonHeldRunnable);
                    setPressed(false);
                }
                wasLongClick = false;
                // Returning true here prevents performClick() from getting
                // called in the usual manner, which would be redundant, given
                // that we are already calling it above.
                return true;
            }
        });
    }

}
