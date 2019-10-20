package com.architecture.to_do_mvvm.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class ScrollChildSwipeRefreshLayout extends SwipeRefreshLayout {

    private View mScrollUpChild;

    public ScrollChildSwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public ScrollChildSwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        if(mScrollUpChild != null){
            return mScrollUpChild.canScrollVertically( -1);
        }
        return super.canChildScrollUp();
    }

    public void setScrollUpChild(View scrollUpChild) {
        this.mScrollUpChild = scrollUpChild;
    }
}
