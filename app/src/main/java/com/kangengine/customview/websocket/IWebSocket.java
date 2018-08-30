package com.kangengine.customview.websocket;

/**
 * @author : Vic
 * time   : 2018/07/27
 * desc   :
 */
public interface IWebSocket {
    /**
     * 发送数据
     *
     * @param text 需要发送的数据
     */
    void sendText(String text);

    /**
     * 0-未连接
     * 1-正在连接
     * 2-已连接
     */
    int getConnectStatus();

    /**
     * 重新连接
     */
    void reconnect();

    /**
     * 关闭连接
     */
    void stop();
}
