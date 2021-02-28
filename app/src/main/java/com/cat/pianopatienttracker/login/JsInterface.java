package com.cat.pianopatienttracker.login;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class JsInterface {
    Context mContext;

    JsInterface(Context mContext) {
        mContext = mContext;
    }

    @JavascriptInterface
    public void ssoLoginCallBack(String tokenObj) throws JSONException {
        JSONObject object = new JSONObject(tokenObj);
//        String key = object.getString("access_token");
        Log.e("TAG", tokenObj);
    }
}
