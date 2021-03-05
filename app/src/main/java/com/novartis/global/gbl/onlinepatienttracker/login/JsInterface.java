package com.novartis.global.gbl.onlinepatienttracker.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

public class JsInterface {
    Context mContext;

    JsInterface(Context mContext) {
        this.mContext = mContext;
    }

    @JavascriptInterface
    public void ssoLoginCallBack(String tokenObj) throws JSONException {
        JSONObject object = new JSONObject(tokenObj);
        Log.e("TAG", object.toString() );
        String accessToken = "Bearer "+object.getString("access_token");

        Intent i = new Intent(mContext,LoginActivity.class);
        i.putExtra("accessToken",accessToken);
        mContext.startActivity(i);


    }
}