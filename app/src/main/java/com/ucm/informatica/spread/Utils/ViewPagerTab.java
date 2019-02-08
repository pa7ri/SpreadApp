package com.ucm.informatica.spread.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;


//custom view pager without swiping
public class ViewPagerTab extends ViewPager {
    public ViewPagerTab(@NonNull Context context) {
        super(context);
    }
    public ViewPagerTab(@NonNull Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }

}
