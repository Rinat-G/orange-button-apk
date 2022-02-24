package com.example.orange_button_apk.model;

public class SessionCloseRequest {
    private final String sessionId;
    private final String pinCode;

    public SessionCloseRequest(String sessionId, String pinCode) {
        this.sessionId = sessionId;
        this.pinCode = pinCode;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getPinCode() {
        return pinCode;
    }
}
