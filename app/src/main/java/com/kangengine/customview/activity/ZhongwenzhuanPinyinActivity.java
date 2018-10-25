package com.kangengine.customview.activity;

import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dyhdyh.manager.assets.AssetFile;
import com.dyhdyh.manager.assets.AssetsManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kangengine.customview.BaseActivity;
import com.kangengine.customview.R;
import com.kangengine.customview.util.ToastUtil;
import com.kangengine.retrofitlibrary2.util.httputil.BaseModel;
import com.kangengine.retrofitlibrary2.util.httputil.HttpBuilder;
import com.kangengine.retrofitlibrary2.util.httputil.HttpUtil;
import com.kangengine.retrofitlibrary2.util.httputil.interfaces.Error;
import com.kangengine.retrofitlibrary2.util.httputil.interfaces.Progress;
import com.kangengine.retrofitlibrary2.util.httputil.interfaces.Success;
import java.io.File;
import java.util.Map;


/**
 * @author Vic
 * desc : 中文转拼音 retrofit 文件处理
 */
public class ZhongwenzhuanPinyinActivity extends BaseActivity {
    private static final String TAG = ZhongwenzhuanPinyinActivity.class.getSimpleName();

    private TextView tvPinYin;
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
        mLinearLayoutClick = findViewById(R.id.ll_click);
        progressBar = findViewById(R.id.progressBar);
        mLinearLayout = findViewById(R.id.ll_progress);
        mLinearLayout.setVisibility(View.GONE);

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


  //--------------------------------以下下载上传均采用com.kangengine.retrofitlibrary2此库的方式-----------------------------------------------------//
    /**
     * 下载文件 参考其他的封装库，次下载相对比较完善，能正确下载文件，采用com.kangengine.retrofitlibrary2
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

    /**
     * post请求数据
     */
    private void postParams(){
        new HttpUtil.SingletonBuilder(getApplicationContext(), "base_url一般是前缀以 / 结尾")
                .build();
        new HttpBuilder(getApplicationContext(),"访问的具体地址")
                .isConnected()
                .params("post的key","post的value")
                .success(new Success() {
                    @Override
                    public void Success(String Strings) {
                        BaseModel model = new BaseModel(Strings);
                        if(model != null) {
                            if(model.success) {
                                if(!TextUtils.isEmpty(model.data)) {
                                     //请求成功返回的数据 如果是的{key1,：value1，key2 ：value2}这种形式，用map去解，否则用构造对象去解
                                    Map<String,String> keyValueString = new Gson().fromJson(model.data,
                                            new TypeToken<Map<String,String>>(){}.getType());

                                    String value = keyValueString.get("key");
                                }
                            }
                        }
                    }
                } )
                .post();
    }


    /**
     * put上传单个图片，例如头像
     * @param file_path
     */
    private void sendFile(String file_path){
        new HttpUtil.SingletonBuilder(getApplicationContext(), "baseurl")
                .build();
        new HttpBuilder(getApplicationContext(), "具体请求地址")
                .isConnected()
                .success(new Success() {
                    @Override
                    public void Success(String Strings) {
                        Log.d(TAG,Strings);
                    }
                } )
                .error(new Error() {
                    @Override
                    public void Error(String message, String type) {

                    }
                })
                .put(file_path);
    }

    /**
     * get获取数据
     */
    private void getData(){
        new HttpUtil.SingletonBuilder(getApplicationContext(), "baseurl")
                .build();

        new HttpBuilder(getApplicationContext(), "具体的请求地址")
                .setShow_progressbar(false)
                .success(new Success() {
                    @Override
                    public void Success(String Strings) {
                        Log.d(TAG,Strings);

                        BaseModel model = new BaseModel(Strings);
                        if(model != null) {
                            if(model.success) {
                                //TODO 请求成功处理逻辑
                            } else {

                            }
                        }
                    }
                } )
                .get();
    }



}
