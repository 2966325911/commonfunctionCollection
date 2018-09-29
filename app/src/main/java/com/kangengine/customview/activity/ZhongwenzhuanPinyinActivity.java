package com.kangengine.customview.activity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dyhdyh.manager.assets.AssetFile;
import com.dyhdyh.manager.assets.AssetsManager;
import com.kangengine.customview.BaseActivity;
import com.kangengine.customview.R;
import com.kangengine.customview.util.ToastUtil;
import com.kangengine.retrofitlibrary.DownloadUtils;
import com.kangengine.retrofitlibrary.JsDownloadListener;
import com.kangengine.retrofitlibrary2.util.httputil.Constant;
import com.kangengine.retrofitlibrary2.util.httputil.HttpBuilder;
import com.kangengine.retrofitlibrary2.util.httputil.HttpUtil;
import com.kangengine.retrofitlibrary2.util.httputil.interfaces.Error;
import com.kangengine.retrofitlibrary2.util.httputil.interfaces.Progress;
import com.kangengine.retrofitlibrary2.util.httputil.interfaces.Success;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.File;


/**
 * @author Vic
 * desc : 中文转拼音 retrofit 文件处理
 */
public class ZhongwenzhuanPinyinActivity extends BaseActivity implements JsDownloadListener {
    private static final String TAG = ZhongwenzhuanPinyinActivity.class.getSimpleName();

    private TextView tvPinYin;
    DownloadUtils downloadUtils;
    private String baseUrl = "http://yydys-prd-app.oss-cn-hangzhou.aliyuncs.com/";
    private String url = "http://yydys-prd-app.oss-cn-hangzhou.aliyuncs.com/xfw/android/1.0.0/xfw0925.apk";
    private String filePath = Environment.getExternalStorageDirectory() + "/customview/download/file/";

    private ProgressBar progressBar;
    private LinearLayout mLinearLayout;
    private LinearLayout mLinearLayoutClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zhongwenzhuan_pinyin);
        String text = getPinYin("上海");
        tvPinYin = findViewById(R.id.tv_pinyin);
        tvPinYin.setText(text);

        downloadUtils = new DownloadUtils(baseUrl,this);
        mLinearLayoutClick = findViewById(R.id.ll_click);
        progressBar = findViewById(R.id.progressBar);
        mLinearLayout = findViewById(R.id.ll_progress);
        mLinearLayout.setVisibility(View.GONE);

    }


    public void clickDownFile(View view) {
        String fileName = getFileName(url,"/");
        downloadUtils.download(url, filePath ,fileName, new rx.Subscriber() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Object o) {

            }
        });
    }

    @Override
    public void onStartDownload() {
        Log.d(TAG,"==onStartDownload===");
        mLinearLayoutClick.setVisibility(View.GONE);
        mLinearLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void onProgress(final int progress) {

        Log.d(TAG,"progress==== " + progress);
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                progressBar.setProgress(progress);
//            }
//        });
//        progressBar.setProgress(progress);
//        if(100 == progress){
//            mLinearLayout.setVisibility(View.GONE);
//        }
    }

    @Override
    public void onFinishDownload() {
        Log.d(TAG,"==onFinishedDownload===");

    }

    @Override
    public void onFail(String errorInfo) {

    }

    private String getFileName(String urlStr,String split) {
        String[] urls = urlStr.split(split);

        return urls[urls.length-1];
    }


    /**
     * 从assets copy文件到sd卡
     * @param view
     */
    public void clickCopyAssetsFile(View view){
        AssetFile assetFile = new AssetFile("test.jpg");
        File outputFile = new File(Environment.getExternalStorageDirectory(),assetFile.getName());
        if(AssetsManager.copyAssetFile(getAssets(),assetFile.getAssetPath(),outputFile)){
            ToastUtil.showToast(this,"copy success");
        } else {
            ToastUtil.showToast(this,"copy fail");
        }
    }

    /**
     * 下载文件
     * @param view
     */
    public void clickDownloadFile(View view) {
        String fileName = getFileName(url,"/");
        new HttpUtil.SingletonBuilder(getApplicationContext(), baseUrl)
                .build();

        new HttpBuilder(getApplicationContext(),url).isConnected().path(filePath)
                .fileName(fileName)
                .progress(new Progress() {
            @Override
            public void progress(int p) {
                Log.d(TAG,"===progresss===="  + p);
            }
        }).success(new Success() {
            @Override
            public void Success(String model) {
                Log.d(TAG,"===下载文件成功===");
                mLinearLayout.setVisibility(View.GONE);
                mLinearLayoutClick.setVisibility(View.VISIBLE);
                ToastUtil.showToast(ZhongwenzhuanPinyinActivity.this,"下载完成");
            }
        }).error(new Error() {
            @Override
            public void Error(String message, String type) {
                Log.d(TAG,"===下载文件失败===");
            }
        }).download();

    }
}
