package com.kangengine.customview.activity;

import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.TypedValue;
import android.widget.TextView;

import com.kangengine.customview.R;

import org.w3c.dom.Text;

/**
 * @author Administrator
 * desc :固定大小的宽度，当文字过多时，文字缩小，文字少，则放大，设置最大文字的size 和最小文字的size
 */
public class AutoChangeTextSizeActivity extends AppCompatActivity {

    private AppCompatTextView tvText;
    private AppCompatTextView tvText1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_change_text_size);
        tvText = findViewById(R.id.tv_text);
        TextViewCompat.setAutoSizeTextTypeWithDefaults(
                tvText, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                tvText, 8, 25, 1, TypedValue.COMPLEX_UNIT_SP);
        tvText.setText("测试自动改变文本大小的TextView");

        tvText1 = findViewById(R.id.tv_text1);

        TextViewCompat.setAutoSizeTextTypeWithDefaults(
                tvText1, TextViewCompat.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        TextViewCompat.setAutoSizeTextTypeUniformWithConfiguration(
                tvText1, 8, 25, 1, TypedValue.COMPLEX_UNIT_SP);
        tvText1.setText("感冒难受");

    }
}
