package io.aiz.plugin.presentation;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Display;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import java.util.Arrays;

@CapacitorPlugin(name = "PresentationAPI")
public class PresentationAPIPlugin extends Plugin {

    private PresentationAPI implementation = new PresentationAPI();
    public DisplayManager displayManager = null;
    public Display[] presentationDisplays = null;
    final String SUCCESS_CALL_BACK = "onSuccessLoadUrl";
    final String FAIL_CALL_BACK = "onFailLoadUrl";

    @PluginMethod
    public void echo(PluginCall call) {
        String value = call.getString("value");

        JSObject ret = new JSObject();
        ret.put("value", implementation.echo(value));
        call.resolve(ret);
    }

    @PluginMethod
    public void openRawHtml(PluginCall call) {
        String html = call.getString("htmlStr");

        JSObject ret = new JSObject();
        ret.put("html", html);
        new Handler(Looper.getMainLooper())
                .post(
                        () -> {
                            displayManager = (DisplayManager) getActivity().getSystemService(Context.DISPLAY_SERVICE);
                            if (displayManager != null) {
                                presentationDisplays = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
                                if (presentationDisplays.length > 0) {
                                    Log.d("presentationDisplays", String.valueOf(presentationDisplays[0]));
                                    SecondaryDisplay secondaryDisplay = new SecondaryDisplay(getContext(), presentationDisplays[0]);
                                    secondaryDisplay.loadUrl(html);
                                    Log.d("-> SecondaryDisplay", "Çalışıyor...");
                                    secondaryDisplay.show();
                                }
                            }
                        }
                );

        call.resolve(ret);
    }

    @PluginMethod
    public void openLink(PluginCall call) {
        String url = call.getString("url");

        JSObject ret = new JSObject();
        ret.put("url", url);
        new Handler(Looper.getMainLooper())
                .post(
                        () -> {
                            displayManager = (DisplayManager) getActivity().getSystemService(Context.DISPLAY_SERVICE);
                            if (displayManager != null) {
                                presentationDisplays = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
                                if (presentationDisplays.length > 0) {
                                    Log.d("presentationDisplays", String.valueOf(presentationDisplays[0]));
                                    SecondaryDisplay secondaryDisplay = new SecondaryDisplay(getContext(), presentationDisplays[0]);
                                    secondaryDisplay.loadUrl(url);
                                    Log.d("-> SecondaryDisplay", "Çalışıyor...");
                                    secondaryDisplay.show();
                                }
                            }
                        }
                );

        call.resolve(ret);
    }

    public void notifyToSuccess(WebView view, String url) {
        JSObject response = new JSObject();
        response.put("url", url);
        response.put("message", "success");
        notifyListeners(SUCCESS_CALL_BACK, response, true);
    }

    public void notifyToFail(WebView view, int errorCode) {
        JSObject response = new JSObject();
        response.put("url", errorCode);
        response.put("message", "fail");
        notifyListeners(FAIL_CALL_BACK, response, true);
    }

    @PluginMethod
    public void getDisplays(PluginCall call) {

        displayManager = (DisplayManager) getActivity().getSystemService(Context.DISPLAY_SERVICE);
        JSObject response = new JSObject();
        int displays = 0;

        if (displayManager != null) {

            presentationDisplays = displayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
            displays = presentationDisplays.length;

        }
        response.put("displays", displays);
        call.resolve(response);
    }

}
