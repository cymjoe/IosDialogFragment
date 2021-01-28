package com.cymjoe.testfiosdialogfragmentdemo.iosdialogfragment;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class CustomRecyclerView extends RecyclerView {


    public CustomRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
                if (onTopScrollYListener != null) {
                    onTopScrollYListener.isRvTop(!canScrollVertically(-1));
                }
                if (!canScrollVertically(-1)) {
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
