package com.kangengine.customview.util;

/**
 * @author : Vic
 * time   : 2018/09/14
 * desc   :
 */
public interface OnUploadFile {
    void onUploadFileSuccess(String info);
    void onUploadFileFailed(String errCode);
}
