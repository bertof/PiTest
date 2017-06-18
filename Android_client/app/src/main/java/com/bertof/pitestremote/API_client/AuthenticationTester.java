package com.bertof.pitestremote.API_client;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

        Boolean getOk() {
            return ok;
        }

        String getInfo() {
            return info;
        }

        @Nullable
        public String getError() {
            return error;
        }

        private Boolean ok;
        private String info;
        @Nullable
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
    public static Boolean testAuthentication(String host, Integer port, String token) throws Exception {

        AuthenticationTesterResponse authenticationTesterResponse = testAuthenticationCall(host, port, token);

        return authenticationTesterResponse.getOk().equals(true) && authenticationTesterResponse.getInfo().equals(VALID_REQUEST_MESSAGE);
    }

    private static AuthenticationTesterResponse testAuthenticationCall(String hostname, Integer port, String token) throws Exception {

        HttpClient httpClient = new DefaultHttpClient();

        URI target = new URI("http", null, hostname, port, AUTH_PATH, "token=" + token, null);

        try {
            HttpGet request = new HttpGet(target);
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            Gson gson = new Gson();
            return gson.fromJson(responseString, AuthenticationTester.AuthenticationTesterResponse.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        throw new Exception("Connection failed");
    }
}
