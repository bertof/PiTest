package com.bertof.pitestremote.API_client;

import android.net.Uri;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by bertof on 11/06/17.
 */


public class ActionsLoader {

    public static final String COMMANDS_PATH = "/commands";
    public static final String SCRIPTS_PATH = "/scripts";

    public static String getCommands(String host, Integer port, String token) {

        HttpClient httpClient = new DefaultHttpClient();

        try {
            URL target = new URL(new URI("http", null, host, port, COMMANDS_PATH, "token=" + token + "&type=json", null).toString());

            HttpGet request = new HttpGet(target.toString());
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            Log.d("RESPONSE", responseString);

            return responseString;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getScripts(String host, Integer port, String token) {

        HttpClient httpClient = new DefaultHttpClient();

        try {
            URL target = new URL(new URI("http", null, host, port, SCRIPTS_PATH, "token=" + token + "&type=json", null).toString());

            HttpGet request = new HttpGet(target.toString());
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");

            Log.d("RESPONSE", responseString);

            return responseString;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
