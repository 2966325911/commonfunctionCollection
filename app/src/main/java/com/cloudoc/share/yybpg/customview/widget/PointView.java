package com.cloudoc.share.yybpg.customview.widget;

import android.graphics.Point;

/**
 * @author : Vic
 * time   : 2018/08/29
 * desc   :
 */
public class PointView extends Point
{
    /**
     * 用于转化密码的下标
     */

    public int index;

    public PointView(int x,int y) {
        super(x,y);
    }

    public int getIndex(){
        return index;
    }

    public void setIndex(int index){
        this.index = index;
    }
}
