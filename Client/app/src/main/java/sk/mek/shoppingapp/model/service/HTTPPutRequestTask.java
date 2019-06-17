package sk.mek.shoppingapp.model.service;

import java.io.IOException;
import java.net.ProtocolException;
import java.util.HashMap;

import sk.mek.shoppingapp.model.IAsyncResult;

public class HTTPPutRequestTask<V> extends HTTPRequestTask<HashMap<String,String>, Void, V> {

    public HTTPPutRequestTask(String url, Class<V> resultClass, IAsyncResult<V> resultObserver) {
        super(url, RequestType.PUT, resultClass, resultObserver);
    }

    @Override
    protected HTTPResult<V> send(HashMap<String, String>[] params) throws IOException {
        HashMap<String, String> joinedParams = new HashMap();

        for (HashMap<String, String> element : params) {
            joinedParams.putAll(element);
        }

        String body = buildBody(joinedParams);
        writeBody(body);


        int responseCode = getConnection().getResponseCode();

        String response = buildResultString();

        return readResult(response, responseCode);
    }

    @Override
    protected void configureConnection() throws ProtocolException {
        super.configureConnection();

        getConnection().setDoOutput(true);
        getConnection().setDoInput(true);
    }

    @Override
    public RequestType getRequestType() {
        return RequestType.PUT;
    }

}
