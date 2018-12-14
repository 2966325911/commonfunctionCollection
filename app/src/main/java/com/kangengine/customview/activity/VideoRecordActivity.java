package com.kangengine.customview.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kangengine.customview.BaseActivity;
import com.kangengine.customview.R;
import com.kangengine.customview.widget.RecordVideo;

/**
 * @author Vic
 * 视频录制及播放
 */
public class VideoRecordActivity extends BaseActivity implements RecordVideo.OnRecordFinishListener {


    private static final String TAG = VideoRecordActivity.class.getSimpleName();

    private RecordVideo mRecorderView;//视频录制控件
    private Button mShootBtn,mStopBtn;//视频开始录制按钮
    private boolean isFinish = true;
    private boolean success = false;//防止录制完成后出现多次跳转事件
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_record);
        mRecorderView = (RecordVideo) findViewById(R.id.movieRecorderView);
        mShootBtn = (Button) findViewById(R.id.shoot_button);
        mStopBtn = (Button) findViewById(R.id.stop_button);
        mRecorderView.setOnRecordFinishListener(this);
        mShootBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecorderView.startRecord();
            }
        });

        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mRecorderView!= null) {
                    mRecorderView.stop();
                }
            }
        });


        //这里也可以用长按去录视频，移开则录制成功
//        mShootBtn.setOnTouchListener(new OnTouchListener() {
//
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    mRecorderView.startRecord(new RecordVideo.OnRecordFinishListener() {
//
//                        @Override
//                        public void onRecordFinish() {
//                            handler.sendEmptyMessage(1);
//                        }
//                    });
//                } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (mRecorderView.getTimeCount() > 1)
//                        handler.sendEmptyMessage(1);
//                    else {
//                        if (mRecorderView.getRecordFile() != null)
//                            mRecorderView.getRecordFile().delete();
//                        mRecorderView.stop();
//                        Toast.makeText(MainActivity.this, "视频录制时间太短", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                return true;
//            }
//        });
    }


    @Override
    public void onResume() {
        super.onResume();
        isFinish = true;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            finishActivity();
        }
    };

    private void finishActivity() {
        if (isFinish) {
//            // 返回到播放页面
//            Intent intent = new Intent(this, PlayVideoActivity.class);
//            Log.d("TAG", mRecorderView.getRecordFile().getAbsolutePath());
//            intent.putExtra("path", mRecorderView.getRecordFile().getAbsolutePath());
//            startActivity(intent);
        }
        // isFinish = false;
        finish();
    }

    @Override
    public void onRecordFinish() {
        Log.d(TAG,"======录音完成====");
        handler.sendEmptyMessage(1);
    }



    /**
     * 录制完成回调
     *
     * @author liuyinjun
     *
     * @date 2015-2-9
     */
    public interface OnShootCompletionListener {
        public void OnShootSuccess(String path, int second);
        public void OnShootFailure();
    }

}
