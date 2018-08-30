package com.cloudoc.share.yybpg.customview.service;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;

import com.cloudoc.share.yybpg.customview.R;

import java.io.IOException;

public class MediaPlayService extends Service {

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams layoutParams;
    private MediaPlayer mediaPlayer;

    private View displayView;

    public MediaPlayService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initWindowManager();

    }

    private void initWindowManager() {
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.format = PixelFormat.RGB_565;

        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.height = 500;
        layoutParams.width = 450;
        layoutParams.x = 300;
        layoutParams.y = 300;

        mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showMediaFloating();
        return super.onStartCommand(intent, flags, startId);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void showMediaFloating() {
        if(Settings.canDrawOverlays(this)) {
            displayView = LayoutInflater.from(getApplicationContext()).inflate(
                    R.layout.video_display,null
            );
            displayView.setOnTouchListener(new FloatingOnTouchListener(mWindowManager,layoutParams,displayView));
            SurfaceView surfaceView = displayView.findViewById(R.id.video_display_surfaceview);
            final SurfaceHolder surfaceHolder = surfaceView.getHolder();



            if(mediaPlayer != null) {
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                surfaceHolder.addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
                        mediaPlayer.setDisplay(surfaceHolder);
                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {

                    }
                });
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });

                try {
                    mediaPlayer.setDataSource(this, Uri.parse("https://raw.githubusercontent.com/dongzhong/ImageAndVideoStore/master/Bruno%20Mars%20-%20Treasure.mp4"));
                    mediaPlayer.prepareAsync();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mWindowManager.addView(displayView,layoutParams);
            }

        }
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayAndRemove();
    }

    private void stopPlayAndRemove() {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        if(mWindowManager != null && displayView != null) {
            mWindowManager.removeView(displayView);
        }
    }
}
