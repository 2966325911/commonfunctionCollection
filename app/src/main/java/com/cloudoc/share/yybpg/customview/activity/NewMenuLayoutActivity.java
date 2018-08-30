package com.cloudoc.share.yybpg.customview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cloudoc.share.yybpg.customview.R;
import com.cloudoc.share.yybpg.customview.adapter.CircleMenuAdapter;
import com.cloudoc.share.yybpg.customview.bean.MenuItem;
import com.cloudoc.share.yybpg.customview.widget.NewCircleViewLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class NewMenuLayoutActivity extends AppCompatActivity {


    private String[] mItemTexts = new String[] { "安全中心 ", "特色服务", "投资理财",
            "转账汇款", "我的账户", "信用卡" };
    private int[] mItemImgs = new int[] { R.mipmap.home_mbank_1_normal,
            R.mipmap.home_mbank_2_normal, R.mipmap.home_mbank_3_normal,
            R.mipmap.home_mbank_4_normal, R.mipmap.home_mbank_5_normal,
            R.mipmap.home_mbank_6_normal };

    private List<MenuItem> menuItemList;

    private NewCircleViewLayout layout;
    private CircleMenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_menu_layout);

        initData();
        initView();
    }

    private void initData() {
        if(menuItemList == null) {
            menuItemList = new ArrayList<>();
        } else {
            menuItemList.clear();
        }
        for(int i = 0 ; i < mItemImgs.length;i++) {
            MenuItem menuItem = new MenuItem();
            menuItem.setImageId(mItemImgs[i]);
            menuItem.setTitle(mItemTexts[i]);
            menuItemList.add(menuItem);
        }
    }

    private void initView() {
        layout = findViewById(R.id.id_menu_layout);

        adapter = new CircleMenuAdapter(menuItemList);
        layout.setAdapter(adapter);
        layout.setOnItemClickListener(new NewCircleViewLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int position) {
                Toast.makeText(NewMenuLayoutActivity.this, menuItemList.get(position).title, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void itemClick(View view) {

            }
        });
    }
}
