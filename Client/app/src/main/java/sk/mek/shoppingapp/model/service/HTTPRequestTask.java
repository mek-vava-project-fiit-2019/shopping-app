package sk.mek.shoppingapp.model.service;

import android.net.Uri;
import android.os.AsyncTask;

import com.example.shoppingapp.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import sk.mek.shoppingapp.model.IAsyncResult;

public abstract class HTTPRequestTask<A, G, V> extends AsyncTask<A, G, HTTPResult<V>> {

    public static final int CONFIG_READ_TIMEOUT = 15000;
    public static final int CONFIG_CONNECTION_TIMEOUT = 15000;
    private String url;


    private Class<V> resultClass;

    private HttpURLConnection connection = null;
    private RequestType requestType;
    private IAsyncResult<V> resultObserver;


    public HTTPRequestTask(String url, RequestType requestType, Class<V> resultClass, IAsyncResult<V> resultObserver) {
        this.url = url;
        this.requestType = requestType;
        this.resultClass = resultClass;
        this.resultObserver = resultObserver;
    }


    public boolean isConnected() {
        return connection != null;
    }

    protected abstract HTTPResult<V> send(A[] params) throws IOException;


    public boolean createConnection(String url) {

        if (isConnected()) {
            connection.disconnect();
            connection = null;
        }

        try {
            //Create a URL object holding our url
            URL myUrl = new URL(url);
            //Create a connection
            connection = (HttpURLConnection) myUrl.openConnection();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected void configureConnection() throws ProtocolException {

        connection.setRequestMethod(requestType.name());
        //connection.setReadTimeout(CONFIG_READ_TIMEOUT);
        //connection.setConnectTimeout(CONFIG_CONNECTION_TIMEOUT);
        connection.setRequestProperty("Content-Type", BuildConfig.CONFIG_CONTENT_TYPE);

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected HTTPResult<V> doInBackground(A... params) {
        if (createConnection(url)) {
            try {
                configureConnection();
                connection.connect();


                HTTPResult<V> result = send(params);
                connection.disconnect();
                return result;

            } catch (IOException e) {
                e.printStackTrace();
                connection.disconnect();
            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(HTTPResult<V> httpResult) {
        super.onPostExecute(httpResult);


        if (resultObserver != null) {
            resultObserver.update(httpResult);
        }

    }

    private String paramsToString(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }
        Uri.Builder builder = new Uri.Builder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue());
        }
        return builder.build().getEncodedQuery();
    }

    public HttpURLConnection getConnection() {
        return connection;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Class<V> getResultClass() {
        return resultClass;
    }

    protected String buildBody(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;


        Iterator it = params.entrySet().iterator();
        while (it.hasNext()) {
            if (first) {
                first = false;
            } else {
                result.append("&");
            }

            Map.Entry<String,String> pair = (Map.Entry) it.next();


            result.append(URLEncoder.encode(pair.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(pair.getValue(), "UTF-8"));

            it.remove();
        }


        return result.toString();
    }

    protected void writeBody(String body) throws IOException {
        OutputStream os = getConnection().getOutputStream();
        BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(os, "UTF-8"));
        writer.write(body);
        writer.flush();
        writer.close();
        os.close();
    }

    protected HTTPResult<V> readResult(String response, int responseCode){
        V resultObject;

        JsonParser parser = new JsonParser();
        JsonElement mJson =  parser.parse(response);
        Gson gson = new Gson();

        try {
            resultObject = gson.fromJson(mJson, resultClass);
        } catch(JsonSyntaxException e){
            e.printStackTrace();

            getConnection().disconnect();
            return null;
        }
        return new HTTPResult<V>(resultObject,responseCode,getRequestType());
    }

    protected String buildResultString() throws IOException {
        BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    public enum RequestType {
        GET, POST, PUT, DELETE
    }


    public abstract RequestType getRequestType();



}
