package com.cloudoc.share.yybpg.lib_permission.annotation;

import com.cloudoc.share.yybpg.lib_permission.PermissionUtils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author : Vic
 * time   : 2018/06/20
 * desc   :
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PermissionCanceled {
    int requestCode() default PermissionUtils.DEFAULT_REQUEST_CODE;
}
