package com.example.orange_button_apk.model;

import java.util.List;

public class SignUpRequest {
    private final List<String> guardEmails;
    private final String pin;

    public SignUpRequest(List<String> guardEmails, String pin) {
        this.guardEmails = guardEmails;
        this.pin = pin;
    }

    public List<String> getGuardEmails() {
        return guardEmails;
    }

    public String getPin() {
        return pin;
    }
}