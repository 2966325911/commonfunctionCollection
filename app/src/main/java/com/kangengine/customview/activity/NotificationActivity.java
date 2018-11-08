package com.kangengine.customview.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.kangengine.customview.BaseActivity;
import com.kangengine.customview.MainActivity;
import com.kangengine.customview.R;

public class NotificationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
    }

    public void sendNotification(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.baidu.om"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setSmallIcon(R.mipmap.icon_delete);
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(true);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.notification);
        contentView.setTextViewText(R.id.textView, "show me when collapsed");

        Notification notification = builder.build();
        notification.contentView = contentView;
        RemoteViews expandedView = new RemoteViews(getPackageName(), R.layout.notification_expand);
        notification.bigContentView = expandedView;
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(1, notification);
    }

    /**
     * 悬挂式Notification
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void sendXuanguaNotification(View view) {
        Notification.Builder builder = new Notification.Builder(this);
        // setCategory() category 用来确定Notification显示的位置，参数就是各种category的类型

        builder.setSmallIcon(R.mipmap.icon_delete)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setCategory(Notification.CATEGORY_MESSAGE)
                .setContentTitle("悬挂式Notification")
                .setContentText("这是悬挂式Notification");

        Intent push = new Intent();
        push.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        push.setClass(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, push, PendingIntent.FLAG_CANCEL_CURRENT);

        builder.setContentText("heads_up Notification on Android 5.0");
        // setFullScreenIntent(pendingIntent,false) 不设置此项将不会,第二个参数不会影响
        builder.setFullScreenIntent(pendingIntent, false);
        NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        nm.notify(2, builder.build());

    }
}
