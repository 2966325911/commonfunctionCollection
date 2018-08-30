package com.cloudoc.share.yybpg.customview.widget;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

import com.cloudoc.share.yybpg.customview.activity.CoordniatorLayoutActivity;
import com.cloudoc.share.yybpg.customview.R;

/**
 * @author : Vic
 * time   : 2018/08/10
 * desc   :
 */
public class MyTextViewBehavior extends CoordinatorLayout.Behavior {

    public MyTextViewBehavior(Context context, AttributeSet attrs) {
        super(context,attrs);
    }

    public MyTextViewBehavior(){

    }
    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        if(child.getId() == R.id.child) {
            if(dependency.getId() == R.id.dependency) {
                return true;
            }
        }
        return super.layoutDependsOn(parent, child, dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        int leftMargin = parent.getWidth() - dependency.getRight();
        int topMargin = parent.getHeight() - dependency.getBottom();
        CoordniatorLayoutActivity.setViewLocationInCoordinatorLayout(leftMargin,topMargin,child);
        return true;
    }

    @Override
    public void onDependentViewRemoved(CoordinatorLayout parent, View child, View dependency) {
        super.onDependentViewRemoved(parent, child, dependency);
    }
}
