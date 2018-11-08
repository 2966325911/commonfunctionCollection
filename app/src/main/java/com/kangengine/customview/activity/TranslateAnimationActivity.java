package com.kangengine.customview.activity;

import android.animation.Animator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;

import com.kangengine.customview.BaseActivity;
import com.kangengine.customview.R;

/**
 * Activity的过渡动画
 */
public class TranslateAnimationActivity extends BaseActivity {

    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_animation);
        intent = new Intent(this,TransitionsActivity.class);

        initView();
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void explode(View view) {
        intent.putExtra("flag",0);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void slide(View view) {
        intent.putExtra("flag",1);
        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void fade(View view) {
        intent.putExtra("flag",2);
        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void share(View view) {
        View fab = findViewById(R.id.fab_button);
        intent.putExtra("flag",3);
        //设置单个共享元素
//        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this,
//                view,"share").toBundle());
        //创建多个转场动画
        startActivity(intent,ActivityOptions.makeSceneTransitionAnimation(this,
                Pair.create(view,"share"),
                Pair.create(fab,"fab")).toBundle());
    }


    private void initView() {
        final View oval = findViewById(R.id.oval);
        oval.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Animator animator = ViewAnimationUtils.createCircularReveal(
                        oval,oval.getWidth()/2,oval.getHeight()/2,
                        oval.getWidth(),0
                );

                animator.setInterpolator(new AccelerateDecelerateInterpolator());
                animator.setDuration(2000);
                animator.start();
            }
        });

        final View rect = findViewById(R.id.rect);
        rect.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                Animator animator = ViewAnimationUtils.createCircularReveal(
                        rect,0,0,0, (float) Math.hypot(rect.getWidth(),
                                rect.getHeight())
                );
                animator.setInterpolator(new AccelerateInterpolator());
                animator.setDuration(2000);
                animator.start();

            }
        });
    }

}
