package com.example.orange_button_apk;

import static com.example.orange_button_apk.utils.Toaster.makeToastLong;
import static com.example.orange_button_apk.utils.Toaster.makeToastShort;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.example.orange_button_apk.fragment.StartFragmentDirections;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AuthCallBack implements Callback {
    private static final String TAG = AuthCallBack.class.getSimpleName();
    private final Activity mActivity;
    private final View rootView;

    public AuthCallBack(Activity activity, View rootView) {
        this.mActivity = activity;
        this.rootView = rootView;
    }

    @Override
    public void onFailure(@NotNull Call call, @NotNull IOException e) {
        Log.e(TAG, "onFailure: " + e.getMessage());
        makeToastLong(mActivity, "Auth request failed. Cause:" + e.getMessage());
        onFail();
    }

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        if (response.isSuccessful()) {
            Log.i(TAG, "onResponse: ok");
            makeToastShort(mActivity, "Auth OK");
            makeToastShort(mActivity, "Session: " + response.body().string());

            onSuccess();
        } else {
            Log.e(TAG, "onResponse: status: " + response.code() + " body:" + response.body());
            makeToastLong(mActivity, "Auth failed. Code: " + response.code());
            if (response.code() == 406) {
                on406();
            }
        }

    }

    private void onSuccess() {
        navigateTo(StartFragmentDirections.actionStartFragmentToMainActivity());
    }

    private void on406() {
        navigateTo(StartFragmentDirections.actionStartFragmentToRegistrationFragment());
    }

    private void onFail() {
        Handler mainHandler = new Handler(mActivity.getMainLooper());
        mainHandler.post(() -> mActivity.findViewById(R.id.try_again).setVisibility(View.VISIBLE));
    }

    private void navigateTo(NavDirections action) {
        Handler mainHandler = new Handler(mActivity.getMainLooper());
        mainHandler.post(() -> {
                    Navigation.findNavController(rootView).navigate(action);
                }
        );
    }
}
