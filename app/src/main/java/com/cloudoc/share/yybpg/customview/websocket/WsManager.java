package com.cloudoc.share.yybpg.customview.websocket;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketListener;

/**
 * @author : Vic
 * time   : 2018/07/27
 * desc   :
 */
public class WsManager  {
    private static WsManager mWsManager;

    private WebSocket webSocket;
    private String url;
    private WsManager(){

    }

    public  static final WsManager getInstance(){
        if(mWsManager == null) {
            synchronized (WsManager.class) {
                if(mWsManager == null) {
                    mWsManager = new WsManager();
                }
            }
        }
        return mWsManager;
    }


   
}
