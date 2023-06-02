package com.example.hellorestapi0.model;

public class ResponseTutorial<T> {
    private int status_code;

    private String message;

    private T data;

    public ResponseTutorial(int status_code, String message, T data){
        this.status_code = status_code;
        this.message = message;
        this.data = data;
    }

    public int getStatus_code() {
        return status_code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}
