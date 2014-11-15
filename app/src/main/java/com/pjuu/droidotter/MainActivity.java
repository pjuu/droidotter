package com.pjuu.droidotter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends Activity {

    private ProgressBar prgrsBar;

    public Boolean hasConnection() {
        // Get a ConnectivityManager
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public class PjuuWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (hasConnection()) {
                view.loadUrl(url);
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            prgrsBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            prgrsBar.setVisibility(View.GONE);
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // Will display a standard error message if the application can't connect
            view.loadUrl("file:///android_asset/error.html");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
         * Activity stuff
         */
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        /*
         * WebView stuff
         */
        WebView webView = (WebView)findViewById(R.id.webview);
        WebSettings browserSettings = webView.getSettings();
        // We need Javascript on the site
        browserSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new PjuuWebViewClient());
        // The URL is stored in values/strings.xml
        webView.loadUrl(getString(R.string.app_url));

        // Progress bar
        prgrsBar = (ProgressBar)findViewById(R.id.progress_bar);
    }

}
