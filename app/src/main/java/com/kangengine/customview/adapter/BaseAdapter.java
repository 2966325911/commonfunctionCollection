package com.kangengine.customview.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author : Vic
 * time   : 2018/06/19
 * desc   :
 */
public class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> implements View.OnClickListener {
    private List<T> mDatas;
    private int layoutId;
    private BaseAdapterListener mBaseAdapterListener;
    private OnItemClickListener<T> mOnItemClickListener;


    public BaseAdapter(List<T> datas,int layoutId,BaseAdapterListener baseAdapterListener) {
        this.mDatas = datas;
        this.layoutId = layoutId;
        this.mBaseAdapterListener = baseAdapterListener;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId,parent,false);
        view.setOnClickListener(this);
        return new BaseViewHolder(parent.getContext(),view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        if(mBaseAdapterListener != null) {
            mBaseAdapterListener.convert(holder,mDatas.get(position));
            holder.itemView.setTag(position);
        }
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    public void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    @Override
    public void onClick(View v) {
        if(mOnItemClickListener != null) {
            Integer tag = (Integer) v.getTag();
            mOnItemClickListener.onItemClick(tag,mDatas.get(tag));
        }
    }

    public interface  BaseAdapterListener<T>{
        void convert(BaseViewHolder holder,T t);
    }
}
