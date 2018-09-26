package com.kangengine.retrofitlibrary;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author : Vic
 * time   : 2018/09/24
 * desc   :带进度的下载的拦截器
 */
public class JsDownloadInterceptor implements Interceptor {

    private JsDownloadListener listener;
    public  JsDownloadInterceptor(JsDownloadListener listener) {
        this.listener = listener;
    }


    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());

        return response.newBuilder().body(new JsResponseBody(response.body(),listener)).build();
    }
}
