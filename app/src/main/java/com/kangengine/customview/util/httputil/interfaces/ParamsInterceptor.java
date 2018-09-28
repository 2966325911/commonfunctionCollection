package com.kangengine.customview.util.httputil.interfaces;

import java.util.Map;

/**
 * Created by Administrator on 2016/11/23.
 */
@FunctionalInterface
public interface ParamsInterceptor {
    Map checkParams(Map params);
}
