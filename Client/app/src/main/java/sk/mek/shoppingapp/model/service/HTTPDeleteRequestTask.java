package sk.mek.shoppingapp.model.service;

import sk.mek.shoppingapp.model.IAsyncResult;

import java.io.IOException;
import java.net.ProtocolException;

public class HTTPDeleteRequestTask<V> extends HTTPRequestTask<Void, Void, V> {

    public HTTPDeleteRequestTask(String url, Class<V> resultClass, IAsyncResult<V> resultObserver) {
        super(url, RequestType.DELETE, resultClass, resultObserver);
    }

    @Override
    protected HTTPResult<V> send(Void[] params) throws IOException {
        int responseCode = getConnection().getResponseCode();
        System.out.println("\nSending 'DELTE' request to URL : " + getUrl());
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
        return RequestType.DELETE;
    }
}
