package com.example;

public class ResponseData {

    private String message;

    public ResponseData() {
    }

    public ResponseData(String message) {
        this.message = message;
    }

    // Getter和Setter方法
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
