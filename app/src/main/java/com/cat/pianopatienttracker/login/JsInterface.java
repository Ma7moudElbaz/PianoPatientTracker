package com.cat.pianopatienttracker.login;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class JsInterface {
    Context mcontext;

    JsInterface(Context mcontext) {
        mcontext = mcontext;
    }

    @JavascriptInterface
    public void ssoLoginCallBack(String token) {
        Log.e("TAG", token);
    }
}
