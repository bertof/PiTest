package com.bertof.pitestremote.API_client;

import android.app.Activity;
import android.content.pm.PackageInstaller;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by bertof on 01/06/17.
 */

public class ConnectionTester {
    class ConnectionTesterException extends Exception {
    }

    public class ConnectionTesterResponse {
        private ArrayList<ConnectionTesterResponseOutputItem> getOutput() {
            return output;
        }

        ArrayList<ConnectionTesterResponseOutputItem> output;
    }

    public class ConnectionTesterResponseOutputItem {
        private Integer getId() {
            return id;
        }

        private String getText() {
            return text;
        }

        Integer id;
        String text;
    }

    private static final String SERVER_WELCOME_MESSAGE = "PiTest server running";

    /**
     * Test if the server is running at the addres-port given
     *
     * @param hostname address of the host
     * @param port     port to access the host
     * @return true if connection successful, false otherwise
     * @throws Exception General connection error
     */
    @NonNull
    public static Boolean testConnection(String hostname, Integer port) throws Exception {

        ConnectionTesterResponse connectionTesterResponse = testConnectionCall(hostname, port);

        return connectionTesterResponse.getOutput().get(0).getId().equals(0) && connectionTesterResponse.getOutput().get(0).getText().equals(SERVER_WELCOME_MESSAGE);
    }


    /**
     * Method that executes a REST call to the server to verify its presence
     *
     * @param hostname address of the host
     * @param port     port to access the host
     * @return ConnectionTesterResponse object
     * @throws java.net.URISyntaxException wrong URI signature
     * @throws Exception                   generic connection failed exception
     */
    private static ConnectionTesterResponse testConnectionCall(String hostname, Integer port) throws Exception {
        HttpClient httpClient = new DefaultHttpClient();

        URI target = new URI("http", null, hostname, port, "/", "", null);

        HttpGet request = new HttpGet(target);
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");

        Gson gson = new Gson();
        return gson.fromJson(responseString, ConnectionTesterResponse.class);

    }
}
