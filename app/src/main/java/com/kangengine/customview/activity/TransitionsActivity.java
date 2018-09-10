package com.kangengine.customview.activity;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.Window;

import com.kangengine.customview.R;

public class TransitionsActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTranslation();
        setContentView(R.layout.activity_transitions);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void setTranslation() {
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        int flag = getIntent().getExtras().getInt("flag");
        // 根据传值设置不同的动画
        switch (flag) {
            case 0:
                getWindow().setEnterTransition(new Explode());

                break;
            case 1:
                getWindow().setEnterTransition(new Slide());
                break;
            case 2:
                break;
            case 3:
                getWindow().setEnterTransition(new Fade());
                getWindow().setExitTransition(new Fade());
                break;
            default:
                break;
        }
    }
}
