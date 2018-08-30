package com.cloudoc.share.yybpg.customview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cloudoc.share.yybpg.customview.widget.CircleClockView;
import com.cloudoc.share.yybpg.customview.widget.SpannableFoldTextView;

import dalvik.system.DexClassLoader;

/**
 * @author Administrator
 * desc ：插件化需要解决的三个问题：
 * 1.插件代码如何加载
 * 2.插件中的生命周期如何管理
 * 3.插件资源和宿主资源冲突怎么办
 */
public class UiTestActivity extends AppCompatActivity {

    private Button btn;
    private TextView tvUiTest;

    private SpannableFoldTextView foldtextview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ui_test);
        setContentView(new CircleClockView(this));

//        btn = findViewById(R.id.button);
//        tvUiTest = findViewById(R.id.text);
//
//        foldtextview = findViewById(R.id.foldtextview);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tvUiTest.setVisibility(View.VISIBLE);
//                tvUiTest.setText("hello espresso");
//            }
//        });
//
//        foldtextview.setText(R.string.long_text);
    }
}
