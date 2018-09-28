package com.kangengine.customview.util.httputil;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by 耿 on 2016/6/28.
 */
public class BaseModel {
    public String data;
    public String message;
    public boolean success;

    public boolean trueSuccess(Context context) {
        if (success) {
            return true;
        } else {
            Toast.makeText(context, message, Toast.LENGTH_SHORT);
            return false;
        }
    }

    public BaseModel(String json) {
        JSONObject obj = new JSONObject();
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        int state = -1;
        try {
            state = obj.getInt("success");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (state == 0) {
            this.success = true;
        } else  {
            try {
                this.success = obj.getBoolean("success");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            this.data = obj.getString("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (HttpUtil.checkNULL(message)) {
            try {
                this.message = obj.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
                message = "";
            }
        }
    }
}
