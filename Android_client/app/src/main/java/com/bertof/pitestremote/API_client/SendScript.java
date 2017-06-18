package com.bertof.pitestremote.API_client;

import android.support.annotation.NonNull;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URI;

/**
 * Created by bertof on 17/06/17.
 */

public class SendScript {

    private class SendScriptException extends Exception {
        public SendScriptException(String message) {
            super(message);
        }
    }

    private static final String EXEC_PATH = "/scripts";

    @NonNull
    public static String sendScriptWithRawResponse(String hostname, int port, String token, String script) throws Exception {
        return sendScriptWithRawResponseCall(hostname, port, token, script).trim();
    }

    private static String sendScriptWithRawResponseCall(String hostname, int port, String token, String script) throws Exception {

        HttpClient httpClient = new DefaultHttpClient();

        URI target = new URI("http", null, hostname, port, EXEC_PATH + "/" + script, "token=" + token + "&type=raw", null);

        HttpGet request = new HttpGet(target);
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity, "UTF-8");
    }


}
