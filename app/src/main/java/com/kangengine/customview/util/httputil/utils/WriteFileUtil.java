package com.kangengine.customview.util.httputil.utils;

import android.os.Handler;
import android.os.Looper;

import com.kangengine.customview.util.httputil.interfaces.Error;
import com.kangengine.customview.util.httputil.interfaces.Progress;
import com.kangengine.customview.util.httputil.interfaces.Success;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;

public class WriteFileUtil {
    public static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void writeFile(ResponseBody body, String path, final Progress progress, Success mSuccessCallBack, Error mErrorCallBack) {
        File futureStudioIconFile = new File(path);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        final ProgressInfo progressInfo = new ProgressInfo();
        try {
            byte[] fileReader = new byte[4096];
            progressInfo.total = body.contentLength();
            progressInfo.read = 0;
            inputStream = body.byteStream();
            outputStream = new FileOutputStream(futureStudioIconFile);
            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
                progressInfo.read += read;
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        progress.progress((float) progressInfo.read / progressInfo.total);
                    }
                });
            }
            mSuccessCallBack.Success(path);
            outputStream.flush();
        } catch (IOException e) {
            mErrorCallBack.Error(e.getMessage(),e.toString());
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
            }

        }

    }

    static class ProgressInfo {
        public long read = 0;
        public long total = 0;
    }
}
