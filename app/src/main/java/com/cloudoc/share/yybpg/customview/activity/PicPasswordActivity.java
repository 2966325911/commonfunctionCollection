package com.cloudoc.share.yybpg.customview.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.cloudoc.share.yybpg.customview.BaseActivity;
import com.cloudoc.share.yybpg.customview.R;
import com.cloudoc.share.yybpg.customview.widget.LockPatternView;


public class PicPasswordActivity extends BaseActivity implements LockPatternView.OnPasswordChanged {

    private LockPatternView lockView;
    private TextView tvPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_password);
        initView();
    }

    private void initView() {
        lockView = fvd(R.id.lockView);
        tvPassword = fvd(R.id.tvPassword);

        lockView.setOnPsswordChanged(this);
    }

    @Override
    public void onPasswordFailed(String errMsg) {
        tvPassword.setText(errMsg);
    }

    @Override
    public void onPasswordSuccess(String password) {
        tvPassword.setText(password);
    }

}