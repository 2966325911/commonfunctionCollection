package com.kangengine.customview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cloudoc.share.yybpg.customview.R;
import com.kangengine.customview.fragment.SettingFragment;

/**
 * @author Administrator
 */
public class PreferenceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);
        getFragmentManager().beginTransaction().replace(R.id.ft_container,new SettingFragment())
                .commitAllowingStateLoss();
    }
}
