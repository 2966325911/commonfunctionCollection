package com.kangengine.customview.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.kangengine.customview.R;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author : Vic
 * time   : 2018/09/05
 * desc   :
 */
public class RecordVideo extends LinearLayout implements MediaRecorder.OnErrorListener {
    private static final String TAG = RecordVideo.class.getSimpleName();

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private ProgressBar mProgressBar;

    private MediaRecorder mMediaRecorder;
    private Camera mCamera;
    /**
     *     计时器
     */
    private Timer mTimer;
    /**
     * 录制完成回调接口
     */
    private OnRecordFinishListener mOnRecordFinishListener;

    /**
     *  视频分辨率宽度
     */
    private int mWidth;
    /**
     * 视频分辨率高度
     */
    private int mHeight;
    /**
     * 是否一开始就打开摄像头
     */
    private boolean isOpenCamera;
    /**
     * 一次拍摄最长时间
     */
    private int  mRecordMaxTime;
    /**
     *  时间计数
     */
    private int mTimeCount;
    /**
     * 存储文件
     */
    private File mRecordFile = null;
    private static final int DEFAULT_WIDTH = 640;
    private static final int DEFAULT_HEIGHT = 480;

    private static final int MAX_TIME = 60;

    public RecordVideo(Context context) {
        this(context, null);
    }

    public RecordVideo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("NewApi")
    public RecordVideo(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // 初始化各项组件
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RecordVideo, defStyle, 0);
        mWidth = a.getInteger(R.styleable.RecordVideo_video_width, DEFAULT_WIDTH);
        mHeight = a.getInteger(R.styleable.RecordVideo_video_height, DEFAULT_HEIGHT);

        // 默认打开
        isOpenCamera = a.getBoolean(R.styleable.RecordVideo_open_camera, true);
        mRecordMaxTime = a.getInteger(R.styleable.RecordVideo_timeLength, MAX_TIME);

        LayoutInflater.from(context).inflate(R.layout.record_video, this);
        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceview);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        // 设置进度条最大量
        mProgressBar.setMax(mRecordMaxTime);

        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.addCallback(new CustomCallBack());
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        a.recycle();
    }

    private class CustomCallBack implements SurfaceHolder.Callback {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (!isOpenCamera){
                return;
            }
            try {
                initCamera();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (!isOpenCamera){
                return;
            }
            freeCameraResource();
        }

    }

    /**
     * 初始化摄像头
     * @throws IOException
     */
    @SuppressLint("NewApi")
    private void initCamera() throws IOException {
        if (mCamera != null) {
            freeCameraResource();
        }
        try {
            mCamera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
            freeCameraResource();
        }
        if (mCamera == null){
            return;
        }


        // setCameraParams();
        mCamera.setDisplayOrientation(90);
        mCamera.setPreviewDisplay(mSurfaceHolder);
        try {
            Camera.Parameters parameters = mCamera.getParameters();
            //实现Camera自动对焦
            List<String> focusModes = parameters.getSupportedFocusModes();
            if (focusModes != null) {
                for (String mode : focusModes) {
                    mode.contains("continuous-video");
                    parameters.setFocusMode("continuous-video");
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        mCamera.startPreview();
        mCamera.unlock();
    }


    /**
     * 释放摄像头资源
     */
    private void freeCameraResource() {
        try{
            if (mCamera != null) {
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                mCamera.lock();
                mCamera.release();
                mCamera = null;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void createRecordDir() {
        File sampleDir = new File(Environment.getExternalStorageDirectory() + File.separator + "yybpg/video/");
        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        File vecordDir = sampleDir;
        // 创建文件
        try {
            //mp4格式
            mRecordFile = File.createTempFile("record", ".mp4", vecordDir);
            Log.i("TAG", mRecordFile.getAbsolutePath());
        } catch (IOException e) {
        }
    }

    /**
     * 初始化
     * @throws IOException
     */
    private void initRecord() throws IOException {
        mMediaRecorder = new MediaRecorder();
        mMediaRecorder.reset();
        if (mCamera != null){
            mMediaRecorder.setCamera(mCamera);
        }
        mMediaRecorder.setOnErrorListener(this);
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        try {
            //这里一定要将权限请求到 record camera ，否则可能奔溃，这里不做处理
            // 视频源
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            // 音频源
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        } catch (Exception e){
            Log.d(TAG,"=====可能权限未开启，请检查=====");
            e.printStackTrace();
        }

        // 视频输出格式
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        // 音频格式
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        // 设置分辨率： 640 * 480
        mMediaRecorder.setVideoSize(mWidth, mHeight);
        mMediaRecorder.setVideoFrameRate(30);
        // 设置帧频率，然后就清晰了
        mMediaRecorder.setVideoEncodingBitRate(1 * 1024 * 1024);
        // 输出旋转90度，保持竖屏录制
        mMediaRecorder.setOrientationHint(90);
        // 视频录制格式
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
        //最大持续时间60s
        mMediaRecorder.setMaxDuration(MAX_TIME* 1000);
        mMediaRecorder.setOutputFile(mRecordFile.getAbsolutePath());
        mMediaRecorder.prepare();
        try {
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnRecordFinishListener(OnRecordFinishListener onRecordFinishListener){
        this.mOnRecordFinishListener = onRecordFinishListener;
    }
//    /**
//     * 开始录制视频
//     * @param onRecordFinishListener
//     * 达到指定时间之后回调接口
//     */
//    public void startRecord(final OnRecordFinishListener onRecordFinishListener) {
//        this.mOnRecordFinishListener = onRecordFinishListener;
//        createRecordDir();
//        try {
//            // 如果未打开摄像头，则打开
//            if (!isOpenCamera){
//                initCamera();
//            }
//            initRecord();
//            // 时间计数器重新赋值
//            mTimeCount = 0;
//            mTimer = new Timer();
//            mTimer.schedule(new TimerTask() {
//
//                @Override
//                public void run() {
//                    // TODO Auto-generated method stub
//                    mTimeCount++;
//                    // 设置进度条
//                    mProgressBar.setProgress(mTimeCount);
//                    // 达到指定时间，停止拍摄
//                    if (mTimeCount == mRecordMaxTime) {
//                        stop();
//                    }
//                }
//            }, 0, 1000);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 开始录制视频
     * 达到指定时间之后回调接口
     */
    public void startRecord() {
        createRecordDir();
        try {
            // 如果未打开摄像头，则打开
            if (!isOpenCamera){
                initCamera();
            }
            initRecord();
            // 时间计数器重新赋值
            mTimeCount = 0;
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    mTimeCount++;
                    // 设置进度条
                    mProgressBar.setProgress(mTimeCount);
                    // 达到指定时间，停止拍摄
                    if (mTimeCount == mRecordMaxTime) {
                        stop();
                    }
                }
            }, 0, 1000);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止拍摄
     */
    public void stop() {
        if (mOnRecordFinishListener != null){
            mOnRecordFinishListener.onRecordFinish();
        }
        stopRecord();
        releaseRecord();
        freeCameraResource();
    }

    /**
     * 停止录制
     */
    public void stopRecord() {
        mProgressBar.setProgress(0);
        if (mTimer != null){
            mTimer.cancel();
        }

        if (mMediaRecorder != null) {
            // 设置后不会崩
            mMediaRecorder.setOnErrorListener(null);
            try {
                mMediaRecorder.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mMediaRecorder.setPreviewDisplay(null);
        }
    }

    /**
     * 释放资源
     */
    private void releaseRecord() {
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            try {
                mMediaRecorder.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mMediaRecorder = null;
    }

    public int getTimeCount() {
        return mTimeCount;
    }

    /**
     * @return the mVecordFile
     */
    public File getRecordFile() {
        return mRecordFile;
    }



    /**
     * 录制完成回调接口
     */
    public interface OnRecordFinishListener {
        public void onRecordFinish();
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        try {
            if (mr != null){
                mr.reset();
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

