package com.kangengine.customview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kangengine.customview.BaseActivity;
import com.kangengine.customview.R;
import com.kangengine.customview.fragment.SettingFragment;

/**
 * @author Administrator
 */
public class PreferenceActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        getFragmentManager().beginTransaction().replace(R.id.ft_container,new SettingFragment())
                .commitAllowingStateLoss();
    }
}
