package com.kangengine.customview.util.httputil.cacahe;

import android.content.Context;

import okhttp3.Cache;

public class CacheProvide {
    Context mContext;

    public CacheProvide(Context context) {
        mContext = context;
    }

    public Cache provideCache() {
        return new Cache(mContext.getCacheDir(), 20*1024 * 1024);
    }
}
