package com.kangengine.customview.fragment;


import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.view.View;
import android.widget.Toast;

import com.cloudoc.share.yybpg.customview.R;

import java.io.File;
import java.io.FileDescriptor;


/**
 * @author vic
 */
public class SettingFragment extends PreferenceFragment {
    public static String PRE_ADVICE = "advice";
    private static String PRE_SWITCH = "switchskin";
    private Preference advice;
    private SwitchPreference preSwitch;
    private Preference multiPre;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pre_setting);

        initPreference();
    }

    private void initPreference() {
        advice = findPreference(PRE_ADVICE);
        preSwitch = (SwitchPreference) findPreference(PRE_SWITCH);
        multiPre = findPreference("multiPre");
        advice.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Toast.makeText(getActivity(), "意见建议被点击了", Toast.LENGTH_SHORT).show();
                return true;
            }
        });


        preSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                boolean boolValue = (boolean) newValue;
                switchDayOrNight();
                if(boolValue) {
                    Toast.makeText(getActivity(), "夜间模式开启", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "夜间模式关闭", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        multiPre.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String value =  newValue.toString();
                Toast.makeText(getActivity(), value, Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    public void switchDayOrNight() {
        int mode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (mode) {
            case Configuration.UI_MODE_NIGHT_NO:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
        getActivity().recreate();

    }
}
