package com.kangengine.customview.util.httputil;

import org.json.JSONException;
import org.json.JSONObject;

public class ErrorResponse {
    private int success;
    private String message;

    public int getSuccessCode() {
        return success;
    }

    public String getErrorMessage() {
        return message;
    }

    public ErrorResponse(String json) {
        JSONObject obj = new JSONObject();
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            this.success = obj.getInt("success");
        } catch (JSONException e) {
            e.printStackTrace();
            success = 0;
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
