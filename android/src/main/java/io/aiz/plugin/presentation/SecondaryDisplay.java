package io.aiz.plugin.presentation;

import android.annotation.SuppressLint;
import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.graphics.Canvas;
import com.imin.image.ILcdManager;


import com.getcapacitor.Plugin;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class SecondaryDisplay extends Presentation {
    PresentationAPIPlugin capPlugin = new PresentationAPIPlugin();

    protected String url = "";
    private static Bitmap bitmap;
    private static final String manufacture = (String) Build.MANUFACTURER;
    private static final String deviceModel = (String) Build.MODEL;
    private static final List<String> iMinModel = Arrays.asList("D1", "D1 Pro", "Falcon1"); // allowed i2c display

    public SecondaryDisplay(Context outerContext, Display display) {
        super(outerContext, display);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secondary_display);

        WebView webView = findViewById(R.id.secondary_webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        String path = url;

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String _url) {
                capPlugin.notifyToSuccess(webView, _url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    capPlugin.notifyToFail(webView, error.getErrorCode());
                } else {
                    capPlugin.notifyToFail(webView, 400);
                }
            }
        });
        if (path.startsWith("http")) {
            webView.loadUrl(path);
        } else {
            if (isI2CDisplay()) {
                ILcdManager.getInstance(SecondaryDisplay.this.getContext()).sendLCDBitmap(convertHtmlToBitmap(path, webView));
            }
            webView.loadDataWithBaseURL(null, path, "text/html", "UTF-8", null);
        }
    }

    public void loadUrl(String url) {
        this.url = url;
    }

    public static boolean isI2CDisplay() {
        return (manufacture.equals("iMin") && iMinModel.contains(deviceModel));
    }

    public static Bitmap convertHtmlToBitmap(String html, final WebView webView) {
        // Load HTML content into the WebView
        webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);

        // Wait for the WebView to finish loading the content
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                // Once the page is loaded, capture the content as bitmap
                captureWebView(webView);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                // Handle error if any
            }
        });

        return bitmap;
    }

    private static void captureWebView(final WebView webView) {
        // Create a bitmap with the same dimensions as the WebView
        bitmap = Bitmap.createBitmap(webView.getWidth(), webView.getHeight(), Bitmap.Config.ARGB_8888);

        // Draw the WebView content onto the bitmap
        Canvas canvas = new Canvas(bitmap);
        webView.draw(canvas);

        // Since drawing may take a moment, invalidate the view
        webView.invalidate();
    }
}
