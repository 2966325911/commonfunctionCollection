package com.kangengine.customview.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.kangengine.customview.R;
import com.kangengine.customview.activity.BottomTabActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class AFragment extends Fragment implements View.OnClickListener {


    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_a, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        btn1 = view.findViewById(R.id.a_btn1);
        btn2 = view.findViewById(R.id.a_btn2);
        btn3 = view.findViewById(R.id.a_btn3);
        btn4 = view.findViewById(R.id.a_btn4);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }


    public void showMsg() {
        ((BottomTabActivity) getActivity()).getEasyNavigationBar().setHintPoint(0, true);
    }

    public void showMsgTab() {
        ((BottomTabActivity) getActivity()).getEasyNavigationBar().setMsgPointCount(1, 50);
    }

    public void hideMsgRed() {
        ((BottomTabActivity) getActivity()).getEasyNavigationBar().clearHintPoint(0);
    }


    public void jumpOtherTab() {
        ((BottomTabActivity) getActivity()).getEasyNavigationBar().selectTab(1);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.a_btn1:
                showMsg();
                break;
            case R.id.a_btn2:
                showMsgTab();
                break;
            case R.id.a_btn3:
                hideMsgRed();
                break;
            case R.id.a_btn4:
                jumpOtherTab();
                break;
            default:
                break;

        }
    }
}
