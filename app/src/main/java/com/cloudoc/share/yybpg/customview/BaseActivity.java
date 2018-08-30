package com.cloudoc.share.yybpg.customview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

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
}

