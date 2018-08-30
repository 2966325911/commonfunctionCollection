package com.cloudoc.share.yybpg.customview.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cloudoc.share.yybpg.customview.R;
import com.cloudoc.share.yybpg.customview.widget.FlowLayout;

import java.util.Random;



public class FlowLayoutActivity extends BasefitsSystemWindowsActivity {

//    @BindView(R.id.fl)
    FlowLayout mFl;
    private String[] arr = {"京东", "淘宝", "阿里巴巴", "dnf", "神舟七号", "外卖小哥", "马云？？？"};

    @Override
    public int getLayoutId() {
        return R.layout.activity_flow_layout;
    }

    @Override
    public String setTitle() {
        return "flow";
    }

    @Override
    public void initView() {
        initViews();
    }

    @Override
    public void initData() {
        initDatas();
    }

    private void initViews() {

        mFl = findViewById(R.id.fl);
    }

    private void initDatas() {
        mToolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        for (int i = 0; i < 20; i++) {
            Random random = new Random();
            TextView view = getView(arr[random.nextInt(arr.length)]);
            mFl.addView(view);
        }
    }

    public TextView getView(String msg) {
        TextView tv = new TextView(this);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 10;
        lp.leftMargin = 10;
        lp.rightMargin = 10;
        lp.bottomMargin = 10;
        tv.setPadding(10, 10, 10, 10);
        tv.setLayoutParams(lp);
        tv.setTextSize(16);
        tv.setText(msg);
        tv.setBackgroundColor(getRandomColor());
        tv.setTextColor(getRandomColor());
        return tv;
    }

    public int getRandomColor() {
        Random random = new Random();
        String r = Integer.toHexString(random.nextInt(256)).toUpperCase();
        String g = Integer.toHexString(random.nextInt(256)).toUpperCase();
        String b = Integer.toHexString(random.nextInt(256)).toUpperCase();

        r = r.length() == 1 ? "0" + r : r;
        g = g.length() == 1 ? "0" + g : g;
        b = b.length() == 1 ? "0" + b : b;
        int color = Color.parseColor("#" + r + g + b);
        return color;
    }
}
