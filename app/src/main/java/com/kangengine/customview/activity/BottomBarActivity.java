package com.kangengine.customview.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.kangengine.customview.BaseActivity;
import com.kangengine.customview.R;
import com.kangengine.customview.fragment.PlaceHolderFragment;

import java.lang.reflect.Field;

import q.rorbin.badgeview.QBadgeView;


/**
 * @author Vic
 * BottomNavigationView 底部tab显示
 */
public class BottomBarActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private QBadgeView qBadgeView0;
    private QBadgeView qBadgeView1;
    private QBadgeView qBadgeView2;
    private QBadgeView qBadgeView3;

    private View menuHome;
    private View menuFound;
    private View menuMessage;
    private View menuMe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_bar);

        initView();
    }

    private void initView() {
        bottomNavigationView = findViewById(R.id.bv_home_navigation);
        bottomNavigationView.setItemIconTintList(null);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        disableShiftMode(bottomNavigationView);
        showFragment("menu_home");

        menuHome = bottomNavigationView.findViewById(R.id.menu_home);
        menuFound = bottomNavigationView.findViewById(R.id.home_found);
        menuMessage = bottomNavigationView.findViewById(R.id.home_message);
        menuMe = bottomNavigationView.findViewById(R.id.home_me);

        showBadgeView();
    }

    /**
     * 显示角标
     */
    private void showBadgeView() {
        qBadgeView0 = new QBadgeView(getApplicationContext());
        qBadgeView1 = new QBadgeView(getApplicationContext());
        qBadgeView2 = new QBadgeView(getApplicationContext());
        qBadgeView3 = new QBadgeView(getApplicationContext());

        //如果只设置红点，则将sestBageNumer设置为-1，为0则不显示
        qBadgeView0.bindTarget(menuHome).setBadgeNumber(-1).setBadgePadding(4,true);
        qBadgeView1.bindTarget(menuFound).setBadgeNumber(8);
        qBadgeView2.bindTarget(menuMessage).setBadgeGravity(Gravity.END | Gravity.TOP).setBadgeNumber(66);
        qBadgeView3.bindTarget(menuMe).setBadgeNumber(100);


    }

    /**
     * 当BottomNavigationView中的元素大于3个是时候会移动,解决此问题，利用反射修改源码中的mShiftingMode
     *
     * @param view
     */

    private static void disableShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                item.setCheckable(item.getItemData().isChecked());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_home:
                showFragment("menu_home");
                return true;
            case R.id.home_found:
                showFragment("home_found");
                return true;
            case R.id.home_message:
                showFragment("home_message");
                return true;
            case R.id.home_me:
                showFragment("home_me");
                return true;
            default:
                return false;
        }
    }

    private void showFragment(String flagStr) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        PlaceHolderFragment fragment = new PlaceHolderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("flagStr",flagStr);
        fragment.setArguments(bundle);
        ft.replace(R.id.frame_layout,fragment).commit();

    }


    @Override
    protected void onDestroy() {

        bottomNavigationView.setOnNavigationItemSelectedListener(null);
        super.onDestroy();
    }
}
