package com.ficture7.aasexplorer.util;

import android.os.Handler;
import android.os.Looper;

public final class LooperUtil {

    private static final Handler UI_HANDLER = new Handler(Looper.getMainLooper());

    public static void runOnUiThread(Runnable runnable) {
        if (UI_HANDLER.getLooper() == Looper.myLooper()) {
           runnable.run();
        } else {
            UI_HANDLER.post(runnable);
        }
    }
}
