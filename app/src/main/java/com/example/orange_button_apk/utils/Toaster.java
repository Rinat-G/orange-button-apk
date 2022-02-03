package com.example.orange_button_apk.utils;

import android.app.Activity;
import android.widget.Toast;

public class Toaster {
    public static void makeToast(Activity activity, String message, int length) {
        activity.runOnUiThread(() -> {
            Toast.makeText(activity, message, length).show();
        });
    }

    public static void makeToastShort(Activity activity, String message) {
        makeToast(activity, message, Toast.LENGTH_SHORT);
    }

    public static void makeToastLong(Activity activity, String message) {
        makeToast(activity, message, Toast.LENGTH_LONG);
    }

}
