package com.bertof.pitestremote.API_client;

import android.content.pm.PackageInstaller;
import android.support.annotation.NonNull;
import android.util.Log;

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
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by bertof on 01/06/17.
 */

public class ConnectionTester {

    private class ConnectionTesterResponse {
        ArrayList<ConnectionTesterResponseOutputItem> output;
    }

    private class ConnectionTesterResponseOutputItem {
        Integer id;
        String text;
    }

    private static final String SERVER_WELCOME_MESSAGE = "PiTest server running";


    /**
     * Test if the server is running at the addres-port given
     *
     * @param url complete url of the server
     * @return true if connection successful, false otherwise
     */
    @NonNull
    public static Boolean testConnection(URL url) {

        HttpClient httpClient = new DefaultHttpClient();

        try {
            HttpGet request = new HttpGet(url.toString());
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            Gson gson = new Gson();
            ConnectionTesterResponse ctr = gson.fromJson(responseString, ConnectionTesterResponse.class);

            //TODO TEMP DEBUG
            Log.d(TAG, responseString);

            return ctr.output.get(0).id.equals(0) && ctr.output.get(0).text.equals(SERVER_WELCOME_MESSAGE);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
