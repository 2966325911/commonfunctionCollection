package com.kangengine.customview;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import com.github.promeg.pinyinhelper.Pinyin;
import com.kangengine.customview.util.SharedPreferencesUtils;

import java.util.Locale;

/**
 * @author : Vic
 * time   : 2018/08/30
 * desc   :
 */
public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public void toNextActivity(Class<?> clazz,Bundle bundle){
        Intent intent = new Intent(this,clazz);
        if(bundle != null) {
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }



    public <T extends View> T fvd(int viewId){
        return findViewById(viewId);
    }


    private static final String LANGUAGE = "language";
    public void setLanguageZh(boolean flag){
        SharedPreferencesUtils.setParam(this,LANGUAGE,flag);
    }

    public boolean getLanguageZh(){
        return (boolean) SharedPreferencesUtils.getParam(this,LANGUAGE,true);
    }

    /**
     * 语言切换
     * @param lauType
     */
    public void set(String lauType) {
        Locale myLocale = new Locale(lauType);
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration conf = resources.getConfiguration();
        conf.locale = myLocale;
        resources.updateConfiguration(conf, dm);
    }


    /**3
     * 将汉字转换为拼音以小写输出
     * @param content
     * @return
     */
    public static String getPinYin(String content) {
        String msg = Pinyin.toPinyin(content,"");
        return msg.toLowerCase();
    }
}

