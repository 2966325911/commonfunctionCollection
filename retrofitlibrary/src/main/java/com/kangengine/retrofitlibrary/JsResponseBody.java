package com.kangengine.retrofitlibrary;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * @author : Vic
 * time   : 2018/09/24
 * desc   :下载请求体
 */
public class JsResponseBody extends ResponseBody {
    private final static String TAG = JsResponseBody.class.getSimpleName();
    private ResponseBody responseBody;
    private JsDownloadListener downloadListener;

    /**
     * 为okio库中的io流当做inputStream使用
     */
    private BufferedSource bufferedSource;

    public JsResponseBody (ResponseBody responseBody,JsDownloadListener jsDownloadListener) {
        this.responseBody = responseBody;
        this.downloadListener = jsDownloadListener;
    }

    @Nullable
    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if(bufferedSource == null){
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource ;
    }

    private Source source(Source source){
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                // read() returns the number of bytes read, or -1 if this source is exhausted.
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                Log.e("download", "read: "+ (int) (totalBytesRead * 100 / responseBody.contentLength()));
                if (null != downloadListener) {
                    if (bytesRead != -1) {
                        downloadListener.onProgress((int) (totalBytesRead * 100 / responseBody.contentLength()));
                    }
                }
                Log.d(TAG,"======totalBytesRead===" + totalBytesRead + "==responseBody.contentLength()=="+responseBody.contentLength() );
                if(totalBytesRead == responseBody.contentLength()) {

                    if(null != downloadListener) {
                        downloadListener.onFinishDownload();
                    }
                }
                return bytesRead;
            }
        };
    }
}
