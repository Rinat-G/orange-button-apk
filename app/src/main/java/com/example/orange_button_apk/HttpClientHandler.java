package com.example.orange_button_apk;

import static okhttp3.HttpUrl.parse;
import static okhttp3.RequestBody.create;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpClientHandler {

    private final OkHttpClient client;


    @Inject
    public HttpClientHandler() {
        client = new OkHttpClient();
    }

    public void makeSessionRequest(String idToken, Callback callback, String baseUrl) {

        HttpUrl httpUrl = parse(baseUrl + "/session")
                .newBuilder()
                .addQueryParameter("token", idToken)
                .build();
        Request request = new Request.Builder()
                .url(httpUrl)
                .addHeader("Content-Type", "application/json")
                .post(create("", null))
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
    }
}
