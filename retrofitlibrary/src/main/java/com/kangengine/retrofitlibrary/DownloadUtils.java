package com.kangengine.retrofitlibrary;

import android.support.annotation.NonNull;


import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author : Vic
 * time   : 2018/09/24
 * desc   :
 */
public class DownloadUtils {
    private static final String TAG = "DownloadUtils";

    private static final int DEFAULT_TIMEOUT = 15;

    private Retrofit retrofit;

    private JsDownloadListener listener;

    private String baseUrl;

    private String downloadUrl;

    public DownloadUtils(String baseUrl, JsDownloadListener listener) {

        this.baseUrl = baseUrl;
        this.listener = listener;

        JsDownloadInterceptor mInterceptor = new JsDownloadInterceptor(listener);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    /**
     * 开始下载
     *
     * @param url
     * @param filePath
     * @param subscriber
     */
    public void download(@NonNull String url, final String filePath, final String fileName, Subscriber subscriber) {
        listener.onStartDownload();

        // subscribeOn()改变调用之前代码的线程
        // observeOn()改变调用它之后代码的线程
        retrofit.create(DownloadService.class)
        .download(url)
        .subscribeOn(Schedulers.io())
        .unsubscribeOn(Schedulers.io())
        .map(new Func1<ResponseBody, InputStream>() {
            @Override
            public InputStream call(ResponseBody responseBody) {
                return responseBody.byteStream();
            }
        }).observeOn(Schedulers.computation())
        .doOnNext(new Action1<InputStream>() {
            @Override
            public void call(InputStream inputStream) {
                writeFile(inputStream,filePath,fileName);
            }
        }).observeOn(AndroidSchedulers.mainThread())
        .subscribe(subscriber);

    }

    /**
     * 将输入流写入文件
     *
     * @param inputStream
     * @param filePath
     */
    private void writeFile(InputStream inputStream, String filePath,String fileName) {

        File file = new File(filePath);

        if(!file.exists()){
            file.mkdirs();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file+"/" + fileName);

            byte[] b = new byte[1024];

            int len;
            while ((len = inputStream.read(b)) != -1) {
                fos.write(b,0,len);
            }
            inputStream.close();
            fos.close();

        } catch (FileNotFoundException e) {
            listener.onFail("FileNotFoundException");
        } catch (IOException e) {
            listener.onFail("IOException");
        }finally {
            close(inputStream);
            close(fos);
        }
    }

    private void close(Closeable closeable){
        if(closeable != null){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

