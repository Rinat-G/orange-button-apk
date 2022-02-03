package com.example.orange_button_apk;

import android.app.Activity;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static com.example.orange_button_apk.utils.Toaster.makeToastLong;
import static com.example.orange_button_apk.utils.Toaster.makeToastShort;

public class AuthCallBack implements Callback {
    private static final String TAG = AuthCallBack.class.getSimpleName();
    private final Activity mActivity;

    public AuthCallBack(Activity activity) {
        this.mActivity = activity;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Log.e(TAG, "onFailure: " + e.getMessage());
        makeToastLong(mActivity, "Auth request failed");
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (response.isSuccessful()) {
            Log.i(TAG, "onResponse: ok");
            makeToastShort(mActivity, "Auth OK");
        } else {
            Log.e(TAG, "onResponse: status: " + response.code() + " body:" + response.body());
            makeToastLong(mActivity, "Auth failed. Code: " + response.code());
        }

    }

}