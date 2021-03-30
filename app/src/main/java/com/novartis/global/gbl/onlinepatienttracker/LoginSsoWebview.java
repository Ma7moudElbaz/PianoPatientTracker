package com.novartis.global.gbl.onlinepatienttracker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoginSsoWebview extends AppCompatActivity {

    WebView webView;
    EditText urlBar;
    String url;
    TextView go;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sso_webview);

        webView = findViewById(R.id.webView);
        urlBar = findViewById(R.id.url_bar);
        go = findViewById(R.id.go);

        go.setOnClickListener(v -> {
            webView.loadUrl(urlBar.getText().toString());
        });

        url = "https://test.piano-tracker.net/login-sso";
//        url = "https://cat.piano-tracker.net/api/login/azure";
//        url = "https://test.piano-tracker.net/login/azure";
//        url = "http://dev.ptracker.org/test";
//        url = "https://test.piano-tracker.net/redirect";
//        url = "https://forixga.cat-sw.com/login";

        final ProgressDialog progDailog = ProgressDialog.show(this, "", "Loading ,Please wait...", true);
        progDailog.setCancelable(true);

        webView.canGoBack();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setAllowContentAccess(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setSupportMultipleWindows(true);

        webView.addJavascriptInterface(new JsInterface(LoginSsoWebview.this), "AndroidFunction");

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                Toast.makeText(LoginSsoWebview.this, error.getDescription().toString(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progDailog.show();
                view.loadUrl(url);

                urlBar.setText(url);

                List<String> networkList = new ArrayList<>();
                try {
                    for (NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                        if (networkInterface.isUp())
                            networkList.add(networkInterface.getName());
                    }
                } catch (Exception ex) {
//                    Timber.d("isVpnUsing Network List didn't received");
                }

                String message = networkList.toString() + "\n" + "User Agent : " + webView.getSettings().getUserAgentString();
                new AlertDialog.Builder(LoginSsoWebview.this)
                        .setTitle("Network List")
                        .setMessage(message)
                        .setCancelable(true)
                        .show();
                return true;
            }

            @Override
            public void onPageFinished(WebView view, final String url) {
                progDailog.dismiss();
            }
        });

        webView.setWebChromeClient(new WebChromeClient());

        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
            return;
        }
        // Otherwise defer to system default behavior.
        super.onBackPressed();
    }
}