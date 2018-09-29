package com.kangengine.retrofitlibrary2.util.httputil;

import android.content.Context;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.kangengine.retrofitlibrary2.util.httputil.interfaces.Error;
import com.kangengine.retrofitlibrary2.util.httputil.interfaces.Progress;
import com.kangengine.retrofitlibrary2.util.httputil.interfaces.Success;
import com.kangengine.retrofitlibrary2.util.httputil.utils.NetUtils;
import com.kangengine.retrofitlibrary2.util.httputil.utils.WriteFileUtil;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.kangengine.retrofitlibrary2.util.httputil.HttpUtil.checkHeaders;
import static com.kangengine.retrofitlibrary2.util.httputil.HttpUtil.checkParams;
import static com.kangengine.retrofitlibrary2.util.httputil.HttpUtil.putCall;

public class HttpBuilder {
    Map<String, Object> params = new HashMap<>();
    Map<String, String> headers = new HashMap<>();
    String url;
    String path;
    String fileName;
    Error mErrorCallBack;
    Success mSuccessCallBack;
    Progress mProgressListener;
    Object tag;
    Context mContext;
    boolean checkNetConnected = false;
    private Context context;
    private boolean show_progressbar = true;
    private static final String TAG = HttpBuilder.class.getSimpleName();
    public HttpBuilder(Context context, @NonNull String url) {
        this.context = context;
        this.setParams(url);
    }

    /**
     * 是否允许缓存，传入时间如：1*3600 代表一小时缓存时效
     *
     * @param time 缓存时间 单位：秒
     * @author gengqiquan
     * @date 2017/3/25 下午3:27
     */
    public HttpBuilder cacheTime(int time) {
        header("Cache-Time", time + "");
        return this;
    }

    /**
     * 添加头部信息
     * @return
     */
    public HttpBuilder addHeader() {
        header("Content-Type", "application/json;charset=utf-8");
        header("Accept", "application/json;charset=utf-8");
        header("platform", "android");

        return this;
    }


    public HttpBuilder path(@NonNull String path) {
        this.path = path;
        return this;
    }

    public HttpBuilder fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public HttpBuilder tag(@NonNull Object tag) {
        this.tag = tag;
        return this;
    }

    public HttpBuilder params(@NonNull Map<String, String> params) {
        this.params.putAll(params);
        return this;
    }

    public HttpBuilder params(@NonNull String key, String value) {
        this.params.put(key, value);
        return this;
    }

    public HttpBuilder params(@NonNull String key, Integer value) {
        this.params.put(key, value);
        return this;
    }

    public HttpBuilder params(@NonNull String key, Long value) {
        this.params.put(key, value);
        return this;
    }

    public HttpBuilder params(@NonNull String key, Short value) {
        this.params.put(key, value);
        return this;
    }

    public HttpBuilder params(@NonNull String key, Byte value) {
        this.params.put(key, value);
        return this;
    }

    public HttpBuilder params(@NonNull String key, Boolean value) {
        this.params.put(key, value);
        return this;
    }

    public HttpBuilder headers(@NonNull Map<String, String> headers) {
        this.headers.putAll(headers);
        return this;
    }

    public HttpBuilder header(@NonNull String key, String value) {
        this.headers.put(key, value);
        return this;
    }

    @CheckResult
    public HttpBuilder success(@NonNull Success success) {
        this.mSuccessCallBack = success;
        return this;
    }

    public HttpBuilder progress(@NonNull Progress progress) {
        this.mProgressListener = progress;
        return this;
    }

    @CheckResult
    public HttpBuilder error(@NonNull Error error) {
        this.mErrorCallBack = error;
        return this;
    }

    /**
     * 检查网络是否连接，未连接跳转到网络设置界面
     *
     * @author gengqiquan
     * @date 2017/3/25 下午3:27
     */
    public HttpBuilder isConnected() {
        checkNetConnected = true;
        return this;
    }


    private void setParams(String url) {
        if (HttpUtil.getmInstance() == null) {
            throw new NullPointerException("HttpUtil has not be initialized");
        }
        this.url = url;
        this.params = new HashMap<>();
    }

    @CheckResult
    private String checkUrl(String url) {
        if (HttpUtil.checkNULL(url)) {
            throw new NullPointerException("absolute url can not be empty");
        }
        return url;
    }

    @CheckResult
    public String message(String mes) {
        if (HttpUtil.checkNULL(mes)) {
            mes = "服务器异常，请稍后再试";
        }

        if (mes.equals("timeout") || mes.equals("SSL handshake timed out")) {
            return "网络请求超时";
        } else {
            return mes;
        }

    }

    /**
     * 请求前初始检查
     *
     * @author gengqiquan
     * @date 2017/3/25 下午4:12
     */
    boolean allready() {
        if (!checkNetConnected || mContext == null) {
            return true;
        }
        if (!NetUtils.isConnected(mContext)) {
            Toast.makeText(mContext, "网络不可用", Toast.LENGTH_SHORT).show();
//            NetUtils.openSetting(mContext);//跳转到网络设置界面
            return false;
        }
        return true;
    }

    public void get() {
        if (!allready()) {
            return;
        }
        showLoading();
        addHeader();
        Call call = HttpUtil.getService().get(checkUrl(this.url), checkParams(params), checkHeaders(headers));
        putCall(tag, url, call);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                hideLoading();
                if ((response.code() == 200) || (response.code() == 201)) {
                    mSuccessCallBack.Success(response.body());
                } else {
                    try {
                        String message = response.errorBody().string();
                        mErrorCallBack.Error(message, "错误");
                        errorCodeProcess(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (tag != null) {
                    HttpUtil.removeCall(url);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                hideLoading();
                t.printStackTrace();
                mErrorCallBack.Error(message(t.getMessage()), Constant.MESSAGE_FAILURE);
                if (tag != null) {
                    HttpUtil.removeCall(url);
                }
            }
        });
    }

    public void post() {
        if (!allready()) {
            return;
        }
        addHeader();

        JSONObject jsonObject = new JSONObject(params);
        Call call = HttpUtil.getService().post(checkUrl(this.url), jsonObject.toString(), checkHeaders(headers));
//        Call call = HttpUtil.getService().post(checkUrl(this.url), checkParams(params), checkHeaders(headers));
        putCall(tag, url, call);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if ((response.code() == 200) || (response.code() == 201)) {
                    mSuccessCallBack.Success(response.body());
                } else {
                    try {
                        String message = response.errorBody().string();
                        mErrorCallBack.Error(message, Constant.MESSAGE_RESPONSE);
                        errorCodeProcess(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (tag != null) {
                    HttpUtil.removeCall(url);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                t.printStackTrace();
                mErrorCallBack.Error(message(t.getMessage()), Constant.MESSAGE_FAILURE);
                if (tag != null) {
                    HttpUtil.removeCall(url);
                }
            }
        });
    }

    public void put(String path) {
        if (!allready()) {
            return;
        }
        addHeader();

        File uploadfile = new File(path);
        // 创建 RequestBody，用于封装 请求RequestBody
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), uploadfile);
        // MultipartBody.Part 和后端约定好Key，这里的partName是用image
        MultipartBody.Part body = MultipartBody.Part.createFormData("avatar", uploadfile.getName(), requestFile);

        Call call = HttpUtil.getService().put(checkUrl(this.url), body, checkHeaders(headers));

        putCall(tag, url, call);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if ((response.code() == 200) || (response.code() == 201)) {
                    mSuccessCallBack.Success(response.toString());
                } else {
                    try {
                        String message = response.errorBody().string();
                        mErrorCallBack.Error(message, Constant.MESSAGE_RESPONSE);
                        errorCodeProcess(message);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (tag != null) {
                    HttpUtil.removeCall(url);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                mErrorCallBack.Error(message(t.getMessage()), Constant.MESSAGE_FAILURE);
                if (tag != null) {
                    HttpUtil.removeCall(url);
                }
            }
        });
    }

    private void errorCodeProcess(String message) {
        ErrorResponse errorresponse = new ErrorResponse(message);
        //error code 2001 是服务器返回用户不存在，登录用户不存在或者账号在另外设备登录返回此值
//        if(errorresponse != null && errorresponse.getSuccessCode() == 2001
//                && !"com.newservice.peanut_android.activity.LoginActivity".equals(myActivity.getComponentName().getClassName())) {
//        }
    }

    public Observable<ResponseBody> Obdownload() {
        this.url = checkUrl(this.url);
        this.params = checkParams(this.params);
        this.headers.put(Constant.DOWNLOAD, Constant.DOWNLOAD);
        this.headers.put(Constant.DOWNLOAD_URL, this.url);
        return HttpUtil.getService().Obdownload(checkHeaders(headers), url, checkParams(params));
    }

   //下载
    public void download() {
        this.url = checkUrl(this.url);
        this.params = checkParams(this.params);
//        this.headers.put(Constant.DOWNLOAD, Constant.DOWNLOAD);
//        this.headers.put(Constant.DOWNLOAD_URL, this.url);
        final Call call = HttpUtil.getService().download(checkHeaders(headers), url, checkParams(params));
        putCall(tag, url, call);

        Observable<ResponseBody> observable = Observable.create(new ObservableOnSubscribe<ResponseBody>() {
            @Override
            public void subscribe(final ObservableEmitter<ResponseBody> emitter) throws Exception {
                call.enqueue(new Callback<ResponseBody>(){

                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        emitter.onNext(response.body());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        mErrorCallBack.Error(t.getMessage(),t.toString());
                    }
                });
            }
        });
//        Observable<ResponseBody> observable = Observable.create(subscriber -> {
//                    call.enqueue(new Callback<ResponseBody>() {
//                        @Override
//                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                            subscriber.onNext(response.body());
//                        }
//
//                        @Override
//                        public void onFailure(Call<ResponseBody> call, Throwable t) {
//                            mErrorCallBack.Error(t);
//                        }
//                    });
//                }
//        );


//        observable.observeOn(Schedulers.io())
//                .subscribe(body -> WriteFileUtil.writeFile(body, path, mProgressListener, mSuccessCallBack, mErrorCallBack), t -> {
//                            mErrorCallBack.Error(t);
//                        }
//                );

        observable.observeOn(Schedulers.io()).subscribe(new io.reactivex.functions.Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Exception {
                WriteFileUtil.writeFile(responseBody,path,fileName,mProgressListener,mSuccessCallBack,mErrorCallBack);
            }
        });
//                .subscribe(body -> WriteFileUtil.writeFile(body, path, mProgressListener, mSuccessCallBack, mErrorCallBack), t -> {
//                            mErrorCallBack.Error(t);
//                        }
//                );
    }
    public boolean isShow_progressbar() {
        return show_progressbar;
    }

    /**
     * 在不需要显示加载动画的时候 设置为false 默认为true
     * @param show_progressbar
     * @return
     */
    public HttpBuilder setShow_progressbar(boolean show_progressbar) {
        this.show_progressbar = show_progressbar;
        return this;
    }

    private void showLoading() {
        try {
//            if (context != null && show_progressbar) {
//                HttpState state = context.getHttpState();
//                if (state == null) {
//                    state = new HttpState(context);
//                    context.AddHttpState(state);
//                }
//                state.show();
//            }
        } catch (Exception e) {
        }
    }

    private void hideLoading() {
//        if (context != null && show_progressbar) {
//            HttpState state = context.getHttpState();
//            if (state != null) {
//                if (state.remove()) {
//                    context.RemoveHttpState();
//                }
//            }
//        }
    }

    @CheckResult
    public Observable<String> Obget() {
        return HttpUtil.getService().Obget(checkUrl(this.url), checkParams(params), checkHeaders(headers))
                ;
    }

    @CheckResult
    public Observable<String> Obpost() {
        return HttpUtil.getService().Obpost(checkUrl(this.url), checkParams(params), checkHeaders(headers))
                ;
    }

    @CheckResult
    public Observable<String> Obput() {
        return HttpUtil.getService().Obput(checkUrl(this.url), checkParams(params), checkHeaders(headers))
                ;
    }

}