package com.bertof.pitestremote.API_client;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AUTH;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import static android.content.ContentValues.TAG;

/**
 * Created by bertof on 06/06/17.
 */

public class AuthenticationTester {

    private static final String AUTH_PATH = "/auth";
    private static final String VALID_REQUEST_MESSAGE = "valid request";

    private class AuthenticationTesterResponse {
        private Boolean ok;
        private String info;
        private String error;
    }

    /**
     * Test the authentication token given
     *
     * @param host  hostname of the server
     * @param port  port of the server
     * @param token authentication token
     * @return true if connection successful, false otherwise
     */
    @NonNull
    public static Boolean testAuthentication(String host, Integer port, String token) {

        HttpClient httpClient = new DefaultHttpClient();

        try {

            URL url = new URL(new URI("http", null, host, port, AUTH_PATH, "token=" + token, null).toString());

            Log.d(TAG, url.toString());

            HttpGet request = new HttpGet(url.toString());
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            Gson gson = new Gson();
            AuthenticationTester.AuthenticationTesterResponse atr = gson.fromJson(responseString, AuthenticationTester.AuthenticationTesterResponse.class);

            //TODO TEMP DEBUG
            Log.d("Authentication", responseString);

            return atr.ok.equals(true) && atr.info.equals(VALID_REQUEST_MESSAGE);

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }

        return false;
    }
}
