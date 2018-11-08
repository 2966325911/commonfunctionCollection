package com.kangengine.customview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kangengine.customview.BaseActivity;
import com.kangengine.customview.R;
import com.noober.background.BackgroundLibrary;

/**
 * @author Vic
 * desc : 提供view的背景图按钮支持
 */
public class BackgroundViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BackgroundLibrary.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_view);
    }
}
