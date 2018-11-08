package com.kangengine.customview.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.kangengine.customview.BaseActivity;
import com.kangengine.customview.R;
import com.kangengine.customview.widget.SurfaceViewTemplate;

public class SurfaceViewActivity extends BaseActivity {

    private SurfaceViewTemplate surfaceViewTemplate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface_view);
        initView();
    }

    private void initView() {
        surfaceViewTemplate = findViewById(R.id.svt);
    }

    public  void  clearCanvas(View view){
        surfaceViewTemplate.clearCanvas();
    }
}
