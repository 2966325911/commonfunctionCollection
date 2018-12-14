package com.kangengine.customview.activity;

import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.VideoView;

import com.kangengine.customview.AppContextAppliction;
import com.kangengine.customview.BaseActivity;
import com.kangengine.customview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vic
 * desc : video缓存
 */
public class VideoCacheActivity extends BaseActivity {

    private VideoView videoView;
    private static final String URL = "http://smartdev.oss-cn-beijing.aliyuncs.com/video/av_video.mp4";
    private static final String URL2 = "https://raw.githubusercontent.com/danikula/AndroidVideoCache/master/files/orange1.mp4";
    private static final String URL3 = "https://raw.githubusercontent.com/danikula/AndroidVideoCache/master/files/orange2.mp4";
    private static final String URL4 = "https://raw.githubusercontent.com/danikula/AndroidVideoCache/master/files/orange3.mp4";

    private static final String AD_VIDEO = "AdVideo";
    private int index = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_cache);

        initView();
    }

    private void initView() {
        videoView = findViewById(R.id.videoView);

        String proxyUrl = AppContextAppliction.getProxy(this).getProxyUrl(URL);
        String proxyUrl2 = AppContextAppliction.getProxy(this).getProxyUrl(URL2);
        String proxyUrl3 = AppContextAppliction.getProxy(this).getProxyUrl(URL3);
        String proxyUrl4 = AppContextAppliction.getProxy(this).getProxyUrl(URL4);
//        String[] newProxyUrl4 = proxyUrl4.split("file://");
//        File file = new File(newProxyUrl4[1]);
//        if(file.exists()) {
//            file.delete();
//        }
        SharedPreferences sp = getSharedPreferences("AndroidVideo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        StringBuilder sb = new StringBuilder();
        sb.append(proxyUrl).append(",").append(proxyUrl2).append(",")
                .append(proxyUrl3).append(",").append(proxyUrl4);


        editor.putString(AD_VIDEO,sb.toString());
        editor.commit();

        String getUrl = sp.getString(AD_VIDEO,"");
        final String[] urls = getUrl.split(",");
        List<String> lists = new ArrayList<>();
        for(int i = 0; i < urls.length;i++){
            lists.add(urls[i]);
        }

        videoView.setVideoPath(urls[0]);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                index++;

                if(index == urls.length) {
                    index = 0;
                }
                videoView.setVideoPath(urls[index]);
                videoView.start();
            }
        });


    }
}
