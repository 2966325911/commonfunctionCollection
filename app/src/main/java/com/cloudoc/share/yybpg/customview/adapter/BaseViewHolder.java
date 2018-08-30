package com.cloudoc.share.yybpg.customview.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * @author : Vic
 * time   : 2018/06/19
 * desc   :
 */
public class BaseViewHolder extends RecyclerView.ViewHolder {
    public View itemView;
    private SparseArray<View> views;
    private Context context;
    public BaseViewHolder(Context context,View itemView){
        super(itemView);
        this.context = context;
        this.itemView = itemView;
        views = new SparseArray<>();
    }


    public static BaseViewHolder createViewHolder(Context context,View item) {
        return new BaseViewHolder(context,item);
    }

    public static BaseViewHolder createViewHolder(Context context, ViewGroup parent,int layoutId) {
        View view = LayoutInflater.from(context).inflate(layoutId,parent,false);
        return new BaseViewHolder(context,view);
    }

    public View getConvertView(){
        return itemView;
    }


    public <T extends View> T getView(int viewId) {
        View view = views.get(viewId);
        if(view == null) {
            view = itemView.findViewById(viewId);
            views.put(viewId,view);
        }
        return (T) view;
    }


    public View setTitle(int viewId,String title) {
        View view = getView(viewId);
        if(view instanceof TextView){
           ((TextView) view).setText(title);
        } else {
            throw new ClassCastException("you need give me TextView!");
        }

        return view;
    }


    public View setVisible(int viewId) {
        return setVisible(viewId,true);
    }

    public View setVisible(int viewId,boolean visible) {
        View view = getView(viewId);
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
        return view;
    }


    public void setBackgroundColor(int viewId,String color) {
        View view = getView(viewId);
        view.setBackgroundColor(Color.parseColor(color));
    }
}
