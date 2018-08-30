package com.cloudoc.share.yybpg.customview.handler;

import android.os.Handler;
import android.os.Message;

/**
 * @author : Vic
 * time   : 2018/06/26
 * desc   :
 */
public class BusinessThreadHandler extends Handler {

    @Override
    public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
        removeMessages(msg.what);
        return super.sendMessageAtTime(msg, uptimeMillis);
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);

    }
}
