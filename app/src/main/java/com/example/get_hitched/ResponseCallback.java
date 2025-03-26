package com.example.get_hitched;

public interface ResponseCallback {
    void onResponse(String response);
    void onError(Throwable throwable);
}