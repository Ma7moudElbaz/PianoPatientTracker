package com.novartis.global.gbl.onlinepatienttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.asksira.webviewsuite.WebViewSuite;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

public class LoginSsoWebview extends AppCompatActivity {

    WebView webView ;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sso_webview);

        webView = findViewById(R.id.webView);

        url = "https://test.piano-tracker.net/login-sso";
//        url = "https://test.piano-tracker.net/login/azure";
//        url = "http://dev.ptracker.org/test";
//        url = "https://test.piano-tracker.net/redirect";

        final ProgressDialog progDailog = ProgressDialog.show(this, "","Loading ,Please wait...", true);
        progDailog.setCancelable(true);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.addJavascriptInterface(new JsInterface(LoginSsoWebview.this), "AndroidFunction");

        webView.setWebChromeClient(new WebChromeClient());

//        webView.setWebViewClient(new WebViewClient(){
//            @Override
//            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                // DOT CALL SUPER METHOD
//                super.onReceivedSslError(view, handler, error);
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                progDailog.show();
//                view.loadUrl(url);
//
//                return true;
//            }
//            @Override
//            public void onPageFinished(WebView view, final String url) {
//                progDailog.dismiss();
//            }
//        });

        webView.loadUrl(url);
    }
}