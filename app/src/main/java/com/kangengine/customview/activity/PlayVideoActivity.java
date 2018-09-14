package com.kangengine.customview.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.Toast;

import com.kangengine.customview.R;
import com.kangengine.customview.videocompress.CompressListener;
import com.kangengine.customview.videocompress.Compressor;
import com.kangengine.customview.videocompress.InitListener;
import com.kangengine.customview.widget.MyVideoView;

import java.io.File;

public class PlayVideoActivity extends AppCompatActivity {

    private static final String TAG = PlayVideoActivity.class.getSimpleName();
    private MyVideoView videoView;
    private String path;
    private Button btnCompress;
    private Compressor mCompressor;
    private String currentOutputVideoPath = "/sdcard/videocompress/out.mp4";
    String cmd = "-y -i " + currentOutputVideoPath + " -strict -2 -vcodec libx264 -preset ultrafast " +
            "-crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 640x480 -aspect 16:9 " + currentOutputVideoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video);
        videoView = (MyVideoView) findViewById(R.id.video);
        Button playBtn = (Button) findViewById(R.id.play_video);
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });

        initLoadBinary();

    }

    public void clickCompress(View view) {
        execCommand(cmd);
    }

    private void initLoadBinary() {
        mCompressor = new Compressor(this);
        mCompressor.loadBinary(new InitListener() {
            @Override
            public void onLoadSuccess() {
                Log.d(TAG, "load library succeed");
            }

            @Override
            public void onLoadFail(String reason) {
                Log.d(TAG, "load library fail:" + reason);
            }
        });
    }

    private void play() {
        Uri uri = Uri.parse(path);
        videoView.setVideoURI(uri);
        MediaController mMediaController = new MediaController(this);
        videoView.setMediaController(mMediaController);
        videoView.start();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
    }

    private void execCommand(String cmd) {
        File mFile = new File(currentOutputVideoPath);
        if (mFile.exists()) {
            mFile.delete();
        }
        mCompressor.execCommand(cmd, new CompressListener() {
            @Override
            public void onExecSuccess(String message) {
                Log.i(TAG, "success " + message);
            }

            @Override
            public void onExecFail(String reason) {
                Log.i(TAG, "fail " + reason);
            }

            @Override
            public void onExecProgress(String message) {
                Log.i(TAG, "progress " + message);

            }
        });
    }

}
