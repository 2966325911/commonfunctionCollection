package com.kangengine.customview.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.kangengine.customview.BaseActivity;
import com.kangengine.customview.R;


/**
 * @author Vic
 * desc : 中文转拼音 retrofit 文件处理
 */
public class ZhongwenzhuanPinyinActivity extends BaseActivity {

    private TextView tvPinYin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhongwenzhuan_pinyin);
        String text = getPinYin("上海");
        tvPinYin = findViewById(R.id.tv_pinyin);
        tvPinYin.setText(text);

    }
}
