package com.cymjoe.testfiosdialogfragmentdemo.iosdialogfragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

public class CustomScrollView extends NestedScrollView {
    private boolean isScrolledToTop = true;// 初始化的时候设置一下值
    private boolean isScrolledToBottom = false;

    public CustomScrollView(@NonNull Context context) {
        super(context);
    }

    public CustomScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (scrollY == 0) {
            isScrolledToTop = clampedY;
            isScrolledToBottom = false;
        } else {
            isScrolledToTop = false;
            isScrolledToBottom = clampedY;
        }
        if (onTopScrollYListener != null) {
            onTopScrollYListener.isTop(isScrolledToTop);
        }
    }


    OnTopScrollYListener onTopScrollYListener;

    public void setOnTopScrollYListener(OnTopScrollYListener onTopScrollYListener) {
        this.onTopScrollYListener = onTopScrollYListener;
    }

    float down;
    float move;
    float noTopMove;//未滑动到顶部的移动距离

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                down = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isScrolledToTop) {
                    move = ev.getRawY() - down - noTopMove;

                    if (onTopScrollYListener != null) {
                        onTopScrollYListener.onScrollY(move);
                    }
                    if (move > 0) {
                        return true;
                    }
                } else {
                    noTopMove = ev.getRawY() - down;
                }
                break;
            case MotionEvent.ACTION_UP:
                noTopMove = 0;
                if (onTopScrollYListener != null) {
                    onTopScrollYListener.onUp();
                }
                break;

        }
        return super.dispatchTouchEvent(ev);
    }

}
