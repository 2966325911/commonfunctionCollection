package com.kangengine.customview.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.kangengine.customview.R;
import com.kangengine.customview.fragment.AFragment;
import com.kangengine.customview.fragment.BFragment;
import com.kangengine.customview.fragment.CFragment;
import com.next.easynavigation.view.EasyNavigationBar;
//import com.next.easynavigation.view.EasyNavigationBar;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vic
 * desc : 底部tab 带红点
 */
public class BottomTabActivity extends AppCompatActivity {

    private EasyNavigationBar easyNavigationBar;
    private String[] tabText = {"首页", "发现", "消息"};
    //未选中icon
    private int[] normalIcon = {R.mipmap.index, R.mipmap.find, R.mipmap.message};
    //选中时icon
    private int[] selectIcon = {R.mipmap.index1, R.mipmap.find1, R.mipmap.message1};
    List<Fragment> fragments = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_botton_tab);

        initView();
    }

    private void initView() {
        easyNavigationBar = findViewById(R.id.navigationBar);
        fragments.add(new AFragment());
        fragments.add(new BFragment());
        fragments.add(new CFragment());

        easyNavigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .addLayoutRule(EasyNavigationBar.RULE_BOTTOM)
                .addLayoutBottom(100)
                .onTabClickListener(new EasyNavigationBar.OnTabClickListener() {
                    @Override
                    public boolean onTabClickEvent(View view, int i) {
                        if(i==0) {
                            Toast.makeText(BottomTabActivity.this, "第一个Tab", Toast.LENGTH_SHORT).show();
                        } else if(i == 1) {
                            Toast.makeText(BottomTabActivity.this, "第二个Tab", Toast.LENGTH_SHORT).show();
                        } else if(i == 2) {
                            Toast.makeText(BottomTabActivity.this, "第三个Tab", Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                })
                .build();



    }

    public EasyNavigationBar getEasyNavigationBar(){
        return easyNavigationBar;
    }
}
