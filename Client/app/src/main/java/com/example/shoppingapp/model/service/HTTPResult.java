package com.example.shoppingapp.model.service;

import java.net.HttpURLConnection;

public class HTTPResult<J> {

    private HTTPRequestTask.RequestType requestType;
    private J resultObject;
    private int resultCode;

    public HTTPResult(J resultObject, int resultCode, HTTPRequestTask.RequestType requestType) {
        this.resultObject = resultObject;
        this.resultCode = resultCode;
        this.requestType = requestType;
    }

    public HTTPResult(int resultCode) {
        this.resultCode = resultCode;
    }

    public J getResultObject() {
        return resultObject;
    }

    public int getResultCode() {
        return resultCode;
    }

    public boolean isSuccessful(){
        return resultCode == HttpURLConnection.HTTP_OK;
    }
}
