package com.example.orange_button_apk;

import static okhttp3.HttpUrl.parse;
import static okhttp3.RequestBody.create;

import com.example.orange_button_apk.model.SessionCloseRequest;
import com.example.orange_button_apk.model.SignUpRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.inject.Inject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpClientHandler {

    private final OkHttpClient client;

    @Inject
    public ObjectMapper objectMapper;

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

    public void makeSignUpRequest(String idToken, Callback callback, String baseUrl, SignUpRequest signUpRequest) {
        try {

            HttpUrl httpUrl = parse(baseUrl + "/signup")
                    .newBuilder()
                    .addQueryParameter("token", idToken)
                    .build();
            Request request = new Request.Builder()
                    .url(httpUrl)
                    .addHeader("Content-Type", "application/json")
                    .post(create(objectMapper.writeValueAsString(signUpRequest), MediaType.parse("application/json")))
                    .build();
            Call call = client.newCall(request);
            call.enqueue(callback);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void makeSessionCloseRequest(String idToken, Callback callback, String baseUrl, SessionCloseRequest signUpRequest) {
        try {

            HttpUrl httpUrl = parse(baseUrl + "/session/close")
                    .newBuilder()
                    .addQueryParameter("token", idToken)
                    .build();
            Request request = new Request.Builder()
                    .url(httpUrl)
                    .addHeader("Content-Type", "application/json")
                    .post(create(objectMapper.writeValueAsString(signUpRequest), MediaType.parse("application/json")))
                    .build();
            Call call = client.newCall(request);
            call.enqueue(callback);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
