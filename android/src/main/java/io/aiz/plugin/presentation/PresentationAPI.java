package io.aiz.plugin.presentation;

import android.util.Log;

public class PresentationAPI {

    public String echo(String value) {
        Log.i("Echo", value);
        return value;
    }
}
