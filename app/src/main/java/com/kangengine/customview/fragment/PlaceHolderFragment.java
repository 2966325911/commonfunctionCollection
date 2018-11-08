package com.kangengine.customview.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kangengine.customview.R;


/**
 * @author Vic
 * 占位Fragment
 */
public class PlaceHolderFragment extends Fragment {
    private String mFlagStr;
    private TextView tvFlagStr = null;
    public PlaceHolderFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        mFlagStr = bundle.getString("flagStr");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_holder,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        tvFlagStr = view.findViewById(R.id.flag_str);
        if(!TextUtils.isEmpty(mFlagStr)) {
            tvFlagStr.setText(mFlagStr);
        } else{
            tvFlagStr.setText("没有传递的值");
        }
    }
}
