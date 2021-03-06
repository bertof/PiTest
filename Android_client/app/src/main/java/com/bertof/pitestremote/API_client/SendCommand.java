package com.bertof.pitestremote.API_client;

import android.support.annotation.NonNull;

import com.google.gson.Gson;

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

public class SendCommand {

    public static class SendCommandException extends Exception {
        SendCommandException(String message) {
            super(message);
        }
    }

    private static final String EXEC_PATH = "/exec";

    @NonNull
    public static String sendCommandWithRawResponse(String hostname, int port, String token, String command) throws Exception {
        String response = sendCommandWithRawResponseCall(hostname, port, token, command).trim();

        if (response.contains("\"ok\":false") && response.contains("invalid token")) {
            throw new SendCommandException("Invalid token");
        }

        return response;
    }

    private static String sendCommandWithRawResponseCall(String hostname, int port, String token, String command) throws Exception {

        HttpClient httpClient = new DefaultHttpClient();

        URI target = new URI("http", null, hostname, port, EXEC_PATH, "token=" + token + "&command=" + command + "&type=raw", null);

        HttpGet request = new HttpGet(target);
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        return EntityUtils.toString(entity, "UTF-8");
    }


}
