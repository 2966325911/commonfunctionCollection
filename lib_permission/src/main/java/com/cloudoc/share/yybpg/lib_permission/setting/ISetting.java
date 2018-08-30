package com.cloudoc.share.yybpg.lib_permission.setting;

import android.content.Context;
import android.content.Intent;

/**
 * @author : Vic
 * time   : 2018/06/20
 * desc   :  如果取消授权则进入应用设置页面进行授权
 */
public interface ISetting {
    /**
     * 进入授权页面
     * @param context
     * @return
     */
    Intent getSettingIntent(Context context);
}
