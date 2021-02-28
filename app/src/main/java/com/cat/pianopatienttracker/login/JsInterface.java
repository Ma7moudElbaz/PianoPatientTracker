package com.cat.pianopatienttracker.login;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class JsInterface {
    Context mcontext;

    JsInterface(Context mcontext) {
        mcontext = mcontext;
    }

    @JavascriptInterface
    public void ssoLoginCallBack(String tokenObj) throws JSONException {
        JSONObject object = new JSONObject(tokenObj);
        String key = object.getString("key");
        Log.e("TAG", key);
    }
}
