package com.kangengine.retrofitlibrary2.util.httputil.utils;

import android.content.Context;


import com.kangengine.retrofitlibrary2.util.httputil.interceptor.CacheInterceptor;
import com.kangengine.retrofitlibrary2.util.httputil.interceptor.DownLoadInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class OkhttpProvidede {
    static OkHttpClient okHttpClient;

    public static OkHttpClient okHttpClient(final Context context, String BASE_URL) {
        if (okHttpClient == null) {
            synchronized (OkhttpProvidede.class) {
                if (okHttpClient == null) {
                    OkHttpClient client = new OkHttpClient.Builder()
                            .addInterceptor(new DownLoadInterceptor(BASE_URL))
                            .addNetworkInterceptor(new CacheInterceptor(context))
//                            .cache(new CacheProvide(context).provideCache())
                            .retryOnConnectionFailure(true)
                            .connectTimeout(5, TimeUnit.SECONDS)
                            .readTimeout(8, TimeUnit.SECONDS)
                            .writeTimeout(8, TimeUnit.SECONDS)
                            .build();
//                    if (BuildConfig.DEBUG) {//printf logs while  debug
//                        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
//                        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
//                        client = client.newBuilder().addInterceptor(logging).build();
//                    }
                    okHttpClient = client;
                }

            }

        }
        return okHttpClient;

    }
}
