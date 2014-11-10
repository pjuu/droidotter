package com.pjuu.droidotter;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.view.Window;

public class MainActivity extends Activity {

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
        WebView browser = (WebView)findViewById(R.id.webview);
        WebSettings browserSettings = browser.getSettings();
        // We need Javascript on the site
        browserSettings.setJavaScriptEnabled(true);
        browser.setWebViewClient(new Browser());
        // The URL is stores in values/strings.xml
        browser.loadUrl(getString(R.string.app_url));
    }

    private class Browser extends WebViewClient {
        /*
         * Slightly customized version of WebViewClient
         */

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode,
                                    String description, String failingUrl) {
            // Will display a standard error message if the application can't connect
            view.loadUrl("file:///android_asset/error.html");
        }
    }
}
