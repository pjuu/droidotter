package com.pjuu.droidotter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends Activity {

    private WebView webView;
    private ProgressBar prgrsBar;

    // Hostname extracted from R.string.app_url
    private String hostname;

    public class PjuuWebChromeClient extends WebChromeClient {

    }

    public class PjuuWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (hasConnection()) {
                // Pass the hostname from the requested URL
                String request_hostname = Uri.parse(url).getHost();
                if (request_hostname.equals(hostname)) {
                    // This is a Pjuu URL so open it in the WebView
                    view.loadUrl(url);
                } else {
                    // This is a non Pjuu URL. Open it in the default browser
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
            } else {
                // Doesn't look like you are connected to the internet
                Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT).show();
            }
            return true;
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

    public Boolean hasConnection() {
        // Get a ConnectivityManager
        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Override
    public void onBackPressed (){
        if (webView.isFocused() && webView.canGoBack()) {
            webView.goBack();
        }
        else {
            super.onBackPressed();
            finish();
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
        // Get the hostname from the URL defined in the resources
        // This will let us check all requests to redirect to the browser or not.
        hostname = Uri.parse(getString(R.string.app_url)).getHost();

        webView = (WebView)findViewById(R.id.webview);
        WebSettings browserSettings = webView.getSettings();
        // We need Javascript on the site
        browserSettings.setJavaScriptEnabled(true);

        webView.setWebViewClient(new PjuuWebViewClient());
        webView.setWebChromeClient(new PjuuWebChromeClient());

        // The URL is stored in values/strings.xml
        webView.loadUrl(getString(R.string.app_url));

        // Progress bar
        prgrsBar = (ProgressBar)findViewById(R.id.progress_bar);
    }

}
