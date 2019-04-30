package com.example.shoppingapp.model.service;

import com.example.shoppingapp.model.IAsyncResult;

import java.io.IOException;
import java.net.ProtocolException;

public class HTTPGetRequestTask<V> extends HTTPRequestTask<Void, Void, V> {





    public HTTPGetRequestTask(String url, Class<V> resultClass, IAsyncResult<V> resultObserver) {
        super(url, RequestType.GET, resultClass, resultObserver);
    }


    @Override
    protected HTTPResult<V> send(Void[] params) throws IOException {
            int responseCode = getConnection().getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + getUrl());
            System.out.println("Response Code : " + responseCode);

            String response = buildResultString();

            //print result
            System.out.println(response);
        return readResult(response, responseCode);
    }




    @Override
    protected void configureConnection() throws ProtocolException {
        super.configureConnection();
        getConnection().setDoOutput(false);
        getConnection().setDoInput(true);
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.GET;
    }
}
