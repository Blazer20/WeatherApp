package com.example.weatherapp.customViews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.motion.widget.MotionLayout;

public class TouchableMotionLayout extends MotionLayout {

    public interface OnMotionEventListener {
        void onMotionEvent(MotionEvent event);
    }

    private OnMotionEventListener listener;

    public TouchableMotionLayout(@NonNull Context context) {
        super(context);
    }

    public TouchableMotionLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TouchableMotionLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        listener.onMotionEvent(event);
        return super.onTouchEvent(event);
    }

    public void setOnMotionEventListener(OnMotionEventListener listener) {
        this.listener = listener;
    }

}
