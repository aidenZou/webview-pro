package com.example.aidenzou.test.myapplication;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by aidenZou on 15/12/25.
 */
public class BaseWebView extends WebView {

    private Activity activity;

    private static final String DEFAULT_URL = "http://m.baidu.com/";
    private String url = "http://m.baidu.com/";

    // 打开指定url
    public void openUrl(String url) {
        this.url = url;
        this.loadUrl(this.url);
    }

    public BaseWebView(Context context) {
        super(context);
        activity = (Activity) context;
        init(context);
    }

    public BaseWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        activity = (Activity) context;
        init(context);
    }

    //    @SuppressLint("SetJavaScriptEnabled")
    private void init(Context context) {
        WebSettings webSettings = this.getSettings();
        // 启用支持 Javascript
        webSettings.setJavaScriptEnabled(true);
//        webSettings.setSupportZoom(true);
        //webSettings.setUseWideViewPort(true);


        // 有限使用缓存
        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        // 不使用缓存
        //webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        // localStorage
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);

//        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();
//        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);

        this.setWebViewClient(baseWebViewClient);
        this.setWebChromeClient(baseWebChromeClient);
        //this.loadUrl(DEFAULT_URL);
        this.loadUrl(this.url);

        // 屏蔽掉长按事件 因为webview长按时将会调用系统的复制控件
        this.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        this.onResume();
    }

    private BaseWebViewClient baseWebViewClient = new BaseWebViewClient();

    // WebViewClient帮助 WebView去处理一些页面控制 和 请求通知
    private class BaseWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // 阻止通过默认浏览器打开网页,此方法可以在webview中打开链接而不会跳转到外部浏览器
            view.loadUrl(url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        // 网页加载完
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // 在这里执行你想调用的js函数
            //Toast.makeText(MainActivity.this, "页面加载完毕", Toast.LENGTH_SHORT).show();
        }

        // WebView 错误码处理
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);

            //view.loadUrl("file:///android_asset/error.html");
//            Toast.makeText(WebActivity.this, "页面加载错误，错误码：" + errorCode, Toast.LENGTH_SHORT).show();
        }

        // 处理https请求，为WebView处理ssl证书设置
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();  // 接受信任所有网站的证书
            // handler.cancel();   // 默认操作 不处理
            // handler.handleMessage(null);  // 可做其他处理
            //super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            super.doUpdateVisitedHistory(view, url, isReload);
        }
    }

    private BaseWebChromeClient baseWebChromeClient = new BaseWebChromeClient();

    private class BaseWebChromeClient extends WebChromeClient {
        // 实现进度条
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // newProgress 1-100 之间的整数

//                if (newProgress == 100) {
//                    // 网页加载完毕
//                    closeDialog();
//                } else {
//                    // 网页正在加载
//                    //openDialog(newProgress);
//                }

            if (newProgress == 100) {
//                progressBar.setVisibility(View.INVISIBLE);
            } else {
//                if (View.INVISIBLE == progressBar.getVisibility()) {
//                    progressBar.setVisibility(View.VISIBLE);
//                }
//                progressBar.setProgress(newProgress);
            }

            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            // 设置title
            // toolbar.setTitle(title);
            if (activity != null) {
                activity.setTitle(title);
            }
            super.onReceivedTitle(view, title);
        }

        @Override
        public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
            super.onReceivedTouchIconUrl(view, url, precomposed);
        }

        @Override
        public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
            return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
        }
    }
}
