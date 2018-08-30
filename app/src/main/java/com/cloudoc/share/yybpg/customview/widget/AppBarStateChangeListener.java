package com.cloudoc.share.yybpg.customview.widget;

import android.support.design.widget.AppBarLayout;

/**
 * @author : Vic
 * time   : 2018/06/19
 * desc   :
 */
public abstract class AppBarStateChangeListener implements AppBarLayout.OnOffsetChangedListener {
    private State mCurrentSate = State.IDLE;

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
        if(i == 0) {
            if(mCurrentSate != State.EXPANDED) {
                onStateChanged(appBarLayout,State.EXPANDED);
            }
        } else if(Math.abs(i) >= appBarLayout.getTotalScrollRange()) {
            if(mCurrentSate != State.COLLAPSED) {
                onStateChanged(appBarLayout,State.COLLAPSED);
            }
        } else {
            if(mCurrentSate != State.IDLE) {
                onStateChanged(appBarLayout,State.IDLE);
            }
            mCurrentSate = State.IDLE;
        }
        onStateChanged(i);
    }


    public abstract void onStateChanged(AppBarLayout appBarLayout,State state);
    public void onStateChanged(int i ) {

    }
    public enum  State {
        EXPANDED,
        COLLAPSED,
        IDLE
    }
}
