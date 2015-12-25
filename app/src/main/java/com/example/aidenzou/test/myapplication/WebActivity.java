package com.example.aidenzou.test.myapplication;

import android.app.ProgressDialog;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

public class WebActivity extends AppCompatActivity {

    private String url = "https://youcai.shequcun.com/#!/";
    //    private String url = "http://m.baidu.com";
    private ProgressBar progressBar;
    private BaseWebView webView;
    private Toolbar toolbar;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setLogo(R.drawable.ic_sync_black_24dp);
        toolbar.setTitle("My Title");
        toolbar.setSubtitle("Sub title");
        toolbar.setNavigationIcon(R.drawable.ic_notifications_black_24dp);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        webView = (BaseWebView) findViewById(R.id.webView);
        webView.openUrl("http://192.168.1.222:8001");

//        toolbar.setOnMenuItemClickListener(onMenuItemClick);

//        private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem menuItem) {
//                String msg = "";
//                switch (menuItem.getItemId()) {
//                    case R.id.action_edit:
//                        msg += "Click edit";
//                        break;
//                    case R.id.action_share:
//                        msg += "Click share";
//                        break;
//                    case R.id.action_settings:
//                        msg += "Click setting";
//                        break;
//                }
//
//                if(!msg.equals("")) {
//                    Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
//                }
//                return true;
//            }
//        };

//        @Override
//        public boolean onCreateOptionsMenu(Menu menu) {
//            // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
//            getMenuInflater().inflate(R.menu.menu_main, menu);
//            return true;
//        }

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }

    private void init() {
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        webView = (BaseWebView) findViewById(R.id.webView);

        // 配置调试WebViews
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        String appCachePath = getApplicationContext().getCacheDir().getAbsolutePath();

        webView.loadUrl(url);

        // WebViewClient帮助 WebView去处理一些页面控制 和 请求通知
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 阻止通过默认浏览器打开网页,此方法可以在webview中打开链接而不会跳转到外部浏览器
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
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
                Toast.makeText(WebActivity.this, "页面加载错误，错误码：" + errorCode, Toast.LENGTH_SHORT).show();
            }

            // 处理https请求，为WebView处理ssl证书设置
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();  // 接受信任所有网站的证书
                // handler.cancel();   // 默认操作 不处理
                // handler.handleMessage(null);  // 可做其他处理
                //super.onReceivedSslError(view, handler, error);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                // 设置title
                toolbar.setTitle(title);
                super.onReceivedTitle(view, title);
            }

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
                    progressBar.setVisibility(View.INVISIBLE);
                } else {
                    if (View.INVISIBLE == progressBar.getVisibility()) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    progressBar.setProgress(newProgress);
                }

                super.onProgressChanged(view, newProgress);
            }

            private void openDialog(int newProgress) {
                if (dialog == null) {
                    dialog = new ProgressDialog(WebActivity.this);
                    dialog.setTitle("正在加载");
                    dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    dialog.setProgress(newProgress);
                    dialog.show();
                } else {
                    dialog.setProgress(newProgress);
                }
            }

            private void closeDialog() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }

        });

        // 屏蔽掉长按事件 因为webview长按时将会调用系统的复制控件
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
    }

    // 按键事件处理
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();
                return true;
            } else {
                // 结束当前Activity
                this.finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }

}
