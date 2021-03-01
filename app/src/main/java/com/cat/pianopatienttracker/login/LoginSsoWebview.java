package com.cat.pianopatienttracker.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.cat.pianopatienttracker.R;

public class LoginSsoWebview extends AppCompatActivity {

    WebView webView ;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sso_webview);

        webView = findViewById(R.id.webView);

//        url = "https://test.piano-tracker.net/login/azure";
        url = "http://dev.ptracker.org/test";

        final ProgressDialog progDailog = ProgressDialog.show(this, "","Loading ,Please wait...", true);
        progDailog.setCancelable(true);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.addJavascriptInterface(new JsInterface(LoginSsoWebview.this), "AndroidFunction");

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                // DOT CALL SUPER METHOD
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progDailog.show();
                view.loadUrl(url);

                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                progDailog.dismiss();
            }
        });

        webView.loadUrl(url);
    }
}