package com.kangengine.retrofitlibrary2.util.httputil.interceptor;

import android.content.Context;
import com.kangengine.retrofitlibrary2.util.SystemUtil;
import com.kangengine.retrofitlibrary2.util.httputil.HttpUtil;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheInterceptor implements Interceptor {
    private Context context;
    public CacheInterceptor(Context context){
        this.context = context;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        int maxAge = 60*60; // 有网络时 设置缓存超时时间1个小时
        int maxStale = 60 * 60 * 24 * 28; // 无网络时，设置超时为4周
        Request request = chain.request();
        if (SystemUtil.isNetWorkConnected(context)) {
            request= request.newBuilder()
//                    .addHeader("apikey", "2ffc3e48c7086e0e1faa003d781c0e69")
                    .cacheControl(CacheControl.FORCE_NETWORK)//有网络时只从网络获取
                    .build();
        }else {
            request= request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)//无网络时只从缓存中读取
                    .build();
        }
        Response response = chain.proceed(request);
        if (SystemUtil.isNetWorkConnected(context)) {
            String cache = request.header("Cache-Time");
            Response.Builder build=response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control");
            if (!HttpUtil.checkNULL(cache)) {
                build.header("Cache-Control", "max-age=" + cache)
                        .build();
            }else{
                build.header("Cache-Control", "public, max-age=" + maxAge)
                        .build();
            }
        } else {
            response= response.newBuilder()
                    .removeHeader("Pragma")
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
        }
        return response;
    }
}
