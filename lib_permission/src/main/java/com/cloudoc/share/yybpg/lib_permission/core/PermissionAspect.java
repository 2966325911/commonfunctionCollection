package com.cloudoc.share.yybpg.lib_permission.core;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.cloudoc.share.yybpg.lib_permission.PermissionActivity;
import com.cloudoc.share.yybpg.lib_permission.PermissionUtils;
import com.cloudoc.share.yybpg.lib_permission.annotation.Permission;
import com.cloudoc.share.yybpg.lib_permission.annotation.PermissionCanceled;
import com.cloudoc.share.yybpg.lib_permission.annotation.PermissionDenied;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author : Vic
 * time   : 2018/06/20
 * desc   :
 */
@Aspect
public class PermissionAspect {
    @Pointcut("execution(@com.cloudoc.share.yybpg.lib_permission.annotation.Permission * *(..)) && @annotation(permission)")
    public void requestPermission(Permission permission) {

    }

    @Around("requestPermission(permission)")
    public void aroundJointPoint(final ProceedingJoinPoint joinPoint, Permission permission) {
        Context context = null;

        final Object object = joinPoint.getThis();
        if(joinPoint.getThis() instanceof Context) {
            context = (Context) object;
        } else if(joinPoint.getThis() instanceof Fragment) {
            context = ((android.support.v4.app.Fragment)object).getActivity();
        } else if(joinPoint.getThis() instanceof android.app.Fragment) {
            context = ((android.app.Fragment)object).getActivity();
        }

        if(context == null || permission == null) {
            return;
        }

        final Context finalContext = context;
        PermissionActivity.requestPermission(context, permission.value(), permission.requestCode(), new IPermission() {
            @Override
            public void granted() {
                try {
                    joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void canceled() {
                PermissionUtils.invokeAnnotation(finalContext, PermissionCanceled.class);
            }

            @Override
            public void denied() {
                PermissionUtils.invokeAnnotation(finalContext, PermissionDenied.class);
                PermissionUtils.gotoSetting(finalContext);
            }
        });

    }
}


