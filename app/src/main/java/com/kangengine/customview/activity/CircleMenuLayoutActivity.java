package com.kangengine.customview.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cloudoc.share.yybpg.customview.R;
import com.kangengine.customview.widget.CircleMenuLayout;

/**
 * @author Vic
 * desc : 圆形菜单
 */
public class CircleMenuLayoutActivity extends AppCompatActivity {

    private CircleMenuLayout mCircleMenuLayout;

    private String[] mItemTexts = new String[] { "安全中心 ", "特色服务", "投资理财",
            "转账汇款", "我的账户", "信用卡" };
    private int[] mItemImgs = new int[] { R.mipmap.home_mbank_1_normal,
            R.mipmap.home_mbank_2_normal, R.mipmap.home_mbank_3_normal,
            R.mipmap.home_mbank_4_normal, R.mipmap.home_mbank_5_normal,
            R.mipmap.home_mbank_6_normal };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_circle_menu_layout);

        initView();
    }

    private void initView() {
        mCircleMenuLayout = findViewById(R.id.id_menu_layout);


        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs,mItemTexts);

        mCircleMenuLayout.setOnItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int position) {
                Toast.makeText(CircleMenuLayoutActivity.this,mItemTexts[position],Toast.LENGTH_SHORT).show();
            }

            @Override
            public void itemClick(View view) {

            }
        });
    }

    public void clickMenuLayout(View view) {
        startActivity(new Intent(this,NewMenuLayoutActivity.class));
    }
}
