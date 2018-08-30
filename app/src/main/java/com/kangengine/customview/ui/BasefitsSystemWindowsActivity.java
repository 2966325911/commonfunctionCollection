package com.kangengine.customview.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author : Vic
 * time   : 2018/06/19
 * desc   :
 */
public abstract class BasefitsSystemWindowsActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setHeight(mToolbar);
        }
    }

    public void setHeight(View view) {
        TypedArray ta = obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        float height = ta.getDimension(0,0);
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) view.getLayoutParams();

        double statusBarHeight = getStatusBarHeight(this);
        lp.height = (int) (statusBarHeight + height);
        view.setPadding(0, (int) statusBarHeight,0,0);

        mToolbar.setLayoutParams(lp);
    }



    private double getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height","dimen",
                "android");
        if(resourceId > 0) {
            result = context.getResources().getDimensionPixelOffset(resourceId);
        }
        return result;
    }
}
