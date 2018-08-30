package com.cloudoc.share.yybpg.lib_permission.setting;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * @author : Vic
 * time   : 2018/06/20
 * desc   :
 */
public class Normal implements ISetting {
    @Override
    public Intent getSettingIntent(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package",context.getPackageName(),null));
        return intent;
    }
}
