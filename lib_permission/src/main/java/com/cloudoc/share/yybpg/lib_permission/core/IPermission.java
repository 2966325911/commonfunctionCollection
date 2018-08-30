package com.cloudoc.share.yybpg.lib_permission.core;

/**
 * @author : Vic
 * time   : 2018/06/20
 * desc   :
 */
public interface IPermission {
    /**
     * 已经授权
     */
    void granted();

    /**
     *  取消授权
     */
    void canceled();

    /**
     * 被拒绝后，不再授权提示
     */

     void denied();
}
