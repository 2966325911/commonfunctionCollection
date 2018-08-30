package com.cloudoc.share.yybpg.customview.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudoc.share.yybpg.customview.R;

import butterknife.ButterKnife;
import me.ele.uetool.UETool;

/**
 * @author : Vic
 * time   : 2018/06/15
 * desc   :
 */
public abstract class BaseActivity extends AppCompatActivity {
    public  Toolbar mToolbar;
    public TextView mTv_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        setStatusBarTextLight(false);
        initToolBar(setTitle());
        initView();
        initData();
        UETool.showUETMenu();

    }

    private  void setStatusBarTextLight(boolean isLight){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | (isLight ? View.SYSTEM_UI_FLAG_LAYOUT_STABLE : View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR));
        }
    }

    private  void initToolBar(String title){
        mToolbar = findViewById(R.id.toolbar);
        if(mToolbar != null) {
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setTitle("");
            mTv_title = findViewById(R.id.tv_title);
            if (!TextUtils.isEmpty(title) && mTv_title != null) {
                mTv_title.setText(title);
            }
            ImageView iv_back = findViewById(R.id.iv_back);
            if (iv_back != null) {
                iv_back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });
            }
        }
    }


    public BaseActivity getCurrentActivity(){
        return this;
    }

    public abstract int  getLayoutId();
    public abstract String setTitle();
    public abstract void initView();
    public abstract void initData();

    public void startActivity(Class<?> clazz,Bundle bundle) {
        Intent intent = new Intent(getCurrentActivity(),clazz);
        if(bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }
}
