package com.cloudoc.share.yybpg.customview.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cloudoc.share.yybpg.customview.R;
import com.cloudoc.share.yybpg.customview.bean.MenuItem;
import com.cloudoc.share.yybpg.customview.widget.CircleMenuLayout;

import java.util.List;

/**
 * @author : Vic
 * time   : 2018/07/31
 * desc   :
 */
public class CircleMenuAdapter extends BaseAdapter {

    private List<MenuItem> mMenuItems;

    public CircleMenuAdapter(List<MenuItem> menuItems) {
        this.mMenuItems = menuItems;
    }

    @Override
    public int getCount() {
        return mMenuItems.size();
    }

    @Override
    public MenuItem getItem(int position) {
        return mMenuItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.circle_menu_item,parent,false);
        initMenuItem(itemView,position);
        return itemView;
    }

    // 初始化菜单项
    private void initMenuItem(View itemView, int position) {
        // 获取数据项
        final MenuItem item = getItem(position);
        ImageView iv =itemView
                .findViewById(R.id.id_circle_menu_item_image);
        TextView tv =  itemView
                .findViewById(R.id.id_circle_menu_item_text);
        // 数据绑定
        iv.setImageResource(item.imageId);
        tv.setText(item.title);
    }
}
