package com.kangengine.customview.util;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

/**
 * @author : Vic
 * time   : 2018/09/06
 * desc   : 具体使用
 */
public class AliYunOssUploadOrDownFileConfig {
    private static final String TAG = AliYunOssUploadOrDownFileConfig.class.getSimpleName();
    private static final String ENDPOINT =  "http://oss-cn-hangzhou.aliyuncs.com";
    private ClientConfiguration conf = null;
    private OSS oss = null;
    private PutObjectRequest put = null;
    private OSSAsyncTask task = null;


    private void initOss(Context context){
        conf = new ClientConfiguration();
        conf.setConnectionTimeout(15*1000);
        conf.setSocketTimeout(15*1000);
        conf.setMaxConcurrentRequest(5);
        conf.setMaxErrorRetry(2);
        OSSLog.enableLog();
        //请求后台服务器返回
        //    "StatusCode":200,
        //    "AccessKeyId":"STS.3p***dgagdasdg",
        //    "AccessKeySecret":"rpnwO9***tGdrddgsR2YrTtI",
        //   "SecurityToken":"CAES+wMIARKAAZhjH0EUOIhJMQBMjRywXq7MQ/cjLYg80Aho1ek0Jm63XMhr9Oc5s˙∂˙∂3qaPer8p1YaX1NTDiCFZWFkvlHf1pQhuxfKBc+mRR9KAbHUefqH+rdjZqjTF7p2m1wJXP8S6k+G2MpHrUe6TYBkJ43GhhTVFMuM3BZajY3VjZWOXBIODRIR1FKZjIiEjMzMzE0MjY0NzM5MTE4NjkxMSoLY2xpZGSSDgSDGAGESGTETqOio6c2RrLWRlbW8vKgoUYWNzOm9zczoqOio6c2RrLWRlbW9KEDExNDg5MzAxMDcyNDY4MThSBTI2ODQyWg9Bc3N1bWVkUm9sZVVzZXJgAGoSMzMzMTQyNjQ3MzkxMTg2OTExcglzZGstZGVtbzI=",
        //   "Expiration":"2017-12-12T07:49:09Z",
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(
                "<StsToken.AccessKeyId>",
                "<StsToken.SecretKeyId>",
                "<StsToken.SecurityToken>"
        );

        oss = new OSSClient(context,ENDPOINT,credentialProvider,conf);

    }


    /**
     * 上传文件
     */
    public void uploadFile(){
        put = new PutObjectRequest(
                "<bucketName>",
                "<objectKey>",
                "<uploadFilePath>");

        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d(TAG, "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });

        task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                Log.d(TAG,"uploadSuccess");
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                // Request exception
                if (clientException != null) {
                    // Local exception, such as a network exception
                    clientException.printStackTrace();
                }
                if (serviceException != null) {
                    // Service exception
                    Log.d(TAG,"ErrorCode="+ serviceException.getErrorCode());
                    Log.d(TAG,"RequestId=" + serviceException.getRequestId());
                    Log.d(TAG,"HostId=" + serviceException.getHostId());
                    Log.d(TAG,"RawMessage=" + serviceException.getRawMessage());
                }
            }
            // task.cancel(); // Cancel the task

            // task.waitUntilFinished(); // Wait till the task is finished
        });
    }


}
