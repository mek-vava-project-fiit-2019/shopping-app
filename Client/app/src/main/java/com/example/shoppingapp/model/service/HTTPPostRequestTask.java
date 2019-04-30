package com.example.shoppingapp.model.service;

import com.example.shoppingapp.model.IAsyncResult;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.HashMap;

public class HTTPPostRequestTask<V> extends HTTPRequestTask<HashMap<String,String>,Void, V> {


    public HTTPPostRequestTask(String url, Class resultClass, IAsyncResult<V> resultObserver) {
        super(url, RequestType.POST, resultClass, resultObserver);
    }

    @Override
    protected HTTPResult<V> send(HashMap<String, String>[] params) throws IOException {

        HashMap<String, String> joindParams = new HashMap();

        for (HashMap<String, String> element : params) {
            joindParams.putAll(element);
        }

        String body = buildBody(joindParams);
        writeBody(body);


        int responseCode = getConnection().getResponseCode();

        String response = buildResultString();

        return readResult(response, responseCode);
    }

    @Override
    protected void configureConnection() throws ProtocolException {
        super.configureConnection();
        getConnection().setDoInput(true);
        getConnection().setDoOutput(true);
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.POST;
    }
}
